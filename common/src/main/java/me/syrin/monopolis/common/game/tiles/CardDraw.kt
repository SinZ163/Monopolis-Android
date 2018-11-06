package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType

class CardDraw(id: String, name: String, val cardType: CardType, val cards: MutableList<Card>) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // On land, pop card off top of cards
        val card = cards.removeAt(0)
        game.displayGenericMessageDialog(card.title, card.description)
        card.action(game, player, card)
        if (card.replaceInDeck) {
            cards.add(card)
        }
    }
}