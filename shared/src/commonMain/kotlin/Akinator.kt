import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.minutes


object Akinator {

    val timeout = 1.minutes // If the user doesn't reply within this amount of time, the session ends.

    suspend fun onMessageReceived(event: MessageReceivedEvent) {
        val channel = event.channel

        val akinator =
            Akotlinator.initialize()

        while (akinator.isRunning) {
            while (!akinator.readyToGuess && akinator.question.question != null) {
                val question = akinator.question // Asks a question to the user. This will return a Question object.
                val qMessage =event.message.replyEmbeds(
                    Embed {
                        title = "Akinator - Question"
                        field {
                            name = "Question #${question.step}"
                            value = question.question.toString()
                        }
                    }
                ).await()
                //TODO: Need to add the buttons

                // Wait for the user to answer the question
                val answer = withTimeoutOrNull(timeout) {
                    channel.jda.await<ButtonInteractionEvent> {
                        (it.message.idLong == qMessage.idLong) && (it.user.idLong == qMessage.author.idLong)
                    }
                }
                akinator.prepareNextQuestion()
            }
            if(akinator.question.question == null && akinator.guesses.isEmpty()) {
                // Akinator ran out of questions, but couldn't guess the character.
            }
            while (akinator.guesses.isNotEmpty()) {

            }
            akinator.shutdown()
        }
    }
}