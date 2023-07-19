import commands.akotlinator.immutable.ApiKey
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import java.util.regex.Pattern

object Route {

    const val BASE_AKINATOR_URL = "https://en.akinator.com"
    private const val SERVER_DOWN_STATUS_MESSAGE = "server down"
    private val FILTER_ARGUMENT_PATTERN = Pattern.compile("\\{FILTER}")

    var defaultRunChecks = true

    private val NEW_SESSION = Route(
        1,
        "$BASE_AKINATOR_URL/new_session?partner=1&player=website-desktop&constraint=ETAT%%3C%%3E%%27AV%%27&{API_KEY}" +
                "&soft_constraint={FILTER}&question_filter={FILTER}&_=%s&urlApiWs=%s",
        "ETAT=%%27EN%%27", "cat=1"
    )

    private val ANSWER = Route(2, "/answer?step=%s&answer=%s", "&question_filter=cat=1")

    private val CANCEL_ANSWER = Route(1, "/cancel_answer?step=%s&answer=-1", "&question_filter=cat=1")

    private val LIST = Route(1, "/list?mode_question=0&step=%s")

    private class Route(
        private val parametersQuantity: Int,
        private val path: String,
        private vararg val filterArguments: String
    ) {

        fun createRequest(
            baseUrl: String,
            filterProfanity: Boolean,
            token: Session?,
            vararg parameters: Any
        ): Request {
            if (parameters.size < parametersQuantity)
                throw IllegalArgumentException("Insufficient parameters; Expected $parametersQuantity, got ${parameters.size}")

            val encodedParams = parameters.map { URLEncoder.encode(it.toString(), UTF_8) }.toTypedArray()

            var formattedPath = path

            val matcher = FILTER_ARGUMENT_PATTERN.matcher(formattedPath)
            val sb = StringBuffer()
            for (i in filterArguments.indices) {
                if (matcher.find()) {
                    matcher.appendReplacement(sb, if (filterProfanity) filterArguments[i] else "")
                }
            }
            matcher.appendTail(sb)
            formattedPath = sb.toString()
            formattedPath = formattedPath.replace(
                "{API_KEY}",
                ApiKey.acquireApiKey().queryString.replace("%", "%%")
            )
            formattedPath = String.format(formattedPath, *encodedParams)

            val jQueryCallback = "jQuery331023608747682107778_" + System.currentTimeMillis()
            formattedPath = "$formattedPath&callback=$jQueryCallback"

            if (token != null)
                formattedPath += token.queryString()

            return Request(baseUrl + formattedPath, jQueryCallback.length)
        }
    }

    class Request(private val url: String, private val jQueryCallbackLength: Int) {

        fun getJson(runChecks: Boolean = defaultRunChecks): String {
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
}

