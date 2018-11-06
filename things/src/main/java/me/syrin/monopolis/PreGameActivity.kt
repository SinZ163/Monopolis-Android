package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pre_game.*
import me.syrin.monopolis.common.LobbyState
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.Monopolis
import me.syrin.monopolis.common.network.StartLobbyPacket
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.startActivity

class PreGameActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = PlayerListAdapter()

        Monopolis.lobby.observe(this, Observer {
            if (it == null) {
                // lobby doesnt exist anymore, back out
                onBackPressed()
            }
            else {
                if (it.ingame) {
                    // game as been started, start game activity
                    startActivity<GameActivity>()
                }
                else {
                    // something about the lobby has changed, update title and playerlist
                    viewAdapter.update(it.players)
                    game_name.text = it.name
                    if (it.host == "Things") {
                        button_start_game.isEnabled = true
                    }
                }
            }
        })

        recycler_view_players.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        button_start_game.setOnClickListener {
            WebSocket.send(StartLobbyPacket())
        }
    }

    override fun onBackPressed() {
        if (Monopolis.lobby.value != null) {
            val lobby = Monopolis.lobby.value ?: return super.onBackPressed()
            WebSocket.send(LeaveLobbyPacket(lobby.id))
        }
        super.onBackPressed()
    }
}
