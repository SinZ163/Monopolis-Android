package me.syrin.monopolis.common.network

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.lang.Exception
import org.json.JSONTokener
import kotlinx.serialization.json.JSON

val URI = java.net.URI("ws://grimm.361zn.is:8000")
class WebSocket : WebSocketClient(URI) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WebSocket", "onOpen")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("WebSocket", "onClose: $reason")
    }

    override fun onMessage(message: String?) {
        Log.i("WebSocket", "onMessage: $message")

        // Due to needing to read part of the JSON to know what the inner payload is, I use a more basic JSON library to unwrap the data.
        // Once its unwrapped I can use kotlinx.serialization to then turn it back into data classes.
        val science = JSONTokener(message)
        val value = science.nextValue() as JSONObject
        val packetID = value.getInt("packetID")
        val rawData = value.getJSONObject("data").toString()

        val packet = when(packetID) {
            0 -> JSON.parse(ChatPacket.serializer(), rawData)
            255 -> JSON.parse(ErrorPacket.serializer(), rawData)
            else -> Log.e("WebSocket", "Unknown packet: $packetID")
        }
        Log.i("WebSocket", "$packetID, $packet")
        EventBus.post(packet)
    }

    override fun onError(ex: Exception?) {
        Log.e("WebSocket", "$ex")
    }
    companion object {
        val instance: WebSocket = WebSocket()

        init {
            instance.connect()
        }

        fun send(packet: IPacket) {
            val packetID = when(packet) {
                is ChatPacket -> 0
                is ErrorPacket -> 255
                else -> -1
            }
            if (packetID == -1) {
                throw NotImplementedError("Unknown packet: $packet")
            }
            val container = RawPacket(packetID, packet)
            val json = JSON.stringify(RawPacket.serializer(), container)
            instance.send(json)
        }
    }
}