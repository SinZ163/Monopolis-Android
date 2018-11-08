package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType
import me.syrin.monopolis.common.network.CardDrawPacket

class CardDraw(id: String, name: String, val cardType: CardType, val cards: MutableList<Card>) : Tile(id, name) {
    override fun onPlayerLand(game: Monopolis, player: Player) : Boolean {
        // On land, pop card off top of cards
        val card = cards[0].cardId
        game.send(CardDrawPacket(player.name, card))

        return false
    }
    fun draw(game: Monopolis, player: Player, cardId: Int) {
        val card = cards.first { it.cardId == cardId }
        if (!game.playback)
            game.displayGenericMessageDialog(card.title, card.description)
        card.action(game, player, card)
        if (card.replaceInDeck) {
            cards.add(card)
        }
    }
}