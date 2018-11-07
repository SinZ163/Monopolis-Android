package me.syrin.monopolis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.game.Monopolis

class GameActivity : AppCompatActivity() {
    lateinit var monopolis: Monopolis
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        monopolis = Monopolis(this, listOf("Sam", "Trent"))

        (fragment2 as BoardFragment).initialiseBoard(monopolis)

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
}
