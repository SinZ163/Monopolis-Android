package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class GoToJail(name: String) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Change player location to jail, and set status and newly jailed
        player.sendToJail()
    }
}