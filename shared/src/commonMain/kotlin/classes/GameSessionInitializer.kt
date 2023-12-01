package classes

import AkinatorSession
import Language

open class GameSessionInitializer(
    val language: Language = Language.ENGLISH,
    val filterProfanity: Boolean = true,
)
{


    fun start(
        onSuccess: (AkinatorSession) -> Unit = {},
        onFailure: (Throwable) -> Unit
    ): AkinatorSession {
        // try to create an AkinatorSession. If it fails, call onFailure. If it succeeds, call onSuccess.
        return try {
            val session = AkinatorSession(
                filterProfanity = filterProfanity,
                language = language
            )
            onSuccess(session)
            session
        } catch (e: Throwable) {
            onFailure(e)
            throw e
        }
    }

}

class GameSessionInitializerBuilder {
    var language: Language = Language.ENGLISH
    var filterProfanity: Boolean = true
    //todo: What else is needed to initialize a game session?

    fun build() = GameSessionInitializer(
        language = language
    )
}
