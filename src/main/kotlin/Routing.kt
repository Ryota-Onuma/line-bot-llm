package com.example

import io.github.cotrin8672.LineWebhook
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.model.*

fun Application.configureRouting() {
    install(DoubleReceive)
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/tasks") {
            println("/tasks called")
            call.respond(
                listOf(
                    Task("cleaning", "Clean the house", Priority.Low),
                    Task("gardening", "Mow the lawn", Priority.Medium),
                    Task("shopping", "Buy the groceries", Priority.High),
                    Task("painting", "Paint the fence", Priority.Medium)
                )
            )
        }
        post("/line") {
             println("/line called")
        }
    }
}
