package me.syrin.monopolis.common.game

import me.syrin.monopolis.common.game.tiles.*
import kotlin.random.Random

class Monopolis {
    val tiles = listOf<Tile>()  // TODO: generates tiles from csv asset

    val players = arrayListOf<Player>()
    var currentPlayer = 0

    var diceOneAmount: Int = 0  // Set each turn
    var diceTwoAmount: Int = 0  // Set each turn
    fun diceTotal(): Int = diceOneAmount + diceTwoAmount
    fun rollValue(): Int {
        return if (diceOneAmount == diceTwoAmount)
            diceTotal() * 2
        else
            diceTotal()
    }

    fun doTurn() {
        // Next player
        currentPlayer = ((currentPlayer + 1) % players.count())
        var player = players[currentPlayer]

        // Check for jail
        if (tiles[player.location].name == "Jail" && player.jailed) {
            // Check if player can jail roll
            if (player.remainingJailRolls > 0) {
                rollDice()
                if (diceOneAmount == diceTwoAmount) {
                    // Free from jail
                    player.freeFromJail()
                }
            }
            else {
                // Pay to unjail
                player.payBail()
            }
        }
        else {
            // Normal turn
            rollDice()
            player.location = ((player.location + rollValue()) % tiles.count())
            // Pass go?
            if (rollValue() > player.location) {
                player.credit(200)
            }
            tiles[player.location].onPlayerLand(this, player)
        }
    }

    private fun rollDice() {
        // Roll dice
        diceOneAmount = Random.nextInt(1,7)
        diceTwoAmount = Random.nextInt(1,7)
    }
}