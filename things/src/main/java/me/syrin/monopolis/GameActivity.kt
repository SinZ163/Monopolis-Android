package me.syrin.monopolis

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.GenericMessageDialogFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.WebSocket

class GameActivity : FragmentActivity() {
    lateinit var monopolis: Monopolis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        monopolis = Monopolis(this, listOf("Sam", "Trent"))

        (fragment_board as BoardFragment).initialiseBoard(monopolis)

        text_view_temp.text = "${monopolis.players[0].name}: ${tempGetLocName(monopolis.players[0].location)}\n${monopolis.players[1].name}: ${tempGetLocName(monopolis.players[1].location)}"

        button_test.setOnClickListener {
            // do turn
            monopolis.doTurn()
            // update player status
            text_view_temp.text = "${monopolis.players[0].name}: ${tempGetLocName(monopolis.players[0].location)}\n${monopolis.players[1].name}: ${tempGetLocName(monopolis.players[1].location)}\nRoll: ${monopolis.diceTotal()}"
        }
    }

    private fun tempGetLocName(i: Int): String {
        return monopolis.tiles[i].name
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Leave?")
        builder.setMessage("Are you sure you want to leave this game?")

        builder.setPositiveButton("Leave") { _, _ ->
            // leave game
            WebSocket.send(LeaveLobbyPacket(me.syrin.monopolis.common.network.Monopolis.lobby.value!!.id))
            super.onBackPressed()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            // cancel
            dialog.cancel()
        }

        builder.show()
    }
}
