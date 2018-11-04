package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Jail(name: String) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Nothing... its jail, we just visiting
    }
}