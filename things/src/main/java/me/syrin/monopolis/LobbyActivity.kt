package me.syrin.monopolis

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_lobby.*

/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class LobbyActivity : Activity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val games: ArrayList<LobbyGame> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        games.add(LobbyGame("Name's game", 2))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))
        games.add(LobbyGame("Test's game", 5))

        viewManager = LinearLayoutManager(this)
        viewAdapter = LobbyGamesAdapter(games)

        recyclerView = recycler_view_games.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
