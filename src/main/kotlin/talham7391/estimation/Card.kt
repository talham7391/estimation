package talham7391.estimation

import kotlin.random.Random

enum class Suit {
    HEARTS,
    SPADES,
    CLUBS,
    DIAMONDS,
}

enum class Rank { // order of rank is important
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE,
}

data class Card(
    val suit: Suit,
    val rank: Rank
)

fun newDeck(): Collection<Card> {
    val deck = mutableListOf<Card>()
    for (suit in Suit.values()) {
        for (rank in Rank.values()) {
            deck.add(Card(suit, rank))
        }
    }
    return deck
}

fun MutableList<Card>.randomlyTake(num: Int): Collection<Card> {
    val cards = mutableListOf<Card>()
    repeat(num) {
        if (size > 0) {
            val idx = Random.nextInt(size)
            val card = removeAt(idx)
            cards.add(card)
        }
    }
    return cards
}