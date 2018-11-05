package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Railroad(id: String, name: String) : Property(id, name, PropertySet.Railroad, 200) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
    }
}