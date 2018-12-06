package me.britton.mud.account.domain.mongo

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.JsonObject
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.ext.mongo.findOneAndUpdateAwait
import io.vertx.kotlin.ext.mongo.findOneAwait
import io.vertx.kotlin.ext.mongo.saveAwait
import me.britton.mud.account.domain.PlayerProfile
import me.britton.mud.account.domain.PlayerProfileRepository

class CorMongoPlayerProfileRepo(vertx: Vertx) : PlayerProfileRepository {

    val mongoclient = MongoClient.createShared(vertx, JsonObject("host" to "localhost", "port" to 28017))

    override suspend fun readPlayerProfile(id: String): PlayerProfile = mongoclient.findOneAwait("players", JsonObject("_id" to id), JsonObject()).apply { this.put("id", this.getString("_id")) }.mapTo(PlayerProfile::class.java)



    override suspend fun createPlayerProfile(player: PlayerProfile): String {
        val json = convertToJson(player)
       return mongoclient.saveAwait("players", json)
    }

    override suspend fun updatePlayerProfile(player: PlayerProfile): PlayerProfile {
      return mongoclient.findOneAndUpdateAwait("players", JsonObject("_id" to player.id), convertToJson(player)).mapTo(PlayerProfile::class.java)
    }

    private fun convertToJson(x: PlayerProfile):  JsonObject{
        return Json.encodeToBuffer(x).toJsonObject()
    }
}