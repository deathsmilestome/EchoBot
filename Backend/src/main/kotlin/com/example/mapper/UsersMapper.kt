package com.example.mapper

import com.example.models.User
import com.example.models.UsersEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement

fun User.toInsertStatement(statement: InsertStatement<Number>): InsertStatement<Number> = statement.also {
    it[UsersEntity.username] = this.username
    it[UsersEntity.messagesNumber] = this.messagesNumber
}

fun User.toUpdateStatement(statement: UpdateStatement): UpdateStatement = statement.also {
    it[UsersEntity.username] = this.username
    it[UsersEntity.messagesNumber] = this.messagesNumber
}

fun ResultRow.toUser(): User = User(
    id = this[UsersEntity.id].value,
    username = this[UsersEntity.username],
    messagesNumber = this[UsersEntity.messagesNumber],
)