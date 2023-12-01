package classes

import Route.defaultRunChecks
import ServerUnavailableException
import Status
import StatusException
import StatusImpl
import entities.Status
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Request private constructor(
    private val url: String,
    private val jQueryCallbackLength: Int
) {
    class Builder {
        private var url: String = ""
        private var jQueryCallbackLength: Int = 0

        fun url(url: String) = apply { this.url = url }
        fun jQueryCallbackLength(jQueryCallbackLength: Int) = apply { this.jQueryCallbackLength = jQueryCallbackLength }
        fun build() = Request(url, jQueryCallbackLength)
    }

    fun getJson(runChecks: Boolean = true): String {
        val client = HttpClient(CIO)

        val response = runBlocking {
            client.use { client ->
                val httpResponse: HttpResponse = client.get(url)
                httpResponse.bodyAsText()
            }
        }

        val jsonResponse = response.substring(jQueryCallbackLength + 1, response.length - 1)

        return try {
            if (runChecks)
                ensureSuccessful(jsonResponse)
            jsonResponse

        } catch (e: Exception) {
            throw StatusException(StatusImpl(Status.Level.UNKNOWN, "Couldn't parse JSON"))
        }
    }
    private fun ensureSuccessful(response: String) {
        val completion = Json.decodeFromString<Status>(response)
        if (completion.level == Status.Level.ERROR) {
            if (SERVER_DOWN_STATUS_MESSAGE.equals(completion.reason, ignoreCase = true))
                throw ServerUnavailableException(completion)

            throw StatusException(completion)
        }
    }
}