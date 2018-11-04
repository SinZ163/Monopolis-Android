package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class FreeParking(name: String) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Nothing... its free parking!
    }
}