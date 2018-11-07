package me.syrin.monopolis.common.game.cards

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player

data class Card(val cardId: Int, val cardType: CardType, val title: String, val description: String, val replaceInDeck: Boolean = true, val action: (game: Monopolis, player: Player, card: Card) -> Unit)