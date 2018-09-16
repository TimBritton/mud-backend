package me.britton.mud.account.domain.mongo

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.JsonObject
import io.vertx.kotlin.ext.mongo.findOneAwait
import me.britton.mud.account.domain.PlayerProfile
import me.britton.mud.account.domain.PlayerProfileRepository

class CorMongoPlayerProfileRepo(vertx: Vertx) : PlayerProfileRepository {

    val mongoclient = MongoClient.createShared(vertx, JsonObject("host" to "locahost", "port" to 28017))

    override suspend fun readPlayerProfile(id: String): PlayerProfile = mongoclient.findOneAwait("players", JsonObject("id" to id), JsonObject()).mapTo(PlayerProfile::class.java)



    override suspend fun createPlayerProfile(player: PlayerProfile): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updatePlayerProfile(player: PlayerProfile): PlayerProfile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}