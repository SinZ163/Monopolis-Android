package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import kotlin.random.Random

class Utility(name: String) : Property(name, 150, PropertySet.Utility) {
    override fun chargePlayer(game: Monopolis, player: Player) {
        // Pay from current dice roll
        chargeAmount(game, player, game.diceTotal())
    }

    fun chargeWithNewRoll(game: Monopolis, player: Player) {
        // Generate dice rolls and pay
        val baseAmount = Random.nextInt(1, 7) + Random.nextInt(1, 7)
        chargeAmount(game, player, baseAmount)
    }

    fun chargeAmount(game: Monopolis, player: Player, baseAmount: Int) {
        // If owned by another player with monopoly on utilities, pay 10x base amount
        // TODO: this
        // If owned by another player without monopoly on utilities, pay 4x base amount
        // TODO: this
    }
}