package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import me.syrin.monopolis.common.game.cards.Card
import java.util.*

class CardDraw(name: String, val cards: Queue<Card>) : Tile(name) {
    override fun onPlayerLand(game: Monopolis, player: Player) {
        // On land, pop card off top of cards
        val card = cards.poll()
        card.action(game, player)
        cards.offer(card)
    }
}