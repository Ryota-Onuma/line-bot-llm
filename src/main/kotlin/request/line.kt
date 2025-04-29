package request

import kotlinx.serialization.Serializable

@Serializable
data class WebhookRequestBody(
    val events: List<Event>,
    val destination: String? = null
)

@Serializable
data class Event(
    val type: String,
    val message: Message? = null,
    val replyToken: String
)

@Serializable
data class Message(
    val type: String,
    val text: String
)

