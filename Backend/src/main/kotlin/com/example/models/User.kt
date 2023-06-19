package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersEntity : IntIdTable(name = "users") {
        val username = text("username")
        val messagesNumber = integer("messages_number")
}

data class User(val id: Int? = null, val username: String, val messagesNumber: Int)