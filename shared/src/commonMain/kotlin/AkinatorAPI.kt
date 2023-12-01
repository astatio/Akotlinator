import StatusImpl.Companion.STATUS_OK
import classes.GameSessionInitializer
import classes.GameSessionInitializerBuilder
import classes.gameSessionInitializer
import dev.minn.jda.ktx.coroutines.await
import dev.minn.jda.ktx.events.await
import dev.minn.jda.ktx.messages.InlineEmbed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import org.jetbrains.kotlin.com.intellij.util.containers.ConcurrentList
import org.json.JSONObject
import org.mockito.stubbing.Answer
import java.net.URL
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes




@Serializer(forClass = URL::class)
object URLSerializer : KSerializer<URL> {
    override fun serialize(encoder: Encoder, value: URL) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): URL {
        return URL(decoder.decodeString())
    }
}


fun DiscordLocale.toLanguage(): Language? {
    return when (this) {
        DiscordLocale.CHINESE_CHINA -> Language.CHINESE
        DiscordLocale.DUTCH -> Language.DUTCH
        DiscordLocale.ENGLISH_UK, DiscordLocale.ENGLISH_US -> Language.ENGLISH
        DiscordLocale.FRENCH -> Language.FRENCH
        DiscordLocale.GERMAN -> Language.GERMAN
        DiscordLocale.ITALIAN -> Language.ITALIAN
        DiscordLocale.JAPANESE -> Language.JAPANESE
        DiscordLocale.KOREAN -> Language.KOREAN
        DiscordLocale.POLISH -> Language.POLISH
        DiscordLocale.PORTUGUESE_BRAZILIAN -> Language.PORTUGUESE
        DiscordLocale.RUSSIAN -> Language.RUSSIAN
        DiscordLocale.SPANISH -> Language.SPANISH
        DiscordLocale.TURKISH -> Language.TURKISH
        else -> null
    }
}

@Serializable
data class Question(
    @SerialName("questionid") val id: String,
    @SerialName("question") val question: String,
    @SerialName("step") val step: Int,
    @SerialName("infogain") val infogain: Double,
    @SerialName("progression") val progression: Double
)


fun Status.checkMissingQuestion() {
    if (this.level == Status.Level.WARNING && REASON_OUT_OF_QUESTIONS.equals(this.reason, ignoreCase = true))
        throw MissingQuestionException()
}


@Serializable
data class Guess(
    val id: String,
    val name: String,
    @SerialName("description") val description: String? = null,

    @SerialName("absolute_picture_path")
    @Serializable(with = URLSerializer::class)
    val image: URL? = null,

    @SerialName("proba") val probability: Double
)

const val REASON_OUT_OF_QUESTIONS = "no question"
const val NO_MORE_QUESTIONS_STATUS = "elem list is empty"
const val PARAMETERS_KEY = "parameters"
const val FORMAT_QUERYSTRING = "&session=%s&signature=%s"

data class Session(
    val signature: Long,
    val session: Int
) {
    fun queryString() = FORMAT_QUERYSTRING.format(session, signature)
}

object Akotlinator {


    /**
     * Begins the initialization process. This will return a GameSessionInitializer object.
     *
     * Throws ClientRequestException if the request was not successful
     */
    fun initialize() = GameSessionInitializer()

    /**
     * Begins the initialization process. This will return a GameSessionInitializer object.
     *
     * Throws ClientRequestException if the request was not successful
     */
    fun initialize(block: GameSessionInitializerBuilder.() -> Unit) = GameSessionInitializerBuilder().apply(block).build()


    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        expectSuccess = false
        HttpResponseValidator {
            validateResponse { response ->
                val statusCode = response.status.value
                if (statusCode in 400..599) {
                    throw ClientRequestException(response, response.bodyAsText())
                }
            }
        }
    }

    suspend fun start(): StepData {
        return client.get("$baseUrl/start") {
            parameter("language", language.code)
        }.body()
    }

    suspend fun answer(step: Int, answer: Int): AnswerResponse {
        return client.get("$baseUrl/answer") {
            parameter("step", step)
            parameter("answer", answer)
            parameter("language", language.code)
        }.body()
    }

    suspend fun guess(step: Int): List<Guess> {
        return client.get("$baseUrl/guess") {
            parameter("step", step)
            parameter("language", language.code)
        }.body()
    }
}




suspend fun main() {
    val language = Language.ENGLISH // You can replace this with the desired language code
    val apiWrapper = AkinatorAPIWrapper(language = language)
    val session = AkinatorSession(apiWrapper, 2.minutes)

    val firstStep = session.start()
    println("Question: ${firstStep.question}")

    // Answer the first question with 'Yes' (0: Yes, 1: No, 2: Don't know, 3: Probably, 4: Probably not)
    val nextStep = session.answerAndGetNextStep(0)
    println("Next question: ${nextStep.question}")

    // Continue answering questions and guessing as needed

    val guesses = session.getGuesses()
    println("Top guesses:")
    guesses.forEach { guess ->
        println("${guess.name} (${guess.probability * 100}%): ${guess.description}")
    }
}

// This class inherits from Akotlinator
