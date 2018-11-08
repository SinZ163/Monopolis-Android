package me.syrin.monopolis.common.network

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class RawPacket(val packetID: Int, val data: IPacket)

interface IPacket {}
interface GamePacket : IPacket {}

// 0
@Serializable
data class ChatPacket(val message: String, val author: String? = null) : IPacket

// 1
@Serializable
data class LoginPacket(val name: String) : IPacket

// 2
@Serializable
data class LobbyListPacket(val lobbies: List<Lobby>) : IPacket

// 3
@Serializable
data class Lobby(val lobbyID: Int, val lobbyName: String, val playerCount: Int, val maxCount: Int) : IPacket

// 4
@Serializable
data class LobbyClosedPacket(val lobbyID: Int) : IPacket

// 5
@Serializable
data class LeaveLobbyPacket(val lobbyID: Int) : IPacket

// 6
@Serializable
data class CreateLobbyPacket(val lobbyName: String, val maxCount: Int) : IPacket

// 7
@Serializable
data class JoinLobbyPacket(val lobbyID: Int) : IPacket

// 8
@Serializable
data class LobbyInfoPacket(val id: Int, val name: String, val ingame: Boolean, val maxCount: Int, val host: String, val players: List<String>) : IPacket

// 9
@Serializable
class StartLobbyPacket : IPacket

// 10
@Serializable
data class TurnStartPacket(val playerName: String) : GamePacket

// 11
@Serializable
data class PlayerRollPacket(val playerName: String, val dice1: Int, val dice2: Int) : GamePacket


// 12
@Serializable
data class PayBailPacket(val playerName: String) : GamePacket

// 13
@Serializable
data class UseJailCardPacket(val playerName: String) : GamePacket


// 14
@Serializable
data class PurchasePropertyPacket(val playerName: String, val tile: Int) : GamePacket

// 15
@Serializable
data class PayPersonPacket(val sender: String, @Optional val receiver: String? = null, val amount: Int) : GamePacket

// 16
@Serializable
data class GainMoneyPacket(val playerName: String, val amount: Int) : GamePacket

// 17
@Serializable
data class CardDrawPacket(val playerName: String, val cardID: Int) : GamePacket

// 18
@Serializable
data class UpgradePropertyPacket(val playerName: String, val tile: Int) : GamePacket
// 19
@Serializable
data class DowngradePropertyPacket(val playerName: String, val tile: Int) : GamePacket

// 253
@Serializable
class PlaybackStartPacket : GamePacket

// 254
@Serializable
class PlaybackEndPacket : GamePacket

// 255
@Serializable
data class ErrorPacket(val error: String) : IPacket