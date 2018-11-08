package me.syrin.monopolis.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_game_buttons.*
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.NetworkHandler

class GameButtonsFragment : Fragment() {

    lateinit var game: Monopolis

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_roll_dice_end_turn.setOnClickListener {
            when (game.turnState) {
                Monopolis.TurnState.RollDice -> game.rollDicePressed()
                Monopolis.TurnState.EndTurn -> game.endTurnPressed()
            }
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

    fun uiUpdate() {
        if (game.players[game.currentPlayer].name == NetworkHandler.name) {
            // if we are the player, display out buttons
            when (game.turnState) {
                Monopolis.TurnState.RollDice -> if (game.players[game.currentPlayer].jailed) displayJailTurn() else displayNormalTurn()
                Monopolis.TurnState.EndTurn -> displayEndTurn()
            }
        }
        else {
            // disable buttons
            disableButtons()
        }
    }

    fun displayNormalTurn() {
        // show roll dice
        button_property_management.isEnabled = true
        button_roll_dice_end_turn.isEnabled = true
        button_roll_dice_end_turn.text = "Roll dice"
//        button_trade.isEnabled = true

        // hide jail
        button_pay_bail.visibility = View.GONE
        button_use_jail_card.visibility = View.GONE
    }

    fun displayJailTurn() {
        // show roll dice
        if (game.players[game.currentPlayer].jailCards.count() > 0) {
            button_use_jail_card.isEnabled = true
        }
        if (game.players[game.currentPlayer].remainingJailRolls > 0) {
            button_roll_dice_end_turn.isEnabled = true
        }
        button_pay_bail.isEnabled = true
        button_property_management.isEnabled = true
//        button_trade.isEnabled = true
        button_roll_dice_end_turn.text = "Roll dice"
        // TODO: remaining rolls check

        // show jail
        button_pay_bail.visibility = View.VISIBLE
        button_use_jail_card.visibility = View.VISIBLE
    }

    fun displayEndTurn() {
        button_roll_dice_end_turn.isEnabled = true
        button_roll_dice_end_turn.text = "End turn"
    }

    fun disableButtons() {
        button_roll_dice_end_turn.isEnabled = false
        button_use_jail_card.isEnabled = false
        button_pay_bail.isEnabled = false
        button_trade.isEnabled = false
        button_property_management.isEnabled = false
    }
}
