package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pre_game.*
import me.syrin.monopolis.common.LobbyState
import me.syrin.monopolis.common.network.Monopolis
import org.jetbrains.anko.startActivity

class PreGameActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = PlayerListAdapter()

        Monopolis.lobby.observe(this, Observer<LobbyState?> {
            viewAdapter.update(it?.players as ArrayList<String>)
            game_name.text = it?.name
        })

        recycler_view_players.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        button_start_game.setOnClickListener {
            startActivity<GameActivity>()
        }
    }
}
