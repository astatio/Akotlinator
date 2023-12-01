import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

@Test
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
    val akiSession = akinator.start(
        onSuccess = {
            println("Akinator session started successfully!")
        },
        onFailure = {
            println("An error occurred while starting the akinator session.")
        }
    )!!
    while (akiSession.isRunning) {
        while (!akinator.readyToGuess && akinator.question.question != null) {
            val question = akinator.question
            println("Question #${question.step}: ${question.question}")
            println("0. Yes")
            println("1. No")
            println("2. Don't know")
            println("3. Probably")
            println("4. Probably not")
            var answer = readln()
            while (answer.toIntOrNull() !in 0..4) {
                println("Invalid input. Please choose an answer:")
                println("0. Yes")
                println("1. No")
                println("2. Don't know")
                println("3. Probably")
                println("4. Probably not")
                answer = readln()
            }
            answer = when (answer.toInt()) {
                0 -> "YES"
                1 -> "NO"
                2 -> "DONT_KNOW"
                3 -> "PROBABLY"
                4 -> "PROBABLY_NOT"
                else -> "DONT_KNOW"
            }
            akinator
            akinator.prepareNextQuestion()
        }
        if (akinator.question.question == null && akinator.guesses.isEmpty()) {
            println("Akinator ran out of questions, but couldn't guess the character.")
            println("Do you wish to try again? (y/n)")
            var continueSession = readln()
            while (continueSession.lowercase() !in listOf("y", "n")) {
                println("Invalid input. Do you wish to try again? (y/n)")
                continueSession = readln()
            }
            continueSession = when (continueSession.lowercase()) {
                "y" -> "true"
                "n" -> "false"
                else -> "true"
            }
            if (continueSession.toBooleanStrict()) {
                akinator.reset()
            } else {
                akinator.stop()
            }
        } else if (akinator.question.question == null && akinator.guesses.isNotEmpty()) {
            println("Akinator guessed the character!")
            println("Your character is ${akinator.guesses.first().name}.")
            println("Is this your character? (y/n)")
            var answer = readln()
            while (answer.lowercase() !in listOf("y", "n")) {
                println("Invalid input. Is this your character? (y/n)")
                answer = readln()
            }
            answer = when (answer.lowercase()) {
                "y" -> "true"
                "n" -> "false"
                else -> "true"
            }
            if (answer.toBooleanStrict()) {
                println("That was fun! Do you wish to try again? (y/n)")
                var continueSession = readln()
                while (continueSession.lowercase() !in listOf("y", "n")) {
                    println("Invalid input. Do you wish to try again? (y/n)")
                    continueSession = readln()
                }
                continueSession = when (continueSession.lowercase()) {
                    "y" -> "true"
                    "n" -> "false"
                    else -> "true"
                }
                if (continueSession.toBooleanStrict()) {
                    akinator.reset()
                } else {
                    akinator.stop()
                }
            } else {
                println("I'm sorry I couldn't guess your character. Do you wish to try again? (y/n)")
                var continueSession = readln()
                while (continueSession.lowercase() !in listOf("y", "n")) {
                    println("Invalid input. Do you wish to try again? (y/n)")
                    continueSession = readln()
                }
                continueSession = when (continueSession.lowercase()) {
                    "y" -> "true"
                    "n" -> "false"
                    else -> "true"
                }
                if (continueSession.toBooleanStrict()) {
                    akinator.reset()
                } else {
                    akinator.stop()
                }
            }
        }
    }
    return
}
