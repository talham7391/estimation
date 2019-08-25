package talham7391.estimation

import kotlin.test.Test
import kotlin.test.assertEquals

class TestCard {
    @Test fun testNewDeck() {
        val cards = mutableSetOf<Card>()
        val deck = newDeck()
        deck.forEach { cards.add(it) }
        assertEquals(52, cards.size)
    }

    @Test fun testRandomlyTake() {
        val cards = newDeck().toMutableList()
        val randomlyTaken = cards.randomlyTake(5)
        assertEquals(5, randomlyTaken.size)
        for (card in randomlyTaken) {
            assert(!cards.contains(card))
        }
    }
}