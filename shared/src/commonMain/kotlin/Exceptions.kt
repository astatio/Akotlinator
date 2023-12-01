import entities.Status

/**
 * An exception indicating that there is no question left to answer or fetch.
 */
class MissingQuestionException : RuntimeException()

/**
 * An exception indicating that no entities.Server could be found for the given
 * combination of Language and GuessType.
 */
class ServerNotFoundException : Exception()


/**
 * An exception indicating that the currently used server has gone offline.
 *
 * @author Marko Zajc
 */
class ServerUnavailableException : StatusException {
    constructor(status: Status) : super(status)
    constructor(status: String) : super(StatusImpl.fromString(status))
}


/**
 * An exception indicating that the server returned an error code ("KO").
 *
 * @property status the problematic status that has been returned
 * @author Marko Zajc
 */
open class StatusException(val status: Status) :
    RuntimeException("${status.level.toString().toUpperCase()} - ${status.reason}")
