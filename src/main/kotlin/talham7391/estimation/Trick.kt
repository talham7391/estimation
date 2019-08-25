package talham7391.estimation

data class Trick(
    val trumpSuit: Suit,
    val numPlayers: Int
) {
    private val cards = mutableListOf<CardInTrick>()

    fun addCard(playerId: String, card: Card) {
        if (cards.size == numPlayers) {
            throw TrickFull()
        }

        cards.add(CardInTrick(card, playerId))
    }

    fun getWinner(): String {
        if (cards.size != numPlayers) {
            throw TrickNotFinished()
        }

        val highestTrump = cards.filter { it.card.suit == trumpSuit }.maxBy { it.card.rank.ordinal }
        if (highestTrump != null) return highestTrump.playedBy

        val highestLeading = cards.filter { it.card.suit == cards[0].card.suit }.maxBy { it.card.rank.ordinal }
        return highestLeading!!.playedBy
    }

    fun getCards(): Collection<Card> = cards.map { it.card }
}

data class CardInTrick(
    val card: Card,
    val playedBy: String
)

class TrickFull : Exception("This trick is full.")
class TrickNotFinished : Exception("This trick isn't finished yet.")