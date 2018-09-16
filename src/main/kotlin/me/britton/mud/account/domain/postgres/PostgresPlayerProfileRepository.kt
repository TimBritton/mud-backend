//package me.britton.mud.account.domain.postgres
//
//
//import me.britton.mud.account.domain.PlayerProfile
//import me.britton.mud.account.domain.PlayerProfileRepository
//import io.reactiverse.pgclient.PgPoolOptions
//import io.reactiverse.reactivex.pgclient.PgClient
//import io.vertx.core.Vertx
//
//import io.vertx.core.json.Json
//import kotlinx.coroutines.experimental.rx2.await
//import java.util.*
//
//
//class PostgresPlayerProfileRepository(vertx: Vertx): PlayerProfileRepository {
//
//    private val client: PgClient
//    init{
//        val options = PgPoolOptions()
//                .setPort(5432)
//                .setHost("localhost")
//                .setDatabase("the-db")
//                .setUser("postgres")
//                .setPassword("devPassword")
//                .setMaxSize(5)
//
//// Create the client pool
//        client = PgClient.pool(io.vertx.reactivex.core.Vertx(vertx), options)
//    }
//    override suspend fun readPlayerProfile(id: String): PlayerProfile {
//        val rowset = client.rxQuery("select data from player_profile where data->'uid' = '$id';").await()
//        if(rowset.rowCount() == 1)
//        {
//            val raw_row = rowset.iterator().next()
//            val json = raw_row.getValue("data")
//            print("$json")
//            return PlayerProfile("","","")
//        }
//        else
//        {
//            throw Exception("ayyyyy")
//        }
//    }
//
//    override suspend fun createPlayerProfile(player: PlayerProfile): Boolean {
//       val create = client.rxQuery("INSERT INTO player_profile VALUES ('${UUID.randomUUID()}', '${Json.encode(player)}';").await()
//        val results = create.iterator().next()
//
//        return (results.getValue(0) as Int == 1)
//    }
//
//    override suspend fun updatePlayerProfile(player: PlayerProfile): PlayerProfile {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}