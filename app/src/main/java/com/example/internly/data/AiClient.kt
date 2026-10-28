package com.example.internly.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

/** One chat message. fromUser = true for the student, false for the AI. */
data class ChatMessage(val text: String, val fromUser: Boolean)

class AiException(val code: Int, message: String) : IOException(message)

object AiClient {

    // Tried in order. If one is overloaded or times out, the next one is used.
    private val MODELS = listOf(
        AiConfig.MODEL,
        "gemini-2.5-flash",
        "gemini-flash-lite-latest"
    )

    suspend fun ask(systemPrompt: String, history: List<ChatMessage>): Result<String> =
        withContext(Dispatchers.IO) {
            var last: Result<String> = Result.failure(Exception("No attempt made"))
            for (model in MODELS) {
                last = runCatching { callOnce(model, systemPrompt, history) }
                if (last.isSuccess) break

                val e = last.exceptionOrNull()
                val code = (e as? AiException)?.code
                val tryNext = e is SocketTimeoutException || code in listOf(404, 429, 500, 503)
                if (!tryNext) break
                delay(1000)
            }

            val err = last.exceptionOrNull()
            if (err is SocketTimeoutException) {
                Result.failure(Exception("The AI took too long to respond. Please try again."))
            } else if (err is AiException && (err.code == 503 || err.code == 429)) {
                Result.failure(Exception("The AI is busy right now. Please try again in a moment."))
            } else {
                last
            }
        }

    private fun callOnce(model: String, systemPrompt: String, history: List<ChatMessage>): String {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent")
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "POST"
            conn.connectTimeout = 10_000
            conn.readTimeout = 25_000
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("x-goog-api-key", AiConfig.API_KEY)

            // Gemini expects the conversation to start with a user message,
            // so drop the app's greeting bubble if it's first.
            val cleaned = history.dropWhile { !it.fromUser }

            val contents = JSONArray()
            cleaned.forEach { m ->
                contents.put(
                    JSONObject()
                        .put("role", if (m.fromUser) "user" else "model")
                        .put("parts", JSONArray().put(JSONObject().put("text", m.text)))
                )
            }
            val body = JSONObject()
                .put(
                    "system_instruction",
                    JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                )
                .put("contents", contents)

            conn.outputStream.use { it.write(body.toString().toByteArray()) }

            val code = conn.responseCode
            if (code != 200) {
                val err = conn.errorStream?.bufferedReader()?.readText().orEmpty()
                throw AiException(code, "Error $code. $model. $err".take(400))
            }

            val text = conn.inputStream.bufferedReader().readText()
            return JSONObject(text)
                .getJSONArray("candidates").getJSONObject(0)
                .getJSONObject("content").getJSONArray("parts")
                .getJSONObject(0).getString("text")
        } finally {
            conn.disconnect()
        }
    }
}