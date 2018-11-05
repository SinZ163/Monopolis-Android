package me.syrin.monopolis.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_message.view.*

class ChatMessageAdapter : RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder>() {

    private val messages = ArrayList<ChatMessage>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ChatMessageViewHolder(val chatMessageView: LinearLayout) : RecyclerView.ViewHolder(chatMessageView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ChatMessageAdapter.ChatMessageViewHolder {
        // create a new view
        val chatMessageView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_message, parent, false) as LinearLayout

        // set the view's size, margins, paddings and layout parameters
        // ...

        return ChatMessageViewHolder(chatMessageView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.chatMessageView.text_view_author.text = messages[position].author
        holder.chatMessageView.text_view_message.text = messages[position].message
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = messages.size

    fun update(messages: List<ChatMessage>) {
        this.messages.clear()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }
}