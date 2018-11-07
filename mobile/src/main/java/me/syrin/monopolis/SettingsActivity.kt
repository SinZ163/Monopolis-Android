package me.syrin.monopolis

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.toast

// TODO: file?
val boards = listOf("uk", "us")

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val name = preferences.getString("playerName", null)
        if (name != null) {
            nameField.setText(name)
        }

        val adapter = BoardAdapter(this)

        boardList.adapter = adapter
        boardList.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            toast("Position: $position")
            Log.i("SettingsActivity", "Pressed button: $position")
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString("playerBoard", boards[position])
                    .apply()
            adapter.update()
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

    internal inner class BoardAdapter(private val context: Context) : BaseAdapter() {
        fun update() {
            notifyDataSetChanged()
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = Button(context)
            view.text = boards[position]
            // Why android Why
            view.setOnClickListener {
                (parent as GridView).performItemClick(view, position, position.toLong())
            }
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val setBoard = preferences.getString("playerBoard", null)
            view.isEnabled = boards[position] != setBoard
            return view
        }

        override fun getItem(position: Int): Any = boards[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int = boards.size
    }
}
