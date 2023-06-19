package com.example.config

import io.ktor.server.config.*

class Settings(config: ApplicationConfig) {

    val dbUrl = config.property("postgres.url").getString()
    val dbUser = config.property("postgres.user").getString()
    val dbPassword = config.property("postgres.password").getString()
    val dbDriver = config.property("postgres.driverClassName").getString()

    val botUrl = config.property("bot.endpoints.url").getString()
    val botDelayEndpoint = config.property("bot.endpoints.delay").getString()
}