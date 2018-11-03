package me.syrin.monopolis.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_lobby.*
import me.syrin.monopolis.common.network.Lobby
import me.syrin.monopolis.common.network.Monopolis

class LobbyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val games: ArrayList<LobbyGame> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        games.add(LobbyGame("Name's game", 2))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))

        viewManager = LinearLayoutManager(context)
        val viewAdapter = LobbyGamesAdapter()
        Monopolis.lobbies.observe(this, Observer<Map<Int, Lobby>> {
            viewAdapter.update(it.values)
        })

        recyclerView = recycler_view_games.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
