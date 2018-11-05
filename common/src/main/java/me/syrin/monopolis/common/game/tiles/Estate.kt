package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Estate(id: String, name: String, propertySet: PropertySet, price: Int, val rent: Int, val oneHouse: Int, val twoHouse: Int, val threeHouse: Int, val fourHouse: Int, val hotel: Int) : Property(id, name, propertySet, price) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
    }

    // TODO: housing logic
}