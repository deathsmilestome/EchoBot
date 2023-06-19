package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable

object MessagesEntity : IntIdTable(name = "messages") {
    val userId = integer("user_id")
    val message = text("message")
}

data class Message(val id: Int? = null, val userId: Int, val message: String)