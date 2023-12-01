package entities

import kotlinx.serialization.Serializable

/**
 * A representation of an API server. All requests (except for
 * [Route.NEW_SESSION]) are passed to such server. Each server has a
 * predefined [Language] and [GuessType].
 */
interface Server {

    /**
     * A language specific to a [Server].
     */
    @Serializable
    enum class Language(val id: String) {
        ARABIC("ar"),
        CHINESE("cn"),
        DUTCH("nl"),
        ENGLISH("en"),
        FRENCH("fr"),
        GERMAN("de"),
        HEBREW("il"),
        INDONESIAN("id"),
        ITALIAN("it"),
        JAPANESE("jp"),
        KOREAN("kr"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        RUSSIAN("ru"),
        SPANISH("es"),
        TURKISH("tr");

        companion object {
            fun getById(id: String): Language? {
                return entries.find { it.id == id }
            }
        }
    }

    /**
     * entities.Server's guess type (referred to as the "subject" or "theme" in the API). Decides what kind
     * of things the [Server]'s [Guess]es will represent. This also affects the [Question]s that will be asked.
     */
    @Serializable
    enum class GuessType(val id: Int) {
        ANIMAL(14),
        MOVIE_TV_SHOW(13),
        PLACE(7),
        CHARACTER(1),
        OBJECT(2);

        companion object {
            fun getById(id: Int): GuessType? {
                return entries.find { it.id == id }
            }
        }
    }

    /**
     * entities.Server's base URL. As the people behind Akinator tend to mix up their servers and
     * the API in general, this should only fetch values from the server-listing endpoint.
     * The host is a valid URL, complete with the path to the endpoint.
     * Example: "https://srv3.akinator.com:9331/ws"
     *
     * @return server's host.
     */
    val url: String

    /**
     * Returns this [Server]'s [Language]. The server will return localized
     * [Question]s and [Guess]es depending on its language.
     *
     * @return server's language.
     */
    val language: Language

    /**
     * Returns this server's [GuessType]. The server will be returning guesses
     * based on that type (also referred to as the subject).
     *
     * @return server's guess type.
     */
    val guessType: GuessType
}