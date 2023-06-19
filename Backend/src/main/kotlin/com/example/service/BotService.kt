package com.example.service

import com.example.config.Settings
import com.example.dao.MessagesDAO
import com.example.dto.MessageDTO
import com.example.dao.UsersDAO
import com.example.dto.DelayDTO
import com.example.dto.MessagesNumberDTO
import com.example.dto.UserDTO
import com.example.models.Message
import com.example.models.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class BotService: KoinComponent {
    private val usersDAO: UsersDAO = get()
    private val messagesDAO: MessagesDAO = get()
    private val settings: Settings = get()


    fun saveUser(userDTO: UserDTO) {
        val checkUser = usersDAO.getUser(userDTO.username)
        if (checkUser == null) {
            val user = User(username = userDTO.username, messagesNumber = 0)
            usersDAO.saveUser(user)
        }
    }

    fun saveMessage(message: MessageDTO) {
        val user = usersDAO.getUser(message.username) ?: throw Exception("no user")
        usersDAO.updateUser(User(user.id, user.username, user.messagesNumber + 1))
        messagesDAO.saveMessage(Message(userId = user.id!!, message = message.text))
    }

    fun getLastMessage(): MessageDTO {
        return messagesDAO.selectLast()
    }

    fun getMessagesNumber(user: UserDTO): MessagesNumberDTO {
        val messagesNumber = usersDAO.getUser(user.username)!!.messagesNumber
        return MessagesNumberDTO(messagesNumber)
    }

    fun updateDelay(delayDTO: DelayDTO) {
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jacksonObjectMapper().writeValueAsString(delayDTO).toRequestBody(mediaType)
        val request = Request.Builder()
            .url("${settings.botUrl}/${settings.botDelayEndpoint}")
            .method("PUT", requestBody)
            .build()

        client.newCall(request).execute()
    }
}