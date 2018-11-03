package me.syrin.monopolis.common.network

import kotlinx.serialization.Serializable

@Serializable
data class RawPacket(val packetID: Int, val data: IPacket)

interface IPacket {}

@Serializable
data class ChatPacket(val msg: String) : IPacket

@Serializable
data class LoginPacket(val name: String) : IPacket

@Serializable
data class Lobby(val lobbyID: Int, val lobbyName: String, val playerCount: Int, val maxCount: Int) : IPacket

@Serializable
data class LobbyListPacket(val lobbies: List<Lobby>)

@Serializable
data class ErrorPacket(val error: String) : IPacket