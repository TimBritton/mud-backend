package me.britton.mud.http

import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
//import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.impl.RouteImpl
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import me.britton.mud.account.domain.PlayerProfile
import me.britton.mud.account.domain.PlayerProfileRepository
import me.britton.mud.account.domain.mongo.CorMongoPlayerProfileRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext



class Routes(vertx: Vertx, context: Context) :  CoroutineScope {
    val vertx = vertx
    val vertxContext = context
    val playerRepository = CorMongoPlayerProfileRepo(vertx)

    override val coroutineContext: CoroutineContext by lazy { context.dispatcher() }
    suspend fun getRouter(): Router {
        val router = Router.router(vertx)
        router.post().handler(BodyHandler.create())
        router.post().handler(CorsHandler.create("*"))
        router.get("/auth/player/:uid").produces(HttpHeaderValues.APPLICATION_JSON.toString()).coroutineHandler(this::getPlayerProfile)
        router.post("/auth/player").coroutineHandler(this::createPlayerProfile)
        return router
    }


     suspend fun getPlayerProfile(context: RoutingContext) {
            try {
                val uid = context.request().getParam("uid")
                System.out.println((uid))
                val player =  playerRepository.readPlayerProfile(uid)
                System.out.println(player)
                context.response().setStatusCode(200).end(Json.encode(player))
            } catch (throwable: Throwable) {
                context.response().setStatusCode(500).end("{\"error\": \"$throwable\"}")
            }
    }

    suspend fun createPlayerProfile(context: RoutingContext) {
        val request = context.bodyAsJson.mapTo(PlayerCreateRequest::class.java)
        val player = PlayerProfile("", request.uid, request.email)

            try {
                val playerProfileCreated = playerRepository.createPlayerProfile(player)

                context.response().setStatusCode(200).end(Json.encode(playerProfileCreated))
            }
            catch (throwable: Throwable)
            {
                context.response().setStatusCode(500).end("{\"error\": \"$throwable\"}")
            }
    }
    /**
     * An extension method for simplifying coroutines usage with Vert.x Web routers
     */
    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }

}


data class PlayerCreateRequest(val uid: String, val email: String )