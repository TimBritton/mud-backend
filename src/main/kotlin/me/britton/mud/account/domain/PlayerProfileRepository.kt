package me.britton.mud.account.domain

interface PlayerProfileRepository {

    suspend fun readPlayerProfile(id: String): PlayerProfile

    suspend fun createPlayerProfile(player: PlayerProfile): Boolean

    suspend fun updatePlayerProfile(player: PlayerProfile): PlayerProfile

}