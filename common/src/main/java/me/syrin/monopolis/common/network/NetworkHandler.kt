package me.syrin.monopolis.common.network

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import me.syrin.monopolis.common.ChatMessage
import me.syrin.monopolis.common.LobbyState

class NetworkHandler {
    companion object {
        val lobbies: MutableLiveData<Map<Int, Lobby>> = MutableLiveData()

        val lobby: MutableLiveData<LobbyState?> = MutableLiveData()

        val chatMessages: MutableLiveData<List<ChatMessage>> = MutableLiveData()

        val connected: MutableLiveData<Boolean> = MutableLiveData()

        val packets: MutableLiveData<List<GamePacket>> = MutableLiveData()

        lateinit var name: String

        var playback: Boolean = false
        var playbackPackets: ArrayList<GamePacket> = arrayListOf()

        @SuppressLint("CheckResult")
        fun init(newName: String) {
            // Above everything else to buzzer beat  WebSocket.init
            EventBus.subscribe<ConnectionLost>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.i("WebSocket", "Disconnected :(")
                        Handler().postDelayed({
                            me.syrin.monopolis.common.network.WebSocket.init(name)
                        }, 5000)
                        //nuke EVERYTHING when this happens
                        lobby.value = null
                        lobbies.value = mapOf()
                        chatMessages.value = listOf()
                        packets.value = listOf()

                        connected.value = false
                    }
            EventBus.subscribe<ConnectionGained>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        connected.value = true
                    }
            connected.value = false
            WebSocket.init(newName)
            name = newName
            // TODO: change this to listen to a (yet to be created) "connected packet" or listen to the open connection
            EventBus.subscribe<IPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (!connected.value!!) {
                            connected.value = true
                        }
                    }
            EventBus.subscribe<LobbyListPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        lobbies.value = emptyMap()
                        for (lobby in it.lobbies) {
                            lobbies.value = lobbies.value?.plus(Pair(lobby.lobbyID, lobby))
                        }
                    }
            EventBus.subscribe<Lobby>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        lobbies.value = lobbies.value?.plus(Pair(it.lobbyID, it))
                    }
            EventBus.subscribe<LobbyClosedPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        lobbies.value = lobbies.value?.minus(it.lobbyID)
                        if (it.lobbyID == lobby.value?.id) {
                            lobby.value = null
                            chatMessages.value = listOf()
                            packets.value = listOf()
                        }
                    }
            EventBus.subscribe<LobbyInfoPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        lobby.value = LobbyState(it.id, it.name, it.ingame, it.host, ArrayList(it.players))
                    }
            EventBus.subscribe<ChatPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (chatMessages.value == null) {
                            chatMessages.value = listOf()
                        }
                        chatMessages.value = chatMessages.value?.plus(ChatMessage(it.message, it.author))
                    }
            EventBus.subscribe<PlaybackStartPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        playback = true
                    }
            EventBus.subscribe<PlaybackEndPacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        playback = false
                        packets.value = packets.value?.plus(playbackPackets)
                        playbackPackets = arrayListOf()
                    }
            EventBus.subscribe<GamePacket>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (packets.value == null) {
                            packets.value = listOf()
                        }
                        if (!playback)
                            packets.value = packets.value?.plus(it)
                        else
                            playbackPackets.add(it)
                    }
        }
    }
}