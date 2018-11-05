package me.syrin.monopolis

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_game.*
import me.syrin.monopolis.common.GenericMessageDialogFragment
import me.syrin.monopolis.common.game.Monopolis

class GameActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        button_test.setOnClickListener {
            displayGenericMessageDialog("test", "testererereerererewrer")
        }

        Monopolis(this)
    }

    private fun displayGenericMessageDialog(title: String, description: String) {
        val dialog = GenericMessageDialogFragment()
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("description", description)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "test")
    }
}
