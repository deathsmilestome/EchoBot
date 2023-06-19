package com.example.dao

import com.example.models.User
import com.example.models.UsersEntity
import com.example.factory.DatabaseConnectionFactory
import com.example.mapper.toInsertStatement
import com.example.mapper.toUpdateStatement
import com.example.mapper.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class UsersDAO {

    fun saveUser(user: User) = transaction(DatabaseConnectionFactory.connection()) {
        UsersEntity.insert() { user.toInsertStatement(it) }
    }

    fun updateUser(user: User) = transaction(DatabaseConnectionFactory.connection()) {
        UsersEntity.update({ UsersEntity.username eq user.username }) { user.toUpdateStatement(it) }
    }

    fun getUser(username: String): User? = transaction(DatabaseConnectionFactory.connection()) {
        return@transaction UsersEntity.select { UsersEntity.username eq username }.firstOrNull()?.toUser()
    }
}