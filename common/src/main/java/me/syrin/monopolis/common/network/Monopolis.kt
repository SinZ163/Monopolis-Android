package me.syrin.monopolis.common.network

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import me.syrin.monopolis.common.ChatMessage
import me.syrin.monopolis.common.LobbyState

class Monopolis {
    companion object {
        private val ws = WebSocket()
        val lobbies: MutableLiveData<Map<Int, Lobby>> = MutableLiveData()

        val lobby: MutableLiveData<LobbyState?> = MutableLiveData()

        val chatMessages: MutableLiveData<List<ChatMessage>> = MutableLiveData()

        init {
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
        }
    }
}