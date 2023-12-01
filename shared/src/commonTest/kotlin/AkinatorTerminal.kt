import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.minutes


fun main() {
    //Start a terminal akinator session
    println("Welcome to Akinator in the terminal!")
    println("Please choose a language:")
    println("1. English")
    println("2. Spanish")
    var language = readln()
    while (language.toIntOrNull() !in 1..2) {
        println("Invalid input. Please choose a language:")
        println("1. English")
        println("2. Spanish")
        language = readln()
    }
    language = when (language.toInt()) {
        1 -> "ENGLISH"
        2 -> "SPANISH"
        else -> "ENGLISH"
    }
    println("You chose $language.")
    println("Do you wish to filter profanity? (y/n)")
    var filterProfanity = readln()
    while (filterProfanity.lowercase() !in listOf("y", "n")) {
        println("Invalid input. Do you wish to filter profanity? (y/n)")
        filterProfanity = readln()
    }
    filterProfanity = when (filterProfanity.lowercase()) {
        "y" -> "true"
        "n" -> "false"
        else -> "true"
    }
    println("You chose $filterProfanity.")
    println("Starting an akinator session...")
    val akinator = Akotlinator.initialize {
        this.language = Language.valueOf(language)
        this.filterProfanity = filterProfanity.toBooleanStrict()
    }
    akinator.start(
        onSuccess = {
            println("Akinator session started successfully!")
        },
        onFailure = {
            println("An error occurred while starting the akinator session.")
        }
    )
    return
}
