package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class GoToJail(id: String, name: String) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // Change player location to jail, and set status and newly jailed
        game.sendStatus("${player.name} has been sent to jail!")
        player.sendToJail()
        return true
    }
}