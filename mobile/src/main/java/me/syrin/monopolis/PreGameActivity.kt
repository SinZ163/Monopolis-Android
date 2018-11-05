package me.syrin.monopolis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.Monopolis
import me.syrin.monopolis.common.network.WebSocket

class PreGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)
    }
    override fun onBackPressed() {
        if (Monopolis.lobby.value != null) {
            val lobby = Monopolis.lobby.value ?: return super.onBackPressed()
            WebSocket.send(LeaveLobbyPacket(lobby.id))
        }
        super.onBackPressed()
    }
}
