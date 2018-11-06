package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType
import java.util.*

class CardDraw(id: String, name: String, val cardType: CardType, val cards: Queue<Card>) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // On land, pop card off top of cards
        val card = cards.poll()
        card.action(game, player, card)
        if (card.replaceInDeck) {
            cards.offer(card)
        }
    }
}