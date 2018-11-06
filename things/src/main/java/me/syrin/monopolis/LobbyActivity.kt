package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import me.syrin.monopolis.common.LobbyState
import me.syrin.monopolis.common.network.Monopolis
import org.jetbrains.anko.startActivity

class LobbyActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        Monopolis.lobby.observe(this, Observer {
            if (it?.ingame == true) {
                startActivity<GameActivity>()
            } else if (it != null) {
                startActivity<PreGameActivity>()
            }
        })
    }
}
