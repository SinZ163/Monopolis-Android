package me.syrin.monopolis.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_game_buttons.*
import me.syrin.monopolis.common.game.Monopolis

class GameButtonsFragment : Fragment() {

    lateinit var game: Monopolis

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_roll_dice.setOnClickListener {
            // TODO: roll dice
        }
        button_end_turn.setOnClickListener {
            // TODO: end turn
        }
        button_pay_bail.setOnClickListener {
            // TODO: pay bail
        }
        button_use_jail_card.setOnClickListener {
            // TODO: jail card
        }
        button_trade.setOnClickListener {
            // TODO: open trade dialog
        }
        button_property_management.setOnClickListener {
            val dialog = PropertyManagementDialogFragment()
            dialog.game = game
            dialog.show(activity?.supportFragmentManager, "property_management")
        }
    }

    fun displayNormalTurn() {
        // show roll dice
        button_roll_dice.visibility = View.VISIBLE
        button_end_turn.visibility = View.GONE

        // hide jail
        button_pay_bail.visibility = View.GONE
        button_use_jail_card.visibility = View.GONE
    }

    fun displayJailTurn() {
        // show roll dice
        button_roll_dice.visibility = View.VISIBLE
        button_end_turn.visibility = View.GONE
        // TODO: remaining rolls check

        // show jail
        button_pay_bail.visibility = View.VISIBLE
        button_use_jail_card.visibility = View.VISIBLE
    }

    fun displayEndTurn() {
        button_roll_dice.visibility = View.GONE
        button_end_turn.visibility = View.VISIBLE
    }
}
