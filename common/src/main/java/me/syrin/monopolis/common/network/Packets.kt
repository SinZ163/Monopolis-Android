package me.syrin.monopolis.common.network

import kotlinx.serialization.Serializable

@Serializable
data class RawPacket(val packetID: Int, val data: IPacket)

interface IPacket {}

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

// 255
@Serializable
data class ErrorPacket(val error: String) : IPacket