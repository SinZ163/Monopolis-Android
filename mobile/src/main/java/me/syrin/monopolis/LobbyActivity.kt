package me.syrin.monopolis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import me.syrin.monopolis.common.LobbyState
import me.syrin.monopolis.common.network.NetworkHandler
import org.jetbrains.anko.startActivity

class LobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        NetworkHandler.lobby.observe(this, Observer<LobbyState?> {
            if (it?.ingame == true) {
                startActivity<GameActivity>()
            } else if (it != null) {
                startActivity<PreGameActivity>()
            }
        })
    }
}
