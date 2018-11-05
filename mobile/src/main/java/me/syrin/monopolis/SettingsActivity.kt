package me.syrin.monopolis

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("playerName", null)
        if (name != null) {
            nameField.setText(name)
        }
    }

    override fun onBackPressed() {
        if (nameField.text.toString() == "") {
            toast("Please enter a name")
            return
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("playerName", nameField.text.toString())
                .apply()
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}
