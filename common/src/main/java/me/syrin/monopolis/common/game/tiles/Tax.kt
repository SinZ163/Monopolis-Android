package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Tax(id: String, name: String, val taxType: TaxType) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // Deduct amount from player balance
        player.pay( when (taxType) {
            TaxType.Income -> 200
            TaxType.Super -> 100
        }, null)
    }
}