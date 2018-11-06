package me.syrin.monopolis.common.game

import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.tiles.Property
import me.syrin.monopolis.common.game.tiles.PropertySet

class Player(val game: Monopolis) {
    var balance: Int = 1500 // Start with $1500
    var location: Int = 0   // Start at Go
    var jailed: Boolean = false
    var remainingJailRolls: Int = 0 // Not used until player sent to jail
    var jailCards = arrayListOf<Card>()

    fun pay(amount: Int, targetPlayer: Player?) {
        // Deduct from player balance
        balance -= amount

        // Add to target player balance
        if (targetPlayer != null) {
            targetPlayer.balance += amount
        }

        // Handle backrupting if negative balance
        if (balance < 0) {
            // TODO: this
        }
    }

    fun credit(amount: Int) {
        // Add to player balance
        balance += amount
    }

    fun sendToJail() {
        // Send to jail and set remaining jail rolls to 3
        location = game.tiles.indexOfFirst { tile -> tile.id == "Jail" }
        jailed = true
        remainingJailRolls = 3
    }

    fun freeFromJail() {
        jailed = false
    }

    fun payBail() {
        pay(50, null)
        freeFromJail()
    }

    fun advanceTo(targetId: String) {
        location = game.tiles.indexOfFirst { tile -> tile.id == targetId }
        // TODO: receive 200?
    }

    fun advanceToNearest(propertySet: PropertySet) {
        location = game.tiles.indexOfFirst { tile -> (tile as Property).propertySet == propertySet }
        // TODO: receive 200?
    }
}