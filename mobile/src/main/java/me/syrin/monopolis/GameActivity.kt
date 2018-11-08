package me.syrin.monopolis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.GameButtonsFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.NetworkHandler
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

class GameActivity : AppCompatActivity() {
    lateinit var monopolis: Monopolis
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        monopolis = Monopolis(this, listOf("Sam", "Trent"))

        (fragment2 as BoardFragment).initialiseBoard(monopolis)
        (fragment_buttons as GameButtonsFragment).game = monopolis

        monopolis.uiUpdates.observe(this, Observer {
            updateUi()
        })


        NetworkHandler.connected.observe(this, Observer {
            Log.i("GameActivity", "We are being told to evacuate?")
            if (!it) {
                startActivity(intentFor<MainActivity>().clearTop().singleTop())
            }
        })

        NetworkHandler.lobby.observe(this, Observer {
            if (it == null)
            {
                startActivity(intentFor<MainActivity>().clearTop())
            }
        })
    }

    private fun updateUi() {
        // TODO: update UI
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Leave?")
        builder.setMessage("Are you sure you want to leave this game?")

        builder.setPositiveButton("Leave") { _, _ ->
            // leave game
            WebSocket.send(LeaveLobbyPacket(NetworkHandler.lobby.value!!.id))
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            // cancel
            dialog.cancel()
        }

        builder.show()
    }
}
