package `line-bot-llm`

import io.github.cotrin8672.LineWebhook
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {
    routing {
        route("/callback") {
            install(DoubleReceive)
            install(LineWebhook) {
                channelSecret = System.getenv("LINE_CHANNEL_SECRET") ?: "default_secret"
            }
            post {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
