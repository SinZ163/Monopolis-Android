package me.syrin.monopolis

import android.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_game_dialog.view.*
import me.syrin.monopolis.common.LobbyState
import me.syrin.monopolis.common.network.CreateLobbyPacket
import me.syrin.monopolis.common.network.Monopolis
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.startActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO: SharedPreferences
        Monopolis.init("Things")
        Monopolis.lobby.observe(this, Observer<LobbyState?> {
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
            inputView.game_name.inputType = InputType.TYPE_CLASS_TEXT

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Game setup")
            builder.setView(inputView)

            builder.setPositiveButton("Create") { dialog, which ->
                // open pregame with passed name and player count
                val name = inputView.game_name.text.toString()
                val playerCount = inputView.player_count.value
//                startActivity<PreGameActivity>("game_name" to name, "player_count" to playerCount)
                WebSocket.send(CreateLobbyPacket(name, playerCount))
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                // cancel
                dialog.cancel()
            }

            builder.show()
        }

        button_join_game.setOnClickListener {
            // open lobby screen
            startActivity<LobbyActivity>()
        }
    }
}
