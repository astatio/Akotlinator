import commands.akotlinator.immutable.ApiKey
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.charsets.Charsets.UTF_8
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.Json



class Route private constructor(
    private val path: String,
    private val parameters: String,
    private val filterArguments: List<String>
) {
    class Builder {
        private var path: String = ""
        private val parameters: MutableList<Any> = mutableListOf()
        private val filterArguments: MutableList<String> = mutableListOf()

        fun path(path: String) = apply { this.path = path }
        fun addParameter(parameter: Any) = apply { this.parameters.add(parameter) }
        fun addFilterArgument(filterArgument: String) = apply { this.filterArguments.add(filterArgument) }
        fun build() = Route(path, parameters, filterArguments)
    }

    val NEW_SESSION = Route("1", "$BASE_AKINATOR_URL/new_session?partner=1&player=website-desktop&constraint=ETAT%%3C%%3E%%27AV%%27&{API_KEY}" +
                "&soft_constraint={FILTER}&question_filter={FILTER}&_=%s&urlApiWs=%s",
        "ETAT=%%27EN%%27", "cat=1")


    private val ANSWER = Route("2", "/answer?step=%s&answer=%s", "&question_filter=cat=1")

    private val CANCEL_ANSWER = Route("1", "/cancel_answer?step=%s&answer=-1", "&question_filter=cat=1")

    private val LIST = Route("1", "/list?mode_question=0&step=%s")

    fun createRequest(
        baseUrl: String,
        filterProfanity: Boolean,
        token: Session?
    ): Request {
        if (parameters.size < parametersQuantity)
            throw IllegalArgumentException("Insufficient parameters; Expected $parametersQuantity, got ${parameters.size}")

        val encodedParams = parameters.map { URLEncoder.encode(it.toString(), UTF_8) }.toTypedArray()

        var formattedPath = path

        val matcher = FILTER_ARGUMENT_PATTERN.matches(formattedPath)
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
