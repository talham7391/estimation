package talham7391.estimation.gamedata

import talham7391.estimation.Card
import talham7391.estimation.Player
import talham7391.estimation.Rank
import talham7391.estimation.Suit
import kotlin.test.Test
import kotlin.test.assertEquals

class TestTrick {

    @Test fun testWinnerHigherLeading() {
        val cards = listOf(
            Card(Suit.DIAMONDS, Rank.THREE),
            Card(Suit.SPADES, Rank.TEN),
            Card(Suit.DIAMONDS, Rank.KING),
            Card(Suit.HEARTS, Rank.FOUR)
        )
        val players = listOf(
            Player(),
            Player(),
            Player(),
            Player()
        )
        val trick = Trick(
            (0 until 4).map { Play(players[it], cards[it]) },
            Suit.CLUBS
        )
        assertEquals(players[2], trick.getWinner())
    }

    @Test fun testWinnerBeatLeadingWithTrump() {
        val cards = listOf(
            Card(Suit.CLUBS, Rank.JACK),
            Card(Suit.CLUBS, Rank.FOUR),
            Card(Suit.HEARTS, Rank.TWO),
            Card(Suit.DIAMONDS, Rank.ACE)
        )
        val players = listOf(
            Player(),
            Player(),
            Player(),
            Player()
        )
        val trick = Trick(
            (0 until 4).map { Play(players[it], cards[it]) },
            Suit.HEARTS
        )
        assertEquals(players[2], trick.getWinner())
    }

    @Test fun testWinnerBeatTrumpWithTrump() {
        val cards = listOf(
            Card(Suit.DIAMONDS, Rank.KING),
            Card(Suit.DIAMONDS, Rank.ACE),
            Card(Suit.CLUBS, Rank.TWO),
            Card(Suit.CLUBS, Rank.FOUR)
        )
        val players = listOf(
            Player(),
            Player(),
            Player(),
            Player()
        )
        val trick = Trick(
            (0 until 4).map { Play(players[it], cards[it]) },
            Suit.CLUBS
        )
        assertEquals(players[3], trick.getWinner())
    }

    @Test fun testWinnerLowTrumpWon() {
        val cards = listOf(
            Card(Suit.HEARTS, Rank.TWO),
            Card(Suit.DIAMONDS, Rank.ACE),
            Card(Suit.CLUBS, Rank.ACE),
            Card(Suit.SPADES, Rank.ACE)
        )
        val players = listOf(
            Player(),
            Player(),
            Player(),
            Player()
        )
        val trick = Trick(
            (0 until 4).map { Play(players[it], cards[it]) },
            Suit.HEARTS
        )
        assertEquals(players[0], trick.getWinner())
    }

    @Test fun testAceHighestCard() {
        val cards = listOf(
            Card(Suit.SPADES, Rank.THREE),
            Card(Suit.SPADES, Rank.ACE),
            Card(Suit.SPADES, Rank.TEN),
            Card(Suit.SPADES, Rank.KING)
        )
        val players = listOf(
            Player(),
            Player(),
            Player(),
            Player()
        )
        val trick = Trick(
            (0 until 4).map { Play(players[it], cards[it]) },
            Suit.SPADES
        )
        assertEquals(players[1], trick.getWinner())
    }

}