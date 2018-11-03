package me.syrin.monopolis.common.network

import kotlinx.serialization.Serializable

@Serializable
data class RawPacket(val packetID: Int, val data: IPacket)

interface IPacket {}

@Serializable
data class ChatPacket(val msg: String) : IPacket

@Serializable
data class ErrorPacket(val error: String) : IPacket