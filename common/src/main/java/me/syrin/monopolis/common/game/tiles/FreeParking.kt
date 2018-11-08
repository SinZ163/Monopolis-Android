package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class FreeParking(id: String, name: String) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // Nothing... its free parking!
        return true
    }
}