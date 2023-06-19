package com.example.controller

import com.example.dto.DelayDTO
import com.example.dto.MessageDTO
import com.example.dto.UserDTO
import com.example.service.BotService
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureBotController() {
    val botService: BotService = get()

    routing {
        get("/receiveLastMessage") {
            try {
                call.respond(botService.getLastMessage()) //Возвращаем последнее сообщенеие
            } catch (e: Exception) {
                throw BadRequestException("Bad request")
            }
        }

        post("/getMessagesNumber") {
            val userDTO = call.receive<UserDTO>()
            call.respond(botService.getMessagesNumber(userDTO))
        }

        post("/saveUser") {
            val user = call.receive<UserDTO>()
            botService.saveUser(user) //Сохраняем пользователя в бд
            call.respondText("UserSaved")
        }

        post("/newMessage") {
            val messageDTO = call.receive<MessageDTO>()
            botService.saveMessage(messageDTO)
            call.respondText("newMessageReceived") //Идем в бд, обновляем последнее сообщение и индекс
        }

        put("/updateQueueDelay") {
            try {
                val delayDTO = call.receive<DelayDTO>()
                botService.updateDelay(delayDTO) //Отправляем запрос на апи бота для обновления задержки
                call.respondText("delay updated")
            } catch (e: Exception) {
                throw BadRequestException("Invalid delay")
            }
        }
    }
}