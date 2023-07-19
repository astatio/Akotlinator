import org.json.JSONObject

@Serializable
data class StatusImpl(
    override val level: Status.Level,
    override val reason: String? = null
) : Status {
    companion object {
        val STATUS_OK = StatusImpl(Status.Level.OK)

        fun fromJson(json: JSONObject): StatusImpl {
            return StatusImpl(
                Status.Level.fromString(json.getString("completion")),
                determineReason(json.getString("completion"))
            )
        }

        fun fromString(status: String): StatusImpl {
            val splitIndex = status.indexOf(" - ")
            return if (splitIndex != -1) {
                val level = Status.Level.fromString(status.substring(0, splitIndex))
                val reason = status.substring(splitIndex + 3)
                StatusImpl(level, reason)
            } else {
                val level = Status.Level.fromString(status)
                StatusImpl(level)
            }
        }

        private fun determineReason(completion: String): String? {
            val reasonSplitIndex = completion.indexOf(" - ")
            return if (reasonSplitIndex != -1) completion.substring(reasonSplitIndex + 3) else null
        }
    }

    @Transient
    private val statusFormat = "%s - %s"

    override fun toString(): String {
        return if (reason == null) level.toString() else String.format(statusFormat, level, reason)
    }
}


interface Status {

    @Serializable
    enum class Level(val apiName: String) {
        OK("OK"),
        WARNING("WARN"),
        ERROR("KO"),
        AKIWRAPPER_ERROR("AW-KO"),
        UNKNOWN("");

        override fun toString(): String = apiName

        companion object {
            fun fromString(completion: String): Level =
                values().find { completion.toUpperCase().startsWith(it.toString()) } ?: UNKNOWN
        }
    }

    val level: Level

    val reason: String?
}

