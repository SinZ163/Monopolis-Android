package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Railroad(id: String, name: String) : Property(id, name, PropertySet.Railroad, 200) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
        val set = getPropertySet(game).filter { owner == it.owner}

        val charge = when(set.size) {
            4 -> 200
            3 -> 100
            2 -> 50
            1 -> 25
            else -> 0
        }
        player.pay(charge, owner)
    }
}