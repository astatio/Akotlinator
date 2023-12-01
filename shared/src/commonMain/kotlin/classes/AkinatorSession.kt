package classes

import Akinator.timeout
import Guess
import Language
import MissingQuestionException
import NO_MORE_QUESTIONS_STATUS
import PARAMETERS_KEY
import Question
import Server
import Session
import StatusException
import StatusImpl
import StatusImpl.Companion.STATUS_OK
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Duration

class AkinatorSession(
    val server: Server,
    val filterProfanity: Boolean,
    val language: Language
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private var session: Session?
    private var currentStep: Int = 0
    abstract var question: Question
    private var guessCache: ConcurrentList<Guess> = mutableListOf<Guess>() as ConcurrentList<Guess>

    init {
        val questionJson = client.request<JsonObject>(
            NEW_SESSION.createRequest(
                "",
                filterProfanity,
                System.currentTimeMillis(),
                server.url
            )
        )
        val parameters = questionJson[PARAMETERS_KEY]?.jsonObject

        session = getSession(parameters)
        this.question = QuestionImpl.from(parameters?.get("step_information")?.jsonObject, STATUS_OK)
    }

    private fun getSession(parameters: JsonObject?): Session? {
        val session = parameters?.get("identification")?.jsonObject
        return session?.get("signature")?.jsonPrimitive?.content?.toLong()
            ?.let { session["session"]?.jsonPrimitive?.content?.toInt()?.let { it1 -> Session(it, it1) } }
    }


    override fun answer(answer: Answer): Question? {
        guessCache = emptyList<Guess>() as ConcurrentList<Guess>
        val oldQuestion = question
        oldQuestion?.let {
            val newQuestionJson =
                ANSWER.createRequest(unirest, server.getUrl(), filterProfanity, session, it.step, answer.id).getJSON()
            try {
                question = QuestionImpl.from(newQuestionJson.getJSONObject(PARAMETERS_KEY), StatusImpl(newQuestionJson))
            } catch (e: MissingQuestionException) {
                question = null
                return null
            }
            currentStep += 1
        }
        return question
    }

    override fun undoAnswer(): Question? {
        guessCache = emptyList<Guess>() as ConcurrentList<Guess>
        val current = question
        if (current == null || current.step < 1) return null

        val questionJson =
            CANCEL_ANSWER.createRequest(unirest, server.getUrl(), filterProfanity, session, current.step).getJSON()
        question = QuestionImpl.from(questionJson.getJSONObject(PARAMETERS_KEY), StatusImpl(questionJson))
        currentStep -= 1

        return question
    }

    suspend fun getGuesses(): ConcurrentList<Guess> {
        try {
            if (guessCache.isEmpty()) {
                val jsonArray =
                    LIST.createRequest(unirest, server.getUrl(), filterProfanity, session, currentStep).getJSON()
                        .getJSONObject(PARAMETERS_KEY).getJSONArray("elements")
                guessCache = jsonArray.map { GuessImpl.from(it as JSONObject) }.sorted()
            }
            return guessCache
        } catch (e: StatusException) {
            if (e.status.level == Status.Level.ERROR && NO_MORE_QUESTIONS_STATUS.equals(
                    e.status.reason,
                    ignoreCase = true
                )
            ) {
                return emptyList<Guess>() as ConcurrentList<Guess>
            }
            throw e
        }
    }


    private fun getTimeElapsed(): Duration {
        return Clock.System.now() - lastInteraction
    }

    var isRunning = true
        get() = field && getTimeElapsed() < timeout

    val guesses: ConcurrentLinkedQueue<Guess> = ConcurrentLinkedQueue()

    private fun getGuessProbability(): Float {
        return guess.probability
    }

    var readyToGuess = false
        get() = field && getGuessProbability() > 0.85

    fun prepareNextQuestion() {
        //todo: get next question
        question = Question(0.0, 0, 0.0, "test")
    }


    suspend fun answerAndGetNextStep(answer: Int): StepData {
        val answerResponse = apiWrapper.answer(currentStep?.step ?: 0, answer)
        currentStep = answerResponse.nextStep
        return currentStep!!
    }

    fun close() { //todo: this one might need something more
        client.close()
    }
}