package me.syrin.monopolis

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_game_dialog.view.*
import me.syrin.monopolis.common.network.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.net.UnknownHostException

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if name is set, if not, send to settings
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("playerName", null)
        if (name == null) {
            startActivity<SettingsActivity>()
        }
        else {
            NetworkHandler.init(name)
        }

        NetworkHandler.connected.observe(this, Observer {
            if (it) {
                // connected
                linear_layout_loading.visibility = View.GONE
                linear_layout_buttons.visibility = View.VISIBLE
            }
            else {
                // not connected
                linear_layout_loading.visibility = View.VISIBLE
                linear_layout_buttons.visibility = View.GONE
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

        button_create_game.setOnClickListener {
            // open dialog to get game name and open pregame
            val inputView = layoutInflater.inflate(R.layout.create_game_dialog, null)
            inputView.player_count.minValue = 2
            inputView.player_count.maxValue = 8
            inputView.player_count.wrapSelectorWheel = false

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Game setup")
            builder.setView(inputView)

            builder.setPositiveButton("Create") { _, _ ->
                // open pregame with passed name and player count
                val name = inputView.game_name.text.toString()
                val playerCount = inputView.player_count.value
                WebSocket.send(CreateLobbyPacket(name, playerCount))
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                // cancel
                dialog.cancel()
            }

            builder.show()
        }

        button_join_game.setOnClickListener {
            // open lobby screen
            startActivity<LobbyActivity>()
        }

        button_settings.setOnClickListener {
            // open settings screen
            startActivity<SettingsActivity>()
        }
    }
}
