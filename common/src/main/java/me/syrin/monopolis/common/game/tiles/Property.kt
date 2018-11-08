package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

abstract class Property(id: String, name: String, val propertySet: PropertySet, val price: Int) : Tile(id, name) {
    var owner: Player? = null
    var mortgaged: Boolean = false

    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // If property owned by another player, pay player
        if (owner != null) {
            chargePlayer(game, player)
        }

        // If property not owned, offer to buy
        // TODO: this
        return true
    }

    abstract fun chargePlayer(game: Monopolis, player: Player)

    fun mortgage() {
        // Change mortgage status and credit player
        if (mortgaged) return

        mortgaged = true
        owner?.credit(price / 2)
    }
}