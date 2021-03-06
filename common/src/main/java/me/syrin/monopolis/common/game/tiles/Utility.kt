package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import kotlin.random.Random

class Utility(id: String, name: String) : Property(id, name, PropertySet.Utility, 150) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // Pay from current dice roll
        chargeAmount(game, player, game.diceTotal())
    }

    private fun chargeAmount(game: Monopolis, player: Player, baseAmount: Int) {
        // If owned by another player with monopoly on utilities, pay 10x base amount
        // TODO: this
        // If owned by another player without monopoly on utilities, pay 4x base amount
        // TODO: this
        val set = getPropertySet(game).filter { owner == it.owner}
        val multiplier = when(set.size) {
            2 -> 10
            1 -> 4
            else -> 0
        }
        player.pay(multiplier*baseAmount, owner)
    }
}