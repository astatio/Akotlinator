package entities

interface Guess : Comparable<Guess> {

    val name: String

    val probability: Double

    val description: String?

    val image: String?

    val isExplicit: Boolean
}

class GuessImpl(
    override val name: String,
    override val description: String?,
    override val image: String?,
    override val probability: Double,
    override val isExplicit: Boolean
) : Guess {

    companion object {
        fun fromJson(json: JSONObject): GuessImpl {
            val name = json.getString("name")
            val description = json.getString("description").let { if (it == "-") null else it }
            val image = try {
                if (json.getString("picture_path") == "none.jpg") null else URL(json.getString("absolute_picture_path"))
            } catch (e: MalformedURLException) {
                null
            }
            val probability = json.getDouble("proba")
            val explicit = json.getInt("corrupt") == 1
            return GuessImpl(name, description, image, probability, explicit)
        }
    }

    override fun compareTo(other: Guess): Int {
        return probability.compareTo(other.probability)
    }
}