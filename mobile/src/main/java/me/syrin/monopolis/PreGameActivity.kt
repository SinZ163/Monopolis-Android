package me.syrin.monopolis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_pre_game.*
import me.syrin.monopolis.common.ChatFragment
import me.syrin.monopolis.common.PlayerListFragment
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.Monopolis
import me.syrin.monopolis.common.network.StartLobbyPacket
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.startActivity

class PreGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_game)

        start.setOnClickListener {
            WebSocket.send(StartLobbyPacket())
        }

        Monopolis.lobby.observe(this, Observer {
            if (it == null) {
                onBackPressed()
            }
            else {
                if (it.ingame) {
                    startActivity<GameActivity>()
                } else {
                    // We are still PreGame
                    actionBar?.title = "Lobby: " + it.name
                    // If I am host, unlock the button
                    if (it.host == Monopolis.name) {
                        start.isEnabled = true
                    }
                }
            }
        })


        tabs.setupWithViewPager(pager)
        pager.adapter = PreGamePageAdapter(supportFragmentManager)

    }
    override fun onBackPressed() {
        if (Monopolis.lobby.value != null) {
            val lobby = Monopolis.lobby.value ?: return super.onBackPressed()
            WebSocket.send(LeaveLobbyPacket(lobby.id))
        }
        super.onBackPressed()
    }
    internal inner class PreGamePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> PlayerListFragment()
                1 -> ChatFragment()
                else -> Fragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when(position) {
                0 -> "Player List"
                1 -> "Chat"
                else -> "Wat"
            }
        }

        override fun getCount(): Int {
            return 2
        }

    }

}
