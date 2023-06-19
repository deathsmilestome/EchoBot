package com.example.config

import com.example.dao.MessagesDAO
import com.example.dao.UsersDAO
import com.example.service.BotService
import io.ktor.server.config.*
import org.koin.core.module.Module
import org.koin.dsl.module

fun settingsModule(config: ApplicationConfig): Module = module {
    single { Settings(config) }
}

fun userDAOModule(): Module = module {
    single { UsersDAO() }
}

fun serviceModule(): Module = module {
    single { BotService() }
}

fun messageDAOModule(): Module = module {
    single { MessagesDAO() }
}