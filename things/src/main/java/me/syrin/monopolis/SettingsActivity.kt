package me.syrin.monopolis

import android.os.Bundle
import android.preference.PreferenceManager
import android.app.AlertDialog
import android.content.SharedPreferences
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.set_name_dialog.view.*
import me.syrin.monopolis.common.BoardFragment
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.NetworkHandler
import org.jetbrains.anko.toast

class SettingsActivity : FragmentActivity() {

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Set click event for change name button (dialog)
        button_change_name.setOnClickListener {
            displaySetNameDialog()
        }

        // Set click event for uk and us buttons
        button_uk_board.setOnClickListener {
            setBoard("uk")
        }
        button_us_board.setOnClickListener {
            setBoard("us")
        }


        // Pull name and board from sharedprefs and populate fields
        val name = preferences.getString("playerName", null)
        val board = preferences.getString("playerBoard", "uk")

        // Initialise board
        setBoard(board)
        displayBoard()

        if (name != null) {
            displayName(name)
        }
        else {
            displaySetNameDialog()
        }
    }

    private fun displaySetNameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter a player name")

        val inputView = layoutInflater.inflate(R.layout.set_name_dialog, null)

        builder.setView(inputView)

        builder.setPositiveButton("Confirm") { dialog, which ->
            val inputName = inputView.edit_text_name.text.toString()
            setName(inputName)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // cancel
            dialog.cancel()
        }

        val dialog = builder.create()

        inputView.edit_text_name.setOnEditorActionListener {v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE, EditorInfo.IME_ACTION_UNSPECIFIED -> {
                    setName(v.text.toString())
                    dialog.dismiss()
                    true
                }
                else -> false
            }
        }

        dialog.show()
    }

    private fun setBoard(boardId: String) {
        preferences.edit().putString("playerBoard", boardId).apply()
        displayBoard()
    }

    private fun displayBoard() {
        (fragment_board_preview as BoardFragment).initialiseBoard(Monopolis(this))
    }

    private fun setName(name: String) {
        preferences.edit().putString("playerName", name).apply()
        displayName(name)
        toast("Name will be set to $name when device restarts")
        if (NetworkHandler.connected.value != true) {
            NetworkHandler.init(preferences.getString("playerName", null) as String)
        }
    }

    private fun displayName(name: String) {
        text_view_name.text = name
    }

    override fun onBackPressed() {
        // Check if name set, if not, dont allow leave
        val name = preferences.getString("playerName", null)
        if (name == null) {
            toast("Please set a player name")
            return
        }

        super.onBackPressed()
    }
}
