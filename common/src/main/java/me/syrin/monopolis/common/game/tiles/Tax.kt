package me.syrin.monopolis.common.game.tiles

import android.util.Log
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

class Tax(id: String, name: String, val taxType: TaxType) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // Deduct amount from player balance
        Log.i("Monopolis:send_d", "Charging ${player.name} some amount")
        player.pay( when (taxType) {
            TaxType.Income -> 200
            TaxType.Super -> 100
        }, null)
        return true
    }
}