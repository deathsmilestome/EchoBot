package com.example.mapper

import com.example.models.Message
import com.example.models.MessagesEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement


fun Message.toInsertStatement(statement: InsertStatement<Number>): InsertStatement<Number> = statement.also {
    it[MessagesEntity.userId] = this.userId
    it[MessagesEntity.message] = this.message
}

fun ResultRow.toMessage(): Message = Message(
    id = this[MessagesEntity.id].value,
    userId = this[MessagesEntity.userId],
    message = this[MessagesEntity.message],
)