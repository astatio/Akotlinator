package commands.akotlinator.immutable

import Route.BASE_AKINATOR_URL
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.regex.Pattern
import javax.annotation.Nonnull
import kotlin.jvm.JvmStatic


class ApiKey(
    @param:Nonnull private val sessionUid: String,
    @param:Nonnull private val frontAddress: String
) {
    @get:Nonnull
    val queryString: String
        get() = "frontaddr=${URLEncoder.encode(frontAddress, UTF_8)}&uid_ext_session=$sessionUid"

    companion object {
        private const val EXCEPTION_NO_KEY = "Couldn't find the API key!"

        private val API_KEY_PATTERN: Pattern =
            Pattern.compile("var uid_ext_session = '(.*)';\\n.*var frontaddr = '(.*)';")

        @JvmStatic
        fun acquireApiKey(): ApiKey {
            val client = HttpClient(CIO)

            return runBlocking {
                client.use { client ->
                    val response: HttpResponse = client.get("$BASE_AKINATOR_URL/game")
                    val page: String = response.bodyAsText()
                    val matcher = API_KEY_PATTERN.matcher(page)

                    if (matcher.find()) {
                        ApiKey(matcher.group(1), matcher.group(2))
                    } else {
                        throw IllegalStateException(
                            String.format(
                                EXCEPTION_NO_KEY,
                                Base64.getEncoder().encodeToString(page.toByteArray(UTF_8))
                            )
                        )
                    }
                }
            }
        }
    }
}