package com.example.controller

import com.example.dto.DelayDTO
import com.example.bot.EchoBot
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get


fun Application.configureDelayController() {
    val echoBot: EchoBot = get()

    routing {
        put("/updateBotDelay") {
            try {
                val delayDTO = call.receive<DelayDTO>().delay
                if (delayDTO < 0)
                echoBot.setBotDelay(delayDTO)
            }
            catch(e: Exception) {
                throw BadRequestException("Invalid delay", e)
            }
        }
    }
}