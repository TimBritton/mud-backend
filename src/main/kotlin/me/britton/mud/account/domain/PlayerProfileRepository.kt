package me.britton.mud.account.domain

import io.vertx.core.AsyncResult

interface PlayerProfileRepository {

    suspend fun readPlayerProfile(id: String): PlayerProfile

    suspend fun createPlayerProfile(player: PlayerProfile): String

    suspend fun updatePlayerProfile(player: PlayerProfile): PlayerProfile

}