package me.syrin.monopolis.common


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.fragment_player_list.*

import me.syrin.monopolis.common.network.Monopolis

class PlayerListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PlayerListAdapter()
        recycler_view_players.adapter = adapter
        recycler_view_players.layoutManager = LinearLayoutManager(view.context)

        Monopolis.lobby.observe(this, Observer<LobbyState?> {
            if (it != null) {
                adapter.update(it.players)
            }
        })
    }

    internal inner class PlayerListAdapter : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {

        private val players = ArrayList<String>()

        fun update(players: ArrayList<String>) {
            this.players.clear()
            this.players.addAll(players)
            notifyDataSetChanged()
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        inner class PlayerViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): PlayerListAdapter.PlayerViewHolder {
            // create a new view
            val textView = TextView(parent.context)
            // set the view's size, margins, paddings and layout parameters
            textView.textSize = 30f

            return PlayerViewHolder(textView)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.textView.text = players[position]
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = players.size
    }
}
