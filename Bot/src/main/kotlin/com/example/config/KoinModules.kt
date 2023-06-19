package com.example.config

import com.example.bot.EchoBot
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.dsl.module

fun botModule(config: ApplicationConfig): Module = module {
    single {
        EchoBot(
            config.property("bot.token").getString(),
            config.property("bot.delay").getString().toLong(),
            config.property("backend.url").getString()
        )
    }
}