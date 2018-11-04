package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Tax(name: String, val amount: Int) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Deduct amount from player balance
        player.pay(amount, null)
    }
}