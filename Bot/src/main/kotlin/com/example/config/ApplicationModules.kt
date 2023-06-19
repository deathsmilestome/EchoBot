package com.example.config

import com.example.bot.EchoBot
import com.example.controller.configureDelayController
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.core.context.startKoin
import org.koin.ktor.ext.get
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ApplicationModules")

fun Application.serialization() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}

fun Application.exceptions() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            logger.warn(cause.message, cause)
            println("exception")
            call.respondText(cause.message!!, status = HttpStatusCode.ServiceUnavailable)
        }
    }
}

fun Application.controller() {
    configureDelayController()
}

fun Application.bot() {
    val echoBot: EchoBot = get()
    echoBot.run()
}

fun Application.koin() {
    startKoin {
        modules(
            listOf(
                botModule(environment.config),
            )
        )
    }
}