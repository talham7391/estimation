import talham7391.estimation.*
import talham7391.estimation.gamedata.InitialBid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestEstimation {

    @Test fun testEstimationGame() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val g = Estimation(PlayerGroup(p1, p2, p3, p4))

        assertEquals(emptyList(), g.getPlayerBids())
        assertEquals(emptyList(), g.initialBiddingHistory())

        p1.bid(2)

        assertEquals(emptyList(), g.getPlayerBids())
        assertEquals(listOf(InitialBid(p1, 2, false)), g.initialBiddingHistory())

        p2.bid(4)
        p3.pass()
        p4.bid(5)
        p1.pass()

        assertEquals(emptyList(), g.getPlayerBids())
        assertEquals(listOf(
            InitialBid(p1, 2, false),
            InitialBid(p2, 4, false),
            InitialBid(p3, 0, true),
            InitialBid(p4, 5, false),
            InitialBid(p1, 0, true)
        ), g.initialBiddingHistory())

        p2.pass()

        assertFails { g.getTrumpSuit() }

        p4.declareTrump(Suit.DIAMONDS)

        assertEquals(Suit.DIAMONDS, g.getTrumpSuit())
        assertEquals(listOf(Pair(p4, 5)), g.getPlayerBids().map { Pair(it.player, it.bid) })

        p1.bid(2)

        assertEquals(listOf(Pair(p4, 5), Pair(p1, 2)), g.getPlayerBids().map { Pair(it.player, it.bid) })

        p2.bid(4)
        p3.bid(2)

        assertEquals(listOf(
            Pair(p4, 5),
            Pair(p1, 2),
            Pair(p2, 4),
            Pair(p3, 2)
        ), g.getPlayerBids().map { Pair(it.player, it.bid) })

        p4.playCard(Card(Suit.CLUBS, Rank.TWO))
    }

    @Test fun testPlayerCannotPlayADifferentSuitIfTheyHaveLeadingSuit() {

    }

    @Test fun testGameScoresAreRememberedForNextGame() {

    }

    @Test fun testPlayerWithHighestScoreBidsFirst() {

    }

    @Test fun testGameEndsAfter13Tricks() {

    }

    @Test fun testPlayerScoresAreComputedProperly() {

    }

    @Test fun testIllegalOperations() {

    }
}