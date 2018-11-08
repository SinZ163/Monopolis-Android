package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

abstract class Property(id: String, name: String, val propertySet: PropertySet, val price: Int) : Tile(id, name) {
    var owner: Player? = null
    var mortgaged: Boolean = false

    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // if mortgaged, do nothing
        if (mortgaged) return true

        // If property owned by another player, pay player
        if (owner != null) {
            chargePlayer(game, player)
        }

        return true
    }

    abstract fun chargePlayer(game: Monopolis, player: Player)

    open fun canMortgage(game: Monopolis): Boolean = true

    fun mortgage(game: Monopolis) {
        // Change mortgage status and credit player
        if (mortgaged) return
        if (!canMortgage(game)) return

        mortgaged = true
        owner?.credit(price / 2)
    }
    fun unmortgate(game: Monopolis) {
        if (owner == null) return

        val cost = 0.6 * price
        if (owner!!.balance > cost) return
        owner!!.pay(cost.toInt(), null)
        mortgaged = false
    }

    fun getPropertySet(game: Monopolis): List<Property> = game.tiles.filter {if (it is Property) it.propertySet == propertySet else false } as List<Property>
}