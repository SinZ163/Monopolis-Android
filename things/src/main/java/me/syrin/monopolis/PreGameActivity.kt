package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pre_game.*
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.NetworkHandler
import me.syrin.monopolis.common.network.StartLobbyPacket
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.startActivity

class PreGameActivity : FragmentActivity() {
    lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter: PlayerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PlayerListAdapter()

        recycler_view_players.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        button_start_game.setOnClickListener {
            WebSocket.send(StartLobbyPacket())
        }
    }

    override fun onResume() {
        super.onResume()

        NetworkHandler.lobby.observe(this, Observer {
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
                    if (it.host == NetworkHandler.name) {
                        button_start_game.isEnabled = true
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if (NetworkHandler.lobby.value != null) {
            val lobby = NetworkHandler.lobby.value ?: return super.onBackPressed()
            WebSocket.send(LeaveLobbyPacket(lobby.id))
        }

        super.onBackPressed()
    }
}
