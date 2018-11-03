package me.syrin.monopolis.common.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers

class Monopolis {
    companion object {
        private val ws = WebSocket()
        val lobbies: MutableLiveData<Map<Int, Lobby>> = MutableLiveData()
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
        }
    }
}