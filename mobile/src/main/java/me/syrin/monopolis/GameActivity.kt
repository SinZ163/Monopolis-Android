package me.syrin.monopolis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.ChatFragment
import me.syrin.monopolis.common.GameButtonsFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.LeaveLobbyPacket
import me.syrin.monopolis.common.network.NetworkHandler
import me.syrin.monopolis.common.network.WebSocket
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

private const val NUM_PAGES = 2

class GameActivity : AppCompatActivity() {
    val gameFragment = GameFragment()
    val chatFragment = ChatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        view_pager_game.adapter = pagerAdapter

        NetworkHandler.connected.observe(this, Observer {
            Log.i("GameActivity", "We are being told to evacuate?")
            if (!it) {
                startActivity(intentFor<MainActivity>().clearTop().singleTop())
            }
        })

        NetworkHandler.lobby.observe(this, Observer {
            if (it == null) {
                startActivity(intentFor<MainActivity>().clearTop())
            }
        })
    }

    override fun onBackPressed() {
        if (view_pager_game.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Leave?")
            builder.setMessage("Are you sure you want to leave this game?")

            builder.setPositiveButton("Leave") { _, _ ->
                // leave game
                WebSocket.send(LeaveLobbyPacket(NetworkHandler.lobby.value!!.id))
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                // cancel
                dialog.cancel()
            }

            builder.show()
        } else {
            // Otherwise, select the previous step.
            view_pager_game.currentItem = view_pager_game.currentItem - 1
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> gameFragment
            1 -> chatFragment
            else -> throw Exception("3 > 2 = no")
        }
    }
}
