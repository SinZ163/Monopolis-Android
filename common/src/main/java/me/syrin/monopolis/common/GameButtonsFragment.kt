package me.syrin.monopolis.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_game_buttons.*
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.network.NetworkHandler
import me.syrin.monopolis.common.network.PayBailPacket
import me.syrin.monopolis.common.network.UseJailCardPacket

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
            game.players[game.currentPlayer].payBail()
            game.send(PayBailPacket(game.players[game.currentPlayer].name))
        }
        button_use_jail_card.setOnClickListener {
            game.players[game.currentPlayer].useJailCard()
            game.send(UseJailCardPacket(game.players[game.currentPlayer].name))
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
                Monopolis.TurnState.BailRequired -> displayJailTurn(true)
            }
        }
        else {
            // disable buttons
            disableButtons()
        }
    }

    fun displayNormalTurn() {
        button_roll_dice_end_turn.text = "Roll dice"

        // show roll dice
        button_roll_dice_end_turn.isEnabled = true

        // property management and trading always enabled
        button_property_management.isEnabled = true
//        button_trade.isEnabled = true     // TODO: trading

        // hide jail
        button_pay_bail.visibility = View.GONE
        button_use_jail_card.visibility = View.GONE
    }

    fun displayJailTurn(bailRequired: Boolean = false) {
        button_roll_dice_end_turn.text = "Roll dice"

        // check if jailcards or
        if (game.players[game.currentPlayer].jailCards.count() > 0) {
            button_use_jail_card.isEnabled = true
        }

        // if bail isn't required this turn
        if (!bailRequired) {
            // show roll dice
            button_roll_dice_end_turn.isEnabled = true
        }

        button_pay_bail.isEnabled = true

        // property management and trading always enabled
        button_property_management.isEnabled = true
//        button_trade.isEnabled = true     // TODO: trading


        // show jail
        button_pay_bail.visibility = View.VISIBLE
        button_use_jail_card.visibility = View.VISIBLE
    }

    fun displayEndTurn() {
        button_roll_dice_end_turn.text = "End turn"
        button_roll_dice_end_turn.isEnabled = true
    }

    fun disableButtons() {
        button_roll_dice_end_turn.isEnabled = false
        button_use_jail_card.isEnabled = false
        button_pay_bail.isEnabled = false
        button_trade.isEnabled = false
        button_property_management.isEnabled = false
    }
}
