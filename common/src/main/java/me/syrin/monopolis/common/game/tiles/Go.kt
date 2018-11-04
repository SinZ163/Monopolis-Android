package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Go(name: String) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Nothing... player getting 200 from passing go need to be done elsewhere
    }
}