package me.syrin.monopolis

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
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
        // TODO: replace this crap
        if (monopolis.players.count() >= 1) {
            text_view_player_colour1.setTextColor(Color.argb(255, Monopolis.COLOURS[0][0], Monopolis.COLOURS[0][1], Monopolis.COLOURS[0][2]))
            text_view_player_colour1.text = "■"
            text_view_player_name1.text = "${monopolis.players[0].name}"
            text_view_player_money1.text = "₩${monopolis.players[0].balance}"
            text_view_player_jailcards1.text = "${monopolis.players[0].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 2) {
            text_view_player_colour2.setTextColor(Color.argb(255, Monopolis.COLOURS[1][0], Monopolis.COLOURS[1][1], Monopolis.COLOURS[1][2]))
            text_view_player_colour2.text = "■"
            text_view_player_name2.text = "${monopolis.players[1].name}"
            text_view_player_money2.text = "₩${monopolis.players[1].balance}"
            text_view_player_jailcards2.text = "${monopolis.players[1].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 3) {
            text_view_player_colour3.setTextColor(Color.argb(255, Monopolis.COLOURS[2][0], Monopolis.COLOURS[2][1], Monopolis.COLOURS[2][2]))
            text_view_player_colour3.text = "■"
            text_view_player_name3.text = "${monopolis.players[2].name}"
            text_view_player_money3.text = "₩${monopolis.players[2].balance}"
            text_view_player_jailcards3.text = "${monopolis.players[2].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 4) {
            text_view_player_colour4.setTextColor(Color.argb(255, Monopolis.COLOURS[3][0], Monopolis.COLOURS[3][1], Monopolis.COLOURS[3][2]))
            text_view_player_colour4.text = "■"
            text_view_player_name4.text = "${monopolis.players[3].name}"
            text_view_player_money4.text = "₩${monopolis.players[3].balance}"
            text_view_player_jailcards4.text = "${monopolis.players[3].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 5) {
            text_view_player_colour5.setTextColor(Color.argb(255, Monopolis.COLOURS[4][0], Monopolis.COLOURS[4][1], Monopolis.COLOURS[4][2]))
            text_view_player_colour5.text = "■"
            text_view_player_name5.text = "${monopolis.players[4].name}"
            text_view_player_money5.text = "₩${monopolis.players[4].balance}"
            text_view_player_jailcards5.text = "${monopolis.players[4].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 6) {
            text_view_player_colour6.setTextColor(Color.argb(255, Monopolis.COLOURS[5][0], Monopolis.COLOURS[5][1], Monopolis.COLOURS[5][2]))
            text_view_player_colour6.text = "■"
            text_view_player_name6.text = "${monopolis.players[5].name}"
            text_view_player_money6.text = "₩${monopolis.players[5].balance}"
            text_view_player_jailcards6.text = "${monopolis.players[5].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 7) {
            text_view_player_colour7.setTextColor(Color.argb(255, Monopolis.COLOURS[6][0], Monopolis.COLOURS[6][1], Monopolis.COLOURS[6][2]))
            text_view_player_colour7.text = "■"
            text_view_player_name7.text = "${monopolis.players[6].name}"
            text_view_player_money7.text = "₩${monopolis.players[6].balance}"
            text_view_player_jailcards7.text = "${monopolis.players[6].jailCards.count()} Jail cards"
        }
        if (monopolis.players.count() >= 8) {
            text_view_player_colour8.setTextColor(Color.argb(255, Monopolis.COLOURS[7][0], Monopolis.COLOURS[7][1], Monopolis.COLOURS[7][2]))
            text_view_player_colour8.text = "■"
            text_view_player_name8.text = "${monopolis.players[7].name}"
            text_view_player_money8.text = "₩${monopolis.players[7].balance}"
            text_view_player_jailcards8.text = "${monopolis.players[7].jailCards.count()} Jail cards"
        }
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
