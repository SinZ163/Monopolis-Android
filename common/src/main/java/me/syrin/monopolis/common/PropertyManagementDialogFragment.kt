package me.syrin.monopolis.common


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_property_management_layout.view.*
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.tiles.Property

class PropertyManagementDialogFragment : DialogFragment() {

    lateinit var game: Monopolis

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val myView = it.layoutInflater.inflate(R.layout.fragment_property_management_layout, null)
            val viewManager = LinearLayoutManager(activity)

            val viewAdapter = PropertyManagementAdapter(game, game.players[game.currentPlayer].properties.sortedBy { property -> property.propertySet })

            myView.recycler_view_properties.apply {
                layoutManager = viewManager
                adapter = viewAdapter
            }

            game.uiUpdates.observe(this, Observer {
                viewAdapter.update(game.players[game.currentPlayer].properties.sortedBy { property -> property.propertySet })
            })

            builder.setView(myView)

            val dialog = builder.create()

//            myView.button_continue.setOnClickListener {
//                dialog.dismiss()
//            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun passGame(game: Monopolis) {
        this.game = game
    }
}
