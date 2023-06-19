package com.example.config

import com.example.controller.configureBotController
import com.example.factory.DatabaseConnectionFactory
import com.example.models.MessagesEntity
import com.example.models.UsersEntity
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.startKoin

fun Application.controllers() {
    configureBotController()
}

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
            call.respondText(cause.message!!, status = HttpStatusCode.ServiceUnavailable)
        }
    }
}

fun Application.koin() {
    startKoin {
        modules(
            listOf(
                settingsModule(environment.config),
                userDAOModule(),
                serviceModule(),
                messageDAOModule()
            )
        )
    }
}

fun Application.database() {
    transaction(DatabaseConnectionFactory.connection()) {
        SchemaUtils.create(UsersEntity)
        SchemaUtils.create(MessagesEntity)
    }
}

