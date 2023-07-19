/**
 * A representation of an API server. All requests (except for
 * [Route.NEW_SESSION] are passed to an such server. Each server has a
 * predefined [Language] and [GuessType].
 */
@Serializable
data class Server(
    @Transient val url: String = "", // This field has a default value
    val language: Language = Language.ENGLISH,
    val guessType: GuessType = GuessType.CHARACTER
)

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
            return values().find { it.id == id }
        }
    }
}

/**
 * Server's guess type (referred to as the "subject" in the API). Decides what kind
 * of things server's guesses will represent.
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
            return values().find { it.id == id }
        }
    }
}


class ServerList(val servers: List<Server>) {
    private val iterator = servers.iterator()
    var current: Server? = null
        private set

    fun next(): Boolean {
        return if (iterator.hasNext()) {
            current = iterator.next()
            true
        } else {
            false
        }
    }

    fun getServers(): List<Server> {
        return servers
    }

    fun getRemainingSize(): Int {
        return servers.size - (servers.indexOf(current) + 1)
    }

    fun hasNext(): Boolean {
        return iterator.hasNext()
    }
}
