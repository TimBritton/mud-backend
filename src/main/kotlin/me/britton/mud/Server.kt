package me.britton.mud

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.netty.util.internal.SocketUtils.accept
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.bridge.BridgeEventType
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import me.britton.mud.http.Routes


class Server : AbstractVerticle()
{

    override fun start(startFuture: Future<Void>?) {
        super.start(startFuture)
        Json.mapper.registerModule(KotlinModule())
        val router = Router.router(vertx)
        val subrouter = Routes(vertx, context)
        // Allow outbound traffic to the news-feed address
        val options2 = BridgeOptions().addOutboundPermitted(PermittedOptions().setAddress("some-address")).addInboundPermitted(PermittedOptions().setAddress("some-address2"))
        var sockJSHandler = SockJSHandler.create(vertx)
        var options = BridgeOptions()
        sockJSHandler.bridge(options2)
        // Serve the static resources
        router.route().handler(StaticHandler.create().setWebRoot("./webroot").setIndexPage("public/index.html"))
        router.route("/game/*").handler(sockJSHandler)
        router.mountSubRouter("/api", subrouter.getRouter())
        vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)

        // Publish a message to the address "news-feed" every second
        vertx.setPeriodic(1000) { t -> vertx.eventBus().send("some-address", "news from the server!\n") }

        vertx.eventBus().consumer<JsonObject>("some-address2"){
            print(it.body() + "\n")

        }

        //okay so when the user connects with the client they need to be told where to listen and where to write their responses


    }
}