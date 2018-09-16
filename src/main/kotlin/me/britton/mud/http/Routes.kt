package me.britton.mud.http

import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.impl.RouteImpl
import kotlinx.coroutines.experimental.launch
import me.britton.mud.account.domain.PlayerProfileRepository
import me.britton.mud.account.domain.mongo.CorMongoPlayerProfileRepo


class Routes(vertx: Vertx, context: Context) {
    val vertx = vertx
    val vertxContext = context
    val playerRepository = CorMongoPlayerProfileRepo(vertx)
    fun getRouter(): Router {
        val router = Router.router(vertx)
        router.post().handler(BodyHandler.create())
        router.get("/auth/player/:uid").produces(HttpHeaderValues.APPLICATION_JSON.toString()).handler(this::getPlayerProfile)
        router.post("/auth/player").handler(this::getCreatePlayerProfile)
        return router
    }


    fun getPlayerProfile(context: RoutingContext) {
        launch(){
            try {
                val player = playerRepository.readPlayerProfile(context.request().getParam("uid"))
                context.response().setStatusCode(200).end(Json.encode(player))
            }
            catch (throwable: Throwable) {
                context.response().setStatusCode(500).end("{\"error\": \"$throwable\"}")
            }
        }
    }

    fun getCreatePlayerProfile(context: RoutingContext) {


    }


}