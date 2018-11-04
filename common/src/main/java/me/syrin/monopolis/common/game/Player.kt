package me.syrin.monopolis.common.game

class Player(val game: Monopolis) {
    var balance: Int = 1500 // Start with $1500
    var location: Int = 0   // Start at Go
    var jailed: Boolean = false
    var remainingJailRolls: Int = 0 // Not used until player sent to jail

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
        location = game.tiles.indexOfFirst { tile -> tile.name == "Jail" }
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
}