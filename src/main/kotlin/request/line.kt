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
    val source: Source? = null,
    val timestamp: Long? = null
)

@Serializable
data class Message(
    val type: String,
    val text: String
)

@Serializable
data class Source(
    val type: String,
    val userId: String? = null
)
