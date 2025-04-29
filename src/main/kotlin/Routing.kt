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
import com.example.request.*
import io.ktor.server.http.content.staticResources

fun Application.configureRouting() {
    install(DoubleReceive)

    routing {
        staticResources("/task-ui", "task-ui")
        get("/") {
            call.respondText("Hello World!")
        }
        get("/tasks") {
            println("/tasks called")
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text=tasks.tasksAsTable()
            )
        }
        get("/tasks/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            println(priorityAsText)
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)
                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )
            } catch(ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/tasks") {
            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: ""
            )

            if (params.toList().any { it.isEmpty() }) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                val priority = Priority.valueOf(params.third)
                TaskRepository.addTask(
                    Task(
                        params.first,
                        params.second,
                        priority
                    )
                )

                call.respond(HttpStatusCode.NoContent)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        post("/line") {
            println("/line called")
            val requestBody = call.receiveText()
            println("Request body: $requestBody")
            val formContent = call.receive<LineRequest>()
            println("Parsed content: $formContent")
            call.respond(HttpStatusCode.OK)
       }
    }
}
