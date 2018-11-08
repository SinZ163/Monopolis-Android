package me.syrin.monopolis.common.network

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.lang.Exception
import org.json.JSONTokener
import kotlinx.serialization.json.JSON

val URI = java.net.URI("ws://grimm.361zn.is:8000")

interface IConnectionStateChange
class ConnectionLost : IConnectionStateChange
data class ConnectionError(val error: Exception?) : IConnectionStateChange
class ConnectionGained : IConnectionStateChange

class WebSocket(var name: String) : WebSocketClient(URI) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WebSocket", "onOpen")
        EventBus.post(ConnectionGained())
        WebSocket.send(LoginPacket(name))
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("WebSocket", "onClose: $reason")
        EventBus.post(ConnectionLost())
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
            2 -> JSON.parse(LobbyListPacket.serializer(), rawData)
            3 -> JSON.parse(Lobby.serializer(), rawData)
            4 -> JSON.parse(LobbyClosedPacket.serializer(), rawData)
            8 -> JSON.parse(LobbyInfoPacket.serializer(), rawData)
            10 -> JSON.parse(TurnStartPacket.serializer(), rawData)
            11 -> JSON.parse(PlayerRollPacket.serializer(), rawData)
            12 -> JSON.parse(PayBailPacket.serializer(), rawData)
            13 -> JSON.parse(JailedPacket.serializer(), rawData)
            14 -> JSON.parse(PurchasePropertyPacket.serializer(), rawData)
            15 -> JSON.parse(PayPersonPacket.serializer(), rawData)
            16 -> JSON.parse(GainMoneyPacket.serializer(), rawData)
            17 -> JSON.parse(CardDrawPacket.serializer(), rawData)
            255 -> JSON.parse(ErrorPacket.serializer(), rawData)
            else -> Log.e("WebSocket", "Unknown packet: $packetID")
        }
        Log.i("WebSocket", "$packetID, $packet")
        EventBus.post(packet)
    }

    override fun onError(ex: Exception?) {
        Log.e("WebSocket", "${ex?.message}", ex)
        EventBus.post(ConnectionError(ex))
    }
    companion object {
        lateinit var instance: WebSocket

        fun init(name: String) {
            instance = WebSocket(name)
            instance.connect()
        }

        fun send(packet: IPacket) {
            val packetID = when(packet) {
                is ChatPacket -> 0
                is LoginPacket -> 1
                is LeaveLobbyPacket -> 5
                is CreateLobbyPacket -> 6
                is JoinLobbyPacket -> 7
                is StartLobbyPacket -> 9
                is TurnStartPacket -> 10
                is PlayerRollPacket -> 11
                is PayBailPacket -> 12
                is JailedPacket -> 13
                is PurchasePropertyPacket -> 14
                is PayPersonPacket -> 15
                is GainMoneyPacket -> 16
                is CardDrawPacket -> 17
                is ErrorPacket -> 255
                else -> -1
            }
            if (packetID == -1) {
                throw NotImplementedError("Unknown packet: $packet")
            }
            val container = RawPacket(packetID, packet)
            val json = JSON.stringify(RawPacket.serializer(), container)
            if (instance.isOpen)
                instance.send(json)
        }
    }
}