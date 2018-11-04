package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_pre_game.*
import org.jetbrains.anko.startActivity

class PreGameActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        val name = intent.extras?.get("game_name") as String
        val playerCount = intent.extras?.get("player_count") as Int

        if (name != "") {
            // game is being created
            game_name.text = name
        }
        else {
            // game is being joined

        }

        button_start_game.setOnClickListener {
            startActivity<GameActivity>()
        }
    }
}
