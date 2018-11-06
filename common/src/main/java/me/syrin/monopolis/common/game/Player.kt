package me.syrin.monopolis.common.game

import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.tiles.Property
import me.syrin.monopolis.common.game.tiles.PropertySet

class Player(val game: Monopolis, val name: String) {
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
            // TODO: give player option to sell things or for now they just lose
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

    fun moveForward(numSpaces: Int) {
        location = ((location + numSpaces) % game.tiles.count())
        game.tiles[location].onPlayerLand(game, this)
    }

    fun advanceTo(targetIndex: Int) {
        if (targetIndex < location) {
            // player passed go
            credit(200)
        }
        location = targetIndex
    }
    fun advanceTo(targetId: String) {
        advanceTo(game.tiles.indexOfFirst { tile -> tile.id == targetId })
    }

    fun advanceToNearest(propertySet: PropertySet) {
        var newLocation: Int
        for (i in 0 until game.tiles.count()) {
            // loops after i+location = 39 (last tile)
            newLocation = (location + i) % 40

            if ((game.tiles[newLocation] as? Property)?.propertySet == propertySet) {
                advanceTo(newLocation)
                return
            }
        }
    }
}