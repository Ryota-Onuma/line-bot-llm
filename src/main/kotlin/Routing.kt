package `line-bot-llm`

import io.github.cotrin8672.LineWebhook
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.*
import request.*
import line.*
import llm.*
import io.ktor.server.http.content.staticResources
import io.ktor.client.call.*



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
            val config = environment.config
            val lineAccessToken = config.property("line.channel_access_token").getString()
            val cloudflareAccountID = config.property("cloudflare.account_id").getString()
            val cloudflareAccessToken = config.property("cloudflare.access_token").getString()

            // 恥ずかしいので環境変数経由にして隠す
            val defaultPrompt = config.property("cloudflare.llm.defalut_prompt").getString()

            try {
                val webhookRequestBody = call.receive<WebhookRequestBody>()
                println("Parsed content: $webhookRequestBody")
                val events = webhookRequestBody.events
                if (events.size == 0){
                    call.respond(HttpStatusCode.OK)
                    return@post
                }
                val message = events[0].message
                if (message == null) {
                    call.respond(HttpStatusCode.OK)
                    return@post
                }

                val llmResp = askLLM(message.text, defaultPrompt, "@cf/meta/llama-3-8b-instruct", cloudflareAccountID, cloudflareAccessToken)
                val llmRespBody = llmResp.body<String>()
                println(llmRespBody)

                val resp = replyMessage(llmRespBody, lineAccessToken, events[0].replyToken)
                val body = resp.body<String>()
                println("LINE API レスポンス: ${body}")
                
            } catch (e: Exception) {
                println(e)
                val rawRequestBody = call.receiveText()
                println("Parsed content: $rawRequestBody")
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}
