package me.syrin.monopolis.common.game

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.syrin.monopolis.common.GenericMessageDialogFragment
import me.syrin.monopolis.common.game.cards.Card
import me.syrin.monopolis.common.game.cards.CardType
import me.syrin.monopolis.common.game.tiles.*
import me.syrin.monopolis.common.network.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random


class Monopolis(val activity: FragmentActivity, playerList: List<String> = listOf()) {
    var tiles = listOf<Tile>()

    val uiUpdates: MutableLiveData<Int> = MutableLiveData()
    fun updateUI() {uiUpdates.value = uiUpdates.value?:0 + 1}

    val players = arrayListOf<Player>()
    var playerMap: Map<String, Player> = mapOf()
    var currentPlayer = 0

    val communityChestCards = getCards(CardType.CommunityChest)
    val chanceCards = getCards(CardType.Chance)

    var diceOneAmount: Int = 0  // Set each turn
    var diceTwoAmount: Int = 0  // Set each turn
    var rollJailCount: Int = 0

    fun diceTotal(): Int = diceOneAmount + diceTwoAmount

    private var isUtilityCard: Boolean = false


    var playback: Boolean = false
    private var nextPacket = 0
    var turnState = TurnState.RollDice

    enum class TurnState {
        RollDice,
        Landed,
        LandedDoubles,
        EndTurn
    }

    init {
        uiUpdates.value = 0

        // Read tiles in
        val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val board = preferences.getString("playerBoard", "uk")
        tiles = readTileDataFromCSV(
                activity.assets.open("tile_data.csv"),
                activity.assets.open("tile_names/$board.csv")
        )

        for (name in playerList) {
            val player = Player(this, name)
            players.add(player)
            playerMap = playerMap.plus(Pair(name, player))
        }
    }

    fun start() {
        NetworkHandler.packets.observe(activity, Observer {
            Log.i("WebSocket", "Monopolis handler ran, ${it.size}")
            if (nextPacket < it.size - 1) {
                activity.longToast("Catching up to speed, this might look weird")
                playback = true
            } else {
                playback = false
            }
            while(nextPacket < it.size) {
                val packet = it[nextPacket]
                when(packet) {
                    is TurnStartPacket -> {
                        activity.toast("${packet.playerName}'s turn now!")
                        // Cleanup variables from previous turns state
                        rollJailCount = 0
                        diceOneAmount = 0  // Hopefully only used for utility logic
                        diceTwoAmount = 0  // Hopefully only used for utility logic
                        turnState = TurnState.RollDice
                        // TODO: Disable UI if not our turn
                        // TODO: Replace this with UI
                        currentPlayer = players.indexOf(playerMap[packet.playerName])
                        updateUI()
                        //val player: Player = playerMap[packet.playerName]!!
                        //send(PlayerRollPacket(player.name, Random.nextInt(1,7), Random.nextInt(1,7)))
                    }
                    is PlayerRollPacket -> {
                        val player: Player = playerMap[packet.playerName]!!
                        diceOneAmount = packet.dice1
                        diceTwoAmount = packet.dice2
                        turnState = TurnState.Landed

                        if (isUtilityCard) {
                            isUtilityCard = false
                            // TODO: special case, pay 10x regardless
                            player.advanceToNearest(PropertySet.Utility)
                        } else if (tiles[player.location].name == "Jail" && player.jailed) {
                            player.remainingJailRolls--
                            if (diceOneAmount == diceTwoAmount) {
                                // Free from jail
                                player.freeFromJail()
                            } else {
                                player.payBail()
                            }
                            player.moveForward(packet.dice1 + packet.dice2)
                        } else {
                            if (packet.dice1 == packet.dice2) {
                                activity.toast("Doubles!")
                                // rolled a double
                                if (rollJailCount >= 2) {
                                    // this is the third double! jail time
                                    player.sendToJail()
                                    endTurn()
                                } else {
                                    turnState = TurnState.LandedDoubles
                                    // double! move and roll again
                                    rollJailCount += 1

                                    // do the first roll
                                    player.moveForward(packet.dice1 + packet.dice2)
                                }
                            } else {
                                player.moveForward(packet.dice1 + packet.dice2)
                            }
                        }
                        updateUI()
                    }
                    is PurchasePropertyPacket -> {
                        val player = playerMap[packet.playerName]!!
                        val property = tiles[packet.tile] as Property
                        property.purchase(player)
                    }
                    is PayPersonPacket -> {
                        val sender = playerMap[packet.sender]!!
                        val receiver = playerMap[packet.receiver]

                        // TODO: Make sure this is the real deal
                        sender.pay(packet.amount, receiver, true)
                    }
                    is GainMoneyPacket -> {
                        val player = playerMap[packet.playerName]!!
                        // TODO: Make sure this is the real deal
                        player.credit(packet.amount, true)
                    }
                    is CardDrawPacket -> {
                        // TODO: do lookup stuff and apply
                        val player = playerMap[packet.playerName]!!
                        val cardTile = tiles[player.location] as CardDraw
                        cardTile.draw(this, player ,packet.cardID)
                    }
                }
                nextPacket += 1
            }
            playback = false
        })
    }
    fun endTurn() {
        Log.i("Monopolis:endTurn", "Current TurnState: ${turnState.name}")
        when(turnState) {
            TurnState.Landed -> turnState = TurnState.EndTurn
            TurnState.LandedDoubles -> turnState = TurnState.RollDice
            else -> activity.toast("How did you do this")
        }
        updateUI()
    }
    // Wire this up to UI
    fun endTurnPressed() {
        val currentPlayer = ((currentPlayer + 1) % players.count())
        val newPlayer = players[currentPlayer]
        send(TurnStartPacket(newPlayer.name), true)
    }
    fun rollDicePressed() {
        send(PlayerRollPacket(players[currentPlayer].name, Random.nextInt(1,7), Random.nextInt(1,7)))
    }

