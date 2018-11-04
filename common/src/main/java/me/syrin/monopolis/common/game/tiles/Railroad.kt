package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Railroad(name: String) : Property(name, 200, PropertySet.Railroad) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
    }
}