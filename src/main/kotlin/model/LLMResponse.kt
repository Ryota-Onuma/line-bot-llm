package model

import kotlinx.serialization.Serializable

@Serializable
data class LLMResponse(
    val result: Result,
    val success: Boolean,
    val errors: List<String>,
    val messages: List<String>
)

@Serializable
data class Result(
    val response: String,
    val usage: Usage
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
) 