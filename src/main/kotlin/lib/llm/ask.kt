package llm

import kotlinx.serialization.Serializable
import io.ktor.server.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.*


suspend fun askLLM(
    message: String,
    defaultPrompt: String,
    model: String,
    accountId: String,
    accessToken: String
): HttpResponse {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val messages = listOf(
        Message(role="system", content=defaultPrompt),
        Message(role="user", content=message)
    )

    val requestBody = LLMRequestBody(messages)

    val CLOUDFLARE_API_ENDPOINT = "https://api.cloudflare.com/client/v4/accounts/$accountId/ai/run/$model"

    val resp = client.post(CLOUDFLARE_API_ENDPOINT) {
        contentType(ContentType.Application.Json)
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        setBody(requestBody)
    }

    return resp
}

@Serializable
data class LLMRequestBody(
    val messages: List<Message>
)

@Serializable
data class Message(
    val role: String,
    val content: String
)