package talham7391.estimation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestTrick {
    @Test fun testTrickWithNoTrump() {
        val plays = listOf(
            Pair("1", Card(Suit.DIAMONDS, Rank.EIGHT)),
            Pair("2", Card(Suit.DIAMONDS, Rank.NINE)),
            Pair("3", Card(Suit.CLUBS, Rank.KING)),
            Pair("4", Card(Suit.SPADES, Rank.EIGHT))
        )

        val trick = Trick(Suit.HEARTS, plays.size)
        for (play in plays) {
            trick.addCard(play.first, play.second)
        }

        val winner = trick.getWinner()
        assertEquals("2", winner)
    }

    @Test fun testTrickWithTrump() {
        val plays = listOf(
            Pair("1", Card(Suit.DIAMONDS, Rank.EIGHT)),
            Pair("2", Card(Suit.DIAMONDS, Rank.NINE)),
            Pair("3", Card(Suit.CLUBS, Rank.KING)),
            Pair("4", Card(Suit.SPADES, Rank.EIGHT))
        )

        val trick = Trick(Suit.SPADES, plays.size)
        for (play in plays) {
            trick.addCard(play.first, play.second)
        }

        val winner = trick.getWinner()
        assertEquals("4", winner)
    }

    @Test fun testTooFewCardsInTrick() {
        val plays = listOf(
            Pair("1", Card(Suit.DIAMONDS, Rank.EIGHT)),
            Pair("2", Card(Suit.DIAMONDS, Rank.NINE)),
            Pair("3", Card(Suit.CLUBS, Rank.KING)),
            Pair("4", Card(Suit.SPADES, Rank.EIGHT))
        )

        val trick = Trick(Suit.SPADES, plays.size + 1)
        for (play in plays) {
            trick.addCard(play.first, play.second)
        }

        assertFailsWith<TrickNotFinished> { trick.getWinner() }
    }

    @Test fun testTooManyCardsInTrick() {
        val plays = listOf(
            Pair("1", Card(Suit.DIAMONDS, Rank.EIGHT)),
            Pair("2", Card(Suit.DIAMONDS, Rank.NINE)),
            Pair("3", Card(Suit.CLUBS, Rank.KING)),
            Pair("4", Card(Suit.SPADES, Rank.EIGHT))
        )

        val trick = Trick(Suit.SPADES, plays.size - 1)
        var idx = 0
        for (play in plays) {
            if (idx == plays.size - 1) {
                assertFailsWith<TrickFull> {
                    trick.addCard(play.first, play.second)
                }
            } else {
                trick.addCard(play.first, play.second)
            }
            idx += 1
        }
    }
}