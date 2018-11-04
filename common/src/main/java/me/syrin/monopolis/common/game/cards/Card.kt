package me.syrin.monopolis.common.game.cards

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

data class Card(val cardType: CardType, val title: String, val description: String, val action: (game: Monopolis, player: Player) -> Unit)