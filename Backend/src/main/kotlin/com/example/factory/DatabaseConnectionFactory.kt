package com.example.factory

import com.example.config.Settings
import org.jetbrains.exposed.sql.Database
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DatabaseConnectionFactory: KoinComponent {
    companion object: KoinComponent {
        private val settings: Settings = get()

        fun connection(): Database {
            val driverClassName = settings.dbDriver
            val jdbcURL = settings.dbUrl
            return Database.connect(jdbcURL, driverClassName, user = settings.dbUser, password = settings.dbPassword)
        }
    }
}