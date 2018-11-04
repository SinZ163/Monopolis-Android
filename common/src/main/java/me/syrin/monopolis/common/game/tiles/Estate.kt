package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Estate(name: String, price: Int, propertySet: PropertySet) : Property(name, price, propertySet) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
    }

    // TODO: housing logic
}