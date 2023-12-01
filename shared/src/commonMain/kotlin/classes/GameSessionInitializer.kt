package classes

import AkinatorSession
import Language
import kotlinx.coroutines.withTimeoutOrNull

open class GameSessionInitializer(
    val language: Language = Language.ENGLISH,
    val filterProfanity: Boolean = true,
)
{


    fun start(
        onSuccess: (AkinatorSession) -> Unit = {},
        onFailure: (Throwable) -> Unit
    ): AkinatorSession? {
        onSuccess(AkinatorSession(this))
        return akinatorSession
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
