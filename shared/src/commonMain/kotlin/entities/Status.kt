package entities
import kotlinx.serialization.Serializable

/**
 * An interface used to represent API call's completion status.
 */
interface Status {

    /**
     * Indicates the severity of a response from the API server.
     */
    enum class Level(val levelName: String) {
        /**
         * Everything is OK, you may continue normally.
         */
        OK("OK"),

        /**
         * The action has completed, but something minor might have failed/not completed.
         */
        WARNING("WARN"),

        /**
         * The action has not completed due to an error.
         */
        ERROR("KO"),

        /**
         * Unknown status (should not ever occur under normal circumstances), indicates that
         * the status level doesn't match any of the known ones.
         */
        UNKNOWN("");

        override fun toString() = levelName

        companion object {
            fun fromString(completion: String): Level {
                for (iteratedLevel in entries) {
                    if (completion.uppercase().startsWith(iteratedLevel.toString())) {
                        return iteratedLevel
                    }
                }
                return UNKNOWN
            }
        }
    }

    /**
     * A less cryptic form of the status message, which helps you distinguish between a
     * usage error (a problem with your code), a library error (a problem with Akiwrapper
     * that should be reported), a server error (a problem with Akinator's servers that
     * only they can fix), or an unproblematic status.
     */
    enum class Reason {
        /**
         * The status is non-erroneous.
         */
        OK,

        /**
         * The status is non-erroneous and the questions have been exhausted.
         */
        QUESTIONS_EXHAUSTED,

        /**
         * The status is erroneous and likely caused by a bug in the library.
         */
        LIBRARY_FAILURE,

        /**
         * The status is erroneous and likely caused by a problem with Akinator's servers.
         */
        SERVER_FAILURE,

        /**
         * The reason is unknown. Refer to the status message and level for more details.
         */
        UNKNOWN
    }

    /**
     * Returns the level of this status. Status level indicates severity of the status.
     *
     * @return status level
     */
    fun getLevel(): Level

    /**
     * Returns the status message or `null` if it was not specified. Note that the
     * status message is usually pretty cryptic and won't mean much to regular users or
     * anyone not experienced with the Akinator API. If you need something more
     * tangible, use [getReason].
     *
     * @return status message
     */
    fun getMessage(): String?

    /**
     * Returns the status reason, which is picked from a list of predefined values or
     * [Reason.UNKNOWN] if it's meaning/significance are unknown. This generally
     * helps distinguish between a usage error (a problem with your code), a library
     * error (a problem with Akiwrapper that should be reported), a server error (a
     * problem with Akinator's servers that only they can fix), or an unproblematic
     * status.
     *
     * @return status [Reason]
     */
    fun getReason(): Reason
}