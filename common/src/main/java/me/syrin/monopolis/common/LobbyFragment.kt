package me.syrin.monopolis.common

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lobby.*
import me.syrin.monopolis.common.network.Lobby
import me.syrin.monopolis.common.network.NetworkHandler

class LobbyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewManager = LinearLayoutManager(context)
        val viewAdapter = LobbyGamesAdapter()
        NetworkHandler.lobbies.observe(this, Observer<Map<Int, Lobby>> {
            Log.i("Debug", "What is $viewAdapter what is $it")
            if (it != null) {
                viewAdapter.update(it.values)
            } else {
                Log.e("Debug", "How is this null")
            }
        })

        recycler_view_games.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
