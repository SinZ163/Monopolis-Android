package me.syrin.monopolis

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerListAdapter : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {

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
    class PlayerViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


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