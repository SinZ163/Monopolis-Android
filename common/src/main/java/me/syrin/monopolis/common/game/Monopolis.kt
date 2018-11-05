package me.syrin.monopolis.common.game

import android.content.Context
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType
import me.syrin.monopolis.common.game.tiles.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.random.Random

class Monopolis(context: Context) {
    var tiles = listOf<Tile>()

    val players = arrayListOf<Player>()
    var currentPlayer = 0

    var diceOneAmount: Int = 0  // Set each turn
    var diceTwoAmount: Int = 0  // Set each turn
    fun diceTotal(): Int = diceOneAmount + diceTwoAmount
    fun rollValue(): Int {
        return if (diceOneAmount == diceTwoAmount)
            diceTotal() * 2
        else
            diceTotal()
    }

    init {
        // Read tiles in
        tiles = readTileDataFromCSV(
                context.assets.open("tile_data.csv"),
                context.assets.open("tile_names/uk.csv")
        )
    }

    fun readTileDataFromCSV(tileInput: InputStream, nameInput: InputStream): List<Tile> {
        val tileDataReader = BufferedReader(InputStreamReader(tileInput))
        val nameDataReader = BufferedReader(InputStreamReader(nameInput))

        var tiles = arrayListOf<Tile>()

        var dataString = tileDataReader.readLine()
        var nameString = nameDataReader.readLine()
        while (dataString != null) {
            // parse line into tiles
            val parts = dataString.split(",")
            val tile = when (parts[0]) {
                "Go" -> Go(parts[1], nameString)
                "Jail" -> Jail(parts[1], nameString)
                "FreeParking" -> FreeParking(parts[1], nameString)
                "GoToJail" -> GoToJail(parts[1], nameString)
                "Railroad" -> Railroad(parts[1], nameString)
                "Utility" -> Utility(parts[1], nameString)
                "Tax" -> Tax(parts[1], nameString, when (parts[2]) {
                    "Income" -> TaxType.Income
                    "Super" -> TaxType.Super
                    else -> throw Exception("invalid tax type")
                })
                "CardDraw" -> CardDraw(parts[1], nameString, when (parts[2]) {
                    "CommunityChest" -> CardType.CommunityChest
                    "Chance" -> CardType.Chance
                    else -> throw Exception("invalid card type")
                }, PriorityQueue<Card>())
                "Estate" -> Estate(parts[1], nameString, when (parts[2]) {
                    "Brown" -> PropertySet.Brown
                    "LightBlue" -> PropertySet.LightBlue
                    "Pink" -> PropertySet.Pink
                    "Orange" -> PropertySet.Orange
                    "Red" -> PropertySet.Red
                    "Yellow" -> PropertySet.Yellow
                    "Green" -> PropertySet.Green
                    "DarkBlue" -> PropertySet.DarkBlue
                    else -> throw Exception("invalid property set")
                }, parts[3].toInt(), parts[4].toInt(), parts[5].toInt(), parts[6].toInt(), parts[7].toInt(), parts[8].toInt(), parts[9].toInt())
                else -> throw Exception("invalid tile data")
            }

            tiles.add(tile)

            dataString = tileDataReader.readLine()
            nameString = nameDataReader.readLine()
        }

        return tiles
    }

    fun doTurn() {
        // Next player
        currentPlayer = ((currentPlayer + 1) % players.count())
        var player = players[currentPlayer]

        // Check for jail
        if (tiles[player.location].name == "Jail" && player.jailed) {
            // Check if player can jail roll
            if (player.remainingJailRolls > 0) {
                rollDice()
                if (diceOneAmount == diceTwoAmount) {
                    // Free from jail
                    player.freeFromJail()
                }
            }
            else {
                // Pay to unjail
                player.payBail()
            }
        }
        else {
            // Normal turn
            rollDice()
            player.location = ((player.location + rollValue()) % tiles.count())
            // Pass go?
            if (rollValue() > player.location) {
                player.credit(200)
            }
            tiles[player.location].onPlayerLand(this, player)
        }
    }

    private fun rollDice() {
        // Roll dice
        diceOneAmount = Random.nextInt(1,7)
        diceTwoAmount = Random.nextInt(1,7)
    }
}