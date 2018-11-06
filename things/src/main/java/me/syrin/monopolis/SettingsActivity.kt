package me.syrin.monopolis

import android.os.Bundle
import android.preference.PreferenceManager
import android.app.AlertDialog
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.set_name_dialog.view.*
import me.syrin.monopolis.common.network.Monopolis
import org.jetbrains.anko.toast

class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set click event for button (dialog)
        button_change_name.setOnClickListener {
            displaySetNameDialog()
        }

        // Pull name from sharedprefs and populate field
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("player_name", null)
        if (name != null) {
            text_view_name.text = name
        }
        else {
            displaySetNameDialog()
            Monopolis.init(preferences.getString("player_name", null) as String)
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

    private fun setName(name: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferences.edit().putString("player_name", name).apply()
        text_view_name.text = name
    }

    override fun onBackPressed() {
        // Check if name set, if not, dont allow leave
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("player_name", null)
        if (name == null) {
            toast("Please set a player name")
            return
        }

        super.onBackPressed()
    }
}
