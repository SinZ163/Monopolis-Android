package me.syrin.monopolis.common

data class LobbyState(val id: Int, val name: String, var ingame: Boolean, val host: String, var players: ArrayList<String>)