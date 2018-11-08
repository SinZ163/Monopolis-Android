package me.syrin.monopolis.common.game.tiles

import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.Player
import kotlin.math.abs

class Estate(id: String, name: String, propertySet: PropertySet, price: Int, val rent: Int, val oneHouse: Int, val twoHouse: Int, val threeHouse: Int, val fourHouse: Int, val hotel: Int) : Property(id, name, propertySet, price) {
    var houseCount: Int = 0

    override fun chargePlayer(game: Monopolis, player: Player) {
        // TODO: charge player
        val set = getPropertySet(game)
        var monopoly = true
        for (property in set) {
            // What
            if (owner != property.owner || property.mortgaged  )
                monopoly = false
        }

        val charge = when (houseCount) {
            5 -> hotel
            4 -> fourHouse
            3 -> threeHouse
            2 -> twoHouse
            1 -> oneHouse
            0 -> if(monopoly) rent * 2 else rent
            else -> 0
        }
        player.pay(charge, owner)
    }
    val houseMap: Map<Int, Int> = mapOf(
            Pair(0, 50),
            Pair(10, 100),
            Pair(20, 150),
            Pair(30, 200)
    )

    override fun canMortgage(game: Monopolis) : Boolean {
        val set = getPropertySet(game)
        for (property in set) {
            // if its owned by someone else, not a monopoly
            if (owner != property.owner) return true
            // What
            if (property !is Estate) return false
            if (property.houseCount > 0) return false
        }
        return true
    }

    fun addHouse(game: Monopolis) {
        val cost = houseMap[game.tiles.indexOf(this)/10]!!
        // Not enough houses / hotels
        if (game.houseCount == 0 && houseCount < 4) return
        if (game.hotelCount == 0 && houseCount == 4) return
        // Already have the hotel, go away
        if (houseCount > 4) return
        val set = getPropertySet(game)
        for (tile in set) {
            if (tile.owner != owner) return
            // What
            if (tile !is Estate) return

            // Keep house count even
            if (abs(houseCount+1 - tile.houseCount) > 1) return
        }

        if (owner != null && owner!!.balance > cost) {
            houseCount += 1
            if (houseCount == 5) {
                game.hotelCount -= 1
                game.houseCount += 4
            } else {
                game.houseCount -= 1
            }
            owner!!.pay(cost, null)
        }
    }
    fun removeHouse(game: Monopolis) {
        val cost = houseMap[game.tiles.indexOf(this)/10]!! / 2
        // Cant get money from nothing
        if (houseCount <= 0) return
        // Not enough houses to refill
        if (houseCount == 5 && game.houseCount < 4) return
        val set = getPropertySet(game)
        for (tile in set) {
            // What
            if (tile !is Estate) return
            // Keep house count even
            if (abs(houseCount - 1 - tile.houseCount) > 1) return
        }
        if (owner != null) {
            houseCount -= 1
            if (houseCount == 4) {
                game.houseCount -= 4
                game.hotelCount += 1
            } else {
                game.houseCount -= 1
            }
            owner!!.credit(cost)
        }
    }

    // TODO: housing logic
}