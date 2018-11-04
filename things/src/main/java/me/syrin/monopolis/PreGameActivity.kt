package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_pre_game.*
import me.syrin.monopolis.common.network.Monopolis
import org.jetbrains.anko.startActivity

class PreGameActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        game_name.text = Monopolis.lobby.value?.name
        val playerCount = intent.extras?.get("player_count") as Int

        button_start_game.setOnClickListener {
            startActivity<GameActivity>()
        }
    }
}
