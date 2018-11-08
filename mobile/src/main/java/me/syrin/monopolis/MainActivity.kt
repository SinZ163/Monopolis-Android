package me.syrin.monopolis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_game_dialog.view.*
import me.syrin.monopolis.common.network.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("playerName", null)
        if (name == null) {
            startActivityForResult<SettingsActivity>(0)
        } else {
            NetworkHandler.init(name)
        }

        NetworkHandler.connected.observe(this, Observer {
            if (it) {
                // connected
                linear_layout_loading?.visibility = View.GONE
                linear_layout_buttons?.visibility = View.VISIBLE
            }
            else {
                // not connected
                linear_layout_loading?.visibility = View.VISIBLE
                linear_layout_buttons?.visibility = View.GONE
            }
        })

        val disposable = EventBus.subscribe<ConnectionError>()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when(it.error) {
                    is UnknownHostException -> {
                        toast("No internet access (DNS Resolution Failed)")
                    }
                }
                Log.e("MainActivity", "ConnectionError: ${it.error?.javaClass}", it.error)
            }

        NetworkHandler.lobby.observe(this, Observer {
            if (it?.ingame == true) {
                startActivity<GameActivity>()
            } else if (it != null) {
                startActivity<PreGameActivity>()
            }
        })

        button_join_game.setOnClickListener {
            startActivity<LobbyActivity>()
        }
        button_create_game.setOnClickListener {
            // open dialog to get game name and open pregame
            val inputView = layoutInflater.inflate(R.layout.create_game_dialog, null)

            inputView.maxPlayerCount.minValue = 2
            inputView.maxPlayerCount.maxValue = 8

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Game setup")
            builder.setView(inputView)

            builder.setPositiveButton("Create") { dialog, which ->
                // open pregame with passed name and player count
                val name = inputView.name.text.toString()
                val playerCount = inputView.maxPlayerCount.value
                WebSocket.send(CreateLobbyPacket(name, playerCount))
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                // cancel
                dialog.cancel()
            }

            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Have they ever accessed
        if (requestCode == 0) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val name = preferences.getString("playerName", null)
            if (name == null) {
                startActivityForResult<SettingsActivity>(0)
            } else {
                NetworkHandler.init(name)
            }
        } else {
            // TODO: Tell server my name changed?
            toast("Not implemented name change")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_settings) {
            startActivityForResult<SettingsActivity>(1)
        }
        return super.onOptionsItemSelected(item)
    }

}
