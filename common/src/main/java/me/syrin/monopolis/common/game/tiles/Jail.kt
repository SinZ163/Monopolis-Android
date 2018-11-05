package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Jail(id: String, name: String) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Nothing... its jail, we just visiting
    }
}