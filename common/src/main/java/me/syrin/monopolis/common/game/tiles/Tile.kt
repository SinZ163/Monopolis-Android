package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

abstract class Tile(val name: String) {
    abstract fun onPlayerLand(game: Monopolis, player: Player)
}