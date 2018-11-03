package me.syrin.monopolis.common


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_test.*
import me.syrin.monopolis.common.network.ChatPacket
import me.syrin.monopolis.common.network.EventBus
import me.syrin.monopolis.common.network.WebSocket

class TestFragment : Fragment() {
    lateinit var ws: WebSocket
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send.setOnClickListener {
            WebSocket.send(ChatPacket("I'm making a note here, huge success"))
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        EventBus.subscribe<ChatPacket>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    dump.text = dump.text.toString() + "\n" +it.msg
                    Log.i("TestFragment", "Did you see ${it.msg}")
                }
    }


}