    fun displayGenericMessageDialog(title: String, description: String) {
        val dialog = GenericMessageDialogFragment()
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("description", description)
        dialog.arguments = bundle
        dialog.show(activity.supportFragmentManager, "generic_dialog")
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
                    Card(0, CardType.CommunityChest, "Advance to Go", "Advance to Go and collect ₩200.") { game, player, card ->
                        player.advanceTo("Go")
                    },
                    Card(1, CardType.CommunityChest, "Bank error in your favor", "Collect ₩200.") { game, player, card ->
                        player.credit(200)
                        endTurn()
                    },
                    Card(2, CardType.CommunityChest, "Doctor's fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                        endTurn()
                    },
                    Card(3, CardType.CommunityChest, "From sale of stock you get ₩50", "Receive ₩50.") { game, player, card ->
                        player.credit(50)
                        endTurn()
                    },
                    Card(4, CardType.CommunityChest, "Get Out of Jail Free", "This card may be kept until needed or sold/traded.", false) { game, player, card ->
                        player.jailCards.add(card)
                        endTurn()
                    },
                    Card(5, CardType.CommunityChest, "Go to Jail", "Go directly to jail. Do not pass Go, Do not collect ₩200.") { game, player, card ->
                        player.sendToJail()
                        endTurn()
                    },
                    Card(6, CardType.CommunityChest, "Grand Opera Night", "Collect ₩50 from every player for opening night seats.") { game, player, card ->
                        for (p in game.players) {
                            p.pay(50, player)
                        }
                        endTurn()
                    },
                    Card(7, CardType.CommunityChest, "Holiday Fund matures", "Receive ₩100.") { game, player, card ->
                        player.credit(100)
                        endTurn()
                    },
                    Card(8, CardType.CommunityChest, "Income tax refund", "Collect ₩20.") { game, player, card ->
                        player.credit(20)
                        endTurn()
                    },
                    Card(9, CardType.CommunityChest, "It is your birthday", "Collect ₩10 from every player.") { game, player, card ->
                        for (p in game.players) {
                            p.pay(10, player)
                        }
                        endTurn()
                    },
                    Card(10, CardType.CommunityChest, "Life insurance matures", "Collect ₩100.") { game, player, card ->
                        player.credit(100)
                        endTurn()
                    },
                    Card(11, CardType.CommunityChest, "Hospital Fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                        endTurn()
                    },
                    Card(12, CardType.CommunityChest, "School fees", "Pay ₩50.") { game, player, card ->
                        player.pay(50, null)
                        endTurn()
                    },
                    Card(13, CardType.CommunityChest, "Receive ₩25 consultancy fee", "Receive ₩25.") { game, player, card ->
                        player.credit(25)
                        endTurn()
                    },
                    Card(14, CardType.CommunityChest, "You are assessed for street repairs", "Pay ₩40 per house and ₩115 per hotel you own.") { game, player, card ->
                        // TODO: requires houses and hotels to be implemented
                        activity.toast("Pretend you lost money")
                        endTurn()
                    },
                    Card(15, CardType.CommunityChest, "You have won second prize in a beauty contest", "Collect ₩10.") { game, player, card ->
                        player.credit(10)
                        endTurn()
                    },
                    Card(16, CardType.CommunityChest, "You inherit ₩100", "Receive ₩100.") { game, player, card ->
                        player.credit(100)
                        endTurn()
                    }
            )
            CardType.Chance -> mutableListOf(
                    Card(0, CardType.Chance, "Advance to Go", "Advance to Go and collect ₩200.") { game, player, card ->
                        player.advanceTo("Go")
                    },
                    Card(1, CardType.Chance, "Advance to RedC", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("RedC")
                    },
                    Card(2, CardType.Chance, "Advance to PinkA", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("PinkA")
                    },
                    Card(3, CardType.Chance, "Advance to nearest Utility", "If unowned, you may buy it from the Bank. If owned, throw dice and pay owner a total 10 times the amount thrown.") { game, player, card ->
                        // Logic teleports to PlayerRoll handler
                        isUtilityCard = true
                        send(PlayerRollPacket(player.name, Random.nextInt(1, 7), Random.nextInt(1, 7)))
                    },
                    Card(4, CardType.Chance, "Advance to the nearest Railroad", "If Railroad is unowned, you may buy it from the Bank. If owned, pay owner twice the rental to which he/she is otherwise entitled.") { game, player, card ->
                        player.advanceToNearest(PropertySet.Railroad)
                    },
                    Card(5, CardType.Chance, "Bank pays you dividend of ₩50", "Receive ₩50") { game, player, card ->
                        player.credit(50)
                        endTurn()
                    },
                    Card(6, CardType.Chance, "Get out of Jail Free", "This card may be kept until needed, or traded/sold.", false) { game, player, card ->
                        player.jailCards.add(card)
                        endTurn()
                    },
                    Card(7, CardType.Chance, "Go Back Three Spaces", "Go Back Three Spaces.") { game, player, card ->
                        player.moveBackward(3)
                    },
                    Card(8, CardType.Chance, "Go to Jail", "Go directly to Jail. Do not pass GO, do not collect ₩200.") { game, player, card ->
                        player.sendToJail()
                        endTurn()
                    },
                    Card(9, CardType.Chance, "Make general repairs on all your property", "For each house pay ₩25, For each hotel pay ₩100.") { game, player, card ->
                        // TODO: requires houses and hotels to be implemented
                        activity.toast("Pretend you lost money please")
                        endTurn()
                    },
                    Card(10, CardType.Chance, "Pay poor tax of ₩15", "Pay ₩15.") { game, player, card ->
                        player.pay(15, null)
                        endTurn()
                    },
                    Card(11, CardType.Chance, "Take a trip to RailroadA", "If you pass Go, collect ₩200.") { game, player, card ->
                        player.advanceTo("RailroadA")
                    },
                    Card(12, CardType.Chance, "Take a walk at DarkBlueB", "Advance token to DarkBlueB") { game, player, card ->
                        player.advanceTo("DarkBlueB")
                    },
                    Card(13, CardType.Chance, "You have been elected Chairman of the Board", "Pay each player ₩50.") { game, player, card ->
                        for (p in game.players) {
                            player.pay(50, p)
                        }
                        endTurn()
                    },
                    Card(14, CardType.Chance, "Your building loan matures", "Receive ₩150.") { game, player, card ->
                        player.credit(150)
                        endTurn()
                    },
                    Card(15, CardType.Chance, "You have won a crossword competition", "Collect ₩100.") { game, player, card ->
                        player.credit(100)
                        endTurn()
                    }
            )
        }

        cards.shuffle()

        return cards
    }

    fun removePlayer(name: String) {
        // TODO: remove player and cleanup assets
    }

    fun send(packet: GamePacket, smurf: Boolean = false) {
        if (!playback) {
            Log.i("Monopolis:send", "Not in playback mode")
            Log.v("Monopolis:send", "Sending $packet")
            if (players[currentPlayer].name == NetworkHandler.name || smurf)
                WebSocket.send(packet)
        } else {
            Log.i("Monopolis:send", "Predicting we don't need to send due to being in playback mode")
            Log.v("Monopolis:send", "Did not send $packet")
        }
    }
}