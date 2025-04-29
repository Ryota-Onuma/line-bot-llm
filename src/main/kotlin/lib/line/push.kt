package line

import kotlinx.serialization.Serializable
import io.ktor.server.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*


const val PUSH_API_ENDPOINT = "https://api.line.me/v2/bot/message/push"

/**
 * LINE にプッシュ送信する汎用関数。
 *
 * @param message 送るテキスト
 * @param accessToken `line.channel_access_token` の値
 * @param roomID `line.room_id` の値
 */
suspend fun pushMessage(
    message: String,
    accessToken: String,
    roomID: String
): HttpResponse {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val reqBody = LineRequestBody(
        to = roomID,
        messages = listOf(
            Message(type = "text", text = message)
        )
    )

    return client.post(PUSH_API_ENDPOINT) {
        contentType(ContentType.Application.Json)
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        setBody(reqBody)
    }
}


@Serializable
data class LineRequestBody(
    val to: String,
    val messages: List<Message>
)

@Serializable
data class LineRequestBodyPayload(
    val to: String,
    val messages: List<Message>
)

@Serializable
data class Message(
    val type: String,
    val text: String
)