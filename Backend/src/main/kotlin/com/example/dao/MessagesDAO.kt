package com.example.dao

import com.example.dto.MessageDTO
import com.example.factory.DatabaseConnectionFactory
import com.example.mapper.toInsertStatement
import com.example.mapper.toMessage
import com.example.mapper.toUser
import com.example.models.Message
import com.example.models.MessagesEntity
import com.example.models.UsersEntity
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class MessagesDAO {
    fun saveMessage(message: Message) = transaction(DatabaseConnectionFactory.connection()) {
        MessagesEntity.insert { message.toInsertStatement(it) }
    }

    fun selectLast(): MessageDTO = transaction(DatabaseConnectionFactory.connection()) {
        val lastMessage = MessagesEntity.select { MessagesEntity.id greaterEq 0 }.lastOrNull()?.toMessage()
        val lastUser = UsersEntity.select { UsersEntity.id eq lastMessage!!.userId }.firstOrNull()?.toUser()
        return@transaction MessageDTO(lastUser!!.username, lastMessage!!.message)
    }
}