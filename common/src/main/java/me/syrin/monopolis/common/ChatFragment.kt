package me.syrin.monopolis.common


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_chat.*
import me.syrin.monopolis.common.network.ChatPacket
import me.syrin.monopolis.common.network.EventBus
import me.syrin.monopolis.common.network.Monopolis
import me.syrin.monopolis.common.network.WebSocket

class ChatFragment : Fragment() {
    private lateinit var viewAdapter: ChatMessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var ws: WebSocket

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_text_message.setOnEditorActionListener {v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND, EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    sendMessage()
                    true
                }
                else -> false
            }
        }

        viewManager = LinearLayoutManager(context)
        viewAdapter = ChatMessageAdapter()

        recycler_view_chat.apply {
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        button_send.setOnClickListener {
            sendMessage()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Monopolis.chatMessages.observe(this, Observer<List<ChatMessage>> {
            replaceMessages(it)
        })
    }

    fun sendMessage() {
        val text = edit_text_message.text.toString()
        if (text == "") {
            return
        }
        edit_text_message.text.clear()
        WebSocket.send(ChatPacket(text))
    }

    fun replaceMessages(messages: List<ChatMessage>) {
        viewAdapter.update(messages)
        recycler_view_chat.scrollToPosition(viewAdapter.itemCount - 1)
    }
}
