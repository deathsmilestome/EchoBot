package com.example.bot


import com.example.dto.MessageDTO
import com.example.dto.MessagesNumberDTO
import com.example.dto.UserDTO
import com.example.util.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class EchoBot(private val botToken: String, delay: Long, private val backendUrl: String) {

    @Volatile
    private var botDelay = delay
    var userNotRegistered = true

    fun setBotDelay(delay: Long) {
        botDelay = delay
    }

    fun run() {
        val bot = bot {
            token = botToken

            dispatch {
                text {
                    if (!userNotRegistered) {
                        delay(botDelay)
                        sendMessage(MessageDTO(message.from!!.username!!, text))
                        val messageNumber = getMessageNumber(UserDTO(message.from!!.username!!))
                        delay(botDelay)
                        bot.sendMessage(ChatId.fromId(message.chat.id), text + messageNumber)
                    }
                }
                command("start") {
                    if (userNotRegistered) {
                        message.from?.username?.let { saveUser(UserDTO(it)) }
                        userNotRegistered = false
                    }
                }
            }
        }
        bot.startPolling()
    }


    private fun saveUser(userDTO: UserDTO) {
        val client = OkHttpClient()
        val mediaType = MEDIA_TYPE.toMediaType()
        val requestBody = jacksonObjectMapper().writeValueAsString(userDTO).toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl$SAVE_USER_ENDPOINT")
            .method(POST_METHOD, requestBody)
            .build()

        client.newCall(request).execute()
    }

    private fun getMessageNumber(userDTO: UserDTO): Int {
        val client = OkHttpClient()
        val mediaType = MEDIA_TYPE.toMediaType()
        val requestBody = jacksonObjectMapper().writeValueAsString(userDTO).toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl$GET_MESSAGES_NUMBER_ENDPOINT")
            .method(POST_METHOD, requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()
        val messagesNumber = jacksonObjectMapper().readValue<MessagesNumberDTO>(responseBody!!)
        return messagesNumber.messagesNumber
    }

    private fun sendMessage(message: MessageDTO): Boolean {
        val client = OkHttpClient()
        val mediaType = MEDIA_TYPE.toMediaType()
        val requestBody = jacksonObjectMapper().writeValueAsString(message).toRequestBody(mediaType)
        val request = Request.Builder()
            .url("$backendUrl$NEW_MESSAGE_ENDPOINT")
            .method(POST_METHOD, requestBody)
            .build()
        try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            return false
        }
        return true
    }
}