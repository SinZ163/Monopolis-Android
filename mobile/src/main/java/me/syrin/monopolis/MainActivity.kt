package me.syrin.monopolis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import me.syrin.monopolis.common.network.Monopolis
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var keepplz = Monopolis()
        Monopolis.lobby.observe(this, Observer {
            if (it?.ingame == true) {
                startActivity<GameActivity>()
            } else {
                startActivity<PreGameActivity>()
            }
        })
        Log.d("MainActivity", "Connecting to ${keepplz}")

        button_join_game.setOnClickListener {
            startActivity<LobbyActivity>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
