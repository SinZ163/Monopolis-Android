package me.syrin.monopolis

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.GameButtonsFragment
import me.syrin.monopolis.common.GenericMessageDialogFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.NetworkHandler
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.startActivity

class GameActivity : FragmentActivity() {
    lateinit var monopolis: Monopolis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        monopolis = Monopolis(this, NetworkHandler.lobby.value!!.players)

        monopolis.start()

        (fragment_board as BoardFragment).initialiseBoard(monopolis)
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
        (fragment_buttons as GameButtonsFragment).uiUpdate()
        text_view_money.text = "₩${monopolis.players.find { player -> player.name == NetworkHandler.name }?.balance}"
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
