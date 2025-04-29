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


const val REPLY_API_ENDPOINT = "https://api.line.me/v2/bot/message/reply"

/**
 * LINE にプッシュ送信する汎用関数。
 *
 * @param message 送るテキスト
 * @param accessToken `line.channel_access_token` の値
 * @param replyToken
 */
suspend fun replyMessage(
    message: String,
    accessToken: String,
    replyToken: String
): HttpResponse {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val reqBody = LineReplyRequestBody(
        replyToken=replyToken,
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
data class LineReplyRequestBody(
    val replyToken: String,
    val messages: List<Message>
)
