package me.syrin.monopolis.common

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lobby_game.view.*
import me.syrin.monopolis.common.network.JoinLobbyPacket
import me.syrin.monopolis.common.network.Lobby
import me.syrin.monopolis.common.network.WebSocket

class LobbyGamesAdapter :
        RecyclerView.Adapter<LobbyGamesAdapter.LobbyGameViewHolder>() {

    private val myDataset: ArrayList<Lobby> = ArrayList()

    fun update(data: Collection<Lobby>) {
        myDataset.clear()
        myDataset.addAll(data)
        // TODO: Change this to DiffUtil stuff
        notifyDataSetChanged()
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class LobbyGameViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): LobbyGameViewHolder {
        // create a new view
        val lobbyGameView = LayoutInflater.from(parent.context)
                .inflate(R.layout.lobby_game, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters
        // TODO: ?

        return LobbyGameViewHolder(lobbyGameView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: LobbyGameViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.linearLayout.game_name.text = myDataset[position].lobbyName
        holder.linearLayout.player_count.text = "${myDataset[position].playerCount} / ${myDataset[position].maxCount} players"
        holder.linearLayout.join_game.setOnClickListener {
            Log.i("LobbyGamesAdapter", "Pressed Join Game for ${myDataset[position]}")
            WebSocket.send(JoinLobbyPacket(myDataset[position].lobbyID))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}