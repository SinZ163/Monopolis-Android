package me.syrin.monopolis.common.game

import android.util.Log
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.tiles.Property
import me.syrin.monopolis.common.game.tiles.PropertySet
import me.syrin.monopolis.common.network.*

class Player(val game: Monopolis, val name: String) {
    var balance: Int = 1500 // Start with $1500
    var location: Int = 0   // Start at Go
    var jailed: Boolean = false
    var remainingJailRolls: Int = 0 // Not used until player sent to jail
    var jailCards = arrayListOf<Card>()

    fun pay(amount: Int, targetPlayer: Player?, isNetworked: Boolean = false) {
        if (isNetworked) {
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
            game.updateUI()
        } else {
            Log.d("Monopolis:send_d", "Being told to charge ${targetPlayer?.name} $amount")
            Log.d("Monopolis:send_d", Log.getStackTraceString(Exception()))
            game.send(PayPersonPacket(name, targetPlayer?.name, amount))
        }
    }

    fun credit(amount: Int, isNetworked: Boolean = false) {
        if (isNetworked) {
            // Add to player balance
            balance += amount
            game.updateUI()
        } else {
            game.send(GainMoneyPacket(name, amount))
        }
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

    fun advanceTo(targetIndex: Int) {
        if (targetIndex < location) {
            // player passed go
            credit(200)
        }
        location = targetIndex
        if (game.tiles[location].onPlayerLand(game, this)) {
            game.endTurn()
        }

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

    fun moveForward(numSpaces: Int) {
        advanceTo((location + numSpaces) % game.tiles.count())
    }
    fun moveBackward(numSpaces: Int) {
        location -= numSpaces
        if (location < 0) {
            // for example -1 (mayfair), which is 39. 40 + -1 = 39
            location += 40
        }
        advanceTo(location)
    }
}