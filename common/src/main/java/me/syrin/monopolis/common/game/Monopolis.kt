package me.syrin.monopolis.common.game

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import me.syrin.monopolis.common.GenericMessageDialogFragment
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType
import me.syrin.monopolis.common.game.tiles.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random

class Monopolis(val activity: FragmentActivity, playerList: List<String> = listOf()) {
    var tiles = listOf<Tile>()

    val players = arrayListOf<Player>()
    var currentPlayer = 0

    val communityChestCards = getCards(CardType.CommunityChest)
    val chanceCards = getCards(CardType.Chance)

    var diceOneAmount: Int = 0  // Set each turn
    var diceTwoAmount: Int = 0  // Set each turn
    fun diceTotal(): Int = diceOneAmount + diceTwoAmount

    init {
        // Read tiles in
        tiles = readTileDataFromCSV(
                activity.assets.open("tile_data.csv"),
                activity.assets.open("tile_names/uk.csv")
        )

        for (name in playerList) {
            players.add(Player(this, name))
        }
    }

    fun displayGenericMessageDialog(title: String, description: String) {
        val dialog = GenericMessageDialogFragment()
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("description", description)
        dialog.arguments = bundle
        dialog.show(activity.supportFragmentManager, "test")
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
                }, when (parts[2]) {
                    "CommunityChest" -> communityChestCards
                    "Chance" -> chanceCards
                    else -> throw Exception("invalid card type")
                })
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

    fun getCards(cardType: CardType): MutableList<Card> {
        val cards = when (cardType) {
            CardType.CommunityChest -> mutableListOf(
                    Card(CardType.CommunityChest, "Advance to Go", "Advance to Go and collect ₩200.") { game, player, card ->
                        player.advanceTo("Go")
                    },
                    Card(CardType.CommunityChest, "Bank error in your favor", "Collect ₩200.") { game, player, card ->
                        player.credit(200)
                    },
                    Card(CardType.CommunityChest, "Doctor's fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                    },
                    Card(CardType.CommunityChest, "From sale of stock you get ₩50", "Receive ₩50.") { game, player, card ->
                        player.credit(50)
                    },
                    Card(CardType.CommunityChest, "Get Out of Jail Free", "This card may be kept until needed or sold/traded.", false) { game, player, card ->
                        player.jailCards.add(card)
                    },
                    Card(CardType.CommunityChest, "Go to Jail", "Go directly to jail. Do not pass Go, Do not collect ₩200.") { game, player, card ->
                        player.sendToJail()
                    },
                    Card(CardType.CommunityChest, "Grand Opera Night", "Collect ₩50 from every player for opening night seats.") { game, player, card ->
                        for (p in game.players) {
                            p.pay(50, player)
                        }
                    },
                    Card(CardType.CommunityChest, "Holiday Fund matures", "Receive ₩100.") { game, player, card ->
                        player.credit(100)
                    },
                    Card(CardType.CommunityChest, "Income tax refund", "Collect ₩20.") { game, player, card ->
                        player.credit(20)
                    },
                    Card(CardType.CommunityChest, "It is your birthday", "Collect ₩10 from every player.") { game, player, card ->
                        for (p in game.players) {
                            p.pay(10, player)
                        }
                    },
                    Card(CardType.CommunityChest, "Life insurance matures", "Collect ₩100.") { game, player, card ->
                        player.credit(100)
                    },
                    Card(CardType.CommunityChest, "Hospital Fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                    },
                    Card(CardType.CommunityChest, "School fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                    },
                    Card(CardType.CommunityChest, "Receive ₩25 consultancy fee", "Receive ₩25.") { game, player, card ->
                        player.credit(25)
                    },
                    Card(CardType.CommunityChest, "You are assessed for street repairs", "Pay ₩40 per house and ₩115 per hotel you own.") { game, player, card ->
                        // TODO: requires houses and hotels to be implemented
                    },
                    Card(CardType.CommunityChest, "You have won second prize in a beauty contest", "Collect ₩10.") { game, player, card ->
                        player.credit(10)
                    },
                    Card(CardType.CommunityChest, "You inherit ₩100", "Receive ₩100.") { game, player, card ->
                        player.credit(100)
                    }
            )
            CardType.Chance -> mutableListOf(
                    Card(CardType.Chance, "Advance to Go", "Advance to Go and collect ₩200.") { game, player, card ->
                        player.advanceTo("Go")
                    },
                    Card(CardType.Chance, "Advance to RedC", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("RedC")
                    },
                    Card(CardType.Chance, "Advance to PinkA", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("PinkA")
                    },
                    Card(CardType.Chance, "Advance to nearest Utility", "If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.") { game, player, card ->
                        player.advanceToNearest(PropertySet.Utility)
                    },
                    Card(CardType.Chance, "Advance to the nearest Railroad", "If Railroad is unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which he/she is otherwise entitled.") { game, player, card ->
                        player.advanceToNearest(PropertySet.Railroad)
                    },
                    Card(CardType.Chance, "Bank pays you dividend of ₩50", "Receive ₩50") { game, player, card ->
                        player.credit(50)
                    },
                    Card(CardType.Chance, "Get out of Jail Free", "This card may be kept until needed, or traded/sold.", false) { game, player, card ->
                        player.jailCards.add(card)
                    },
                    Card(CardType.Chance, "Go Back Three Spaces", "Go Back Three Spaces.") { game, player, card ->
                        player.location -= 3
                        if (player.location < 0) {
                            // for example -1 (mayfair), which is 39. 40 + -1 = 39
                            player.location += 40
                        }
                        game.tiles[player.location].onPlayerLand(game, player)
                    },
                    Card(CardType.Chance, "Go to Jail", "Go directly to Jail. Do not pass GO, do not collect ₩200.") { game, player, card ->
                        player.sendToJail()
                    },
                    Card(CardType.Chance, "Make general repairs on all your property", "For each house pay ₩25, For each hotel pay ₩100.") { game, player, card ->
                        // TODO: requires houses and hotels to be implemented
                    },
                    Card(CardType.Chance, "Pay poor tax of ₩15", "Pay ₩15.") { game, player, card ->
                        player.pay(15, null)
                    },
                    Card(CardType.Chance, "Take a trip to RailroadA", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("RailroadA")
                    },
                    Card(CardType.Chance, "Take a walk at DarkBlueB", "Advance token to DarkBlueB") { game, player, card ->
                        player.advanceTo("DarkBlueB")
                    },
                    Card(CardType.Chance, "You have been elected Chairman of the Board", "Pay each player ₩50.") { game, player, card ->
                        for (p in game.players) {
                            player.pay(50, p)
                        }
                    },
                    Card(CardType.Chance, "Your building loan matures", "Receive ₩150.") { game, player, card ->
                        player.credit(150)
                    },
                    Card(CardType.Chance, "You have won a crossword competition", "Collect ₩100.") { game, player, card ->
                        player.credit(100)
                    }
            )
        }

        cards.shuffle()

        return cards
    }

    fun removePlayer(name: String) {
        // TODO: remove player and cleanup assets
    }

    fun doTurn() {
        // Next player
        currentPlayer = ((currentPlayer + 1) % players.count())
        var player = players[currentPlayer]

        // Check for jail
        if (tiles[player.location].name == "Jail" && player.jailed) {
            // Check if player can jail roll
            if (player.remainingJailRolls > 0) {
                // TODO: give player choice to pay
                rollDice()
                player.remainingJailRolls--
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
            normalRoll(player)
        }
    }

    private fun normalRoll(player: Player, rollJailCount: Int = 0) {
        rollDice()

        if (diceOneAmount == diceTwoAmount) {
            // rolled a double
            if (rollJailCount >= 2) {
                // this is the third double! jail time
                player.sendToJail()
                return
            } else {
                // double! move and roll again
                player.moveForward(diceTotal())
                normalRoll(player, rollJailCount + 1)
            }
        }

        player.moveForward(diceTotal())
    }

    private fun rollDice() {
        // Roll dice
        diceOneAmount = Random.nextInt(1,7)
        diceTwoAmount = Random.nextInt(1,7)
    }
}