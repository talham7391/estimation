package talham7391.estimation.phases

import talham7391.estimation.*
import talham7391.estimation.gamedata.NewBid
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestFinalBiddingPhase {

    inline fun testPhaseWithPlayers(winningBid: Int, turnIdx: Int, testFunc: (Player, Player, Player, Player, FinalBiddingPhase) -> Unit) {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val players = listOf(p1, p2, p3, p4)
        val p = FinalBiddingPhase(PlayerGroup(p1, p2, p3, p4), NewBid(players[turnIdx], winningBid), true)
        testFunc(p1, p2, p3, p4, p)
    }

    @Test fun testFinalBiddingPhase() = testPhaseWithPlayers(5, 2) { p1, p2, p3, p4, p ->
        p4.bid(2)
        p1.bid(3)
        p2.bid(1)
        assert(p.isPhaseComplete())
        val expectedBids = listOf(NewBid(p1, 3), NewBid(p2, 1), NewBid(p3, 5), NewBid(p4, 2))
        assertEquals(expectedBids.toSet(), p.getFinalBids().toSet())
    }

    @Test fun testCannotBidHigherThanFirstBidder() = testPhaseWithPlayers(3, 3) { p1, p2, _, _, _ ->
        assertFails { p1.bid(4) }
        p1.bid(3)
        assertFails { p2.bid(6) }
        p2.bid(3)
    }

    @Test fun testCannotBidOutOfBounds() = testPhaseWithPlayers(13, 2) { p1, _, _, p4, _ ->
        assertFails { p4.bid(14) }
        assertFails { p4.bid(-1) }
        p4.bid(0)
        assertFails { p1.bid(14) }
        assertFails { p1.bid(-1) }
        p1.bid(13)
    }

    @Test fun testIllegalActions() = testPhaseWithPlayers(3, 0) { _, p2, _, _, _ ->
        assertFails { p2.playCard(Card(Suit.SPADES, Rank.JACK)) }
        assertFails { p2.pass() }
        assertFails { p2.declareTrump(Suit.DIAMONDS) }
        p2.bid(3)
    }

    @Test fun testCorrectPlayerHasTurn() = testPhaseWithPlayers(5, 1) { p1, p2, p3, p4, p ->
        assertEquals(p3, p.getPlayerWithTurn())
        p3.bid(4)
        assertEquals(p4, p.getPlayerWithTurn())
        p4.bid(3)
        assertEquals(p1, p.getPlayerWithTurn())
        p1.bid(2)
        assertEquals(p2, p.getPlayerWithTurn())
        assert(p.isPhaseComplete())
    }

    @Test fun testCannotBidOncePhaseComplete() = testPhaseWithPlayers(1, 0) { p1, p2, p3, p4, p ->
        p2.bid(1)
        p3.bid(1)
        p4.bid(1)
        assert(p.isPhaseComplete())
        assertFails { p1.bid(1) }
        assertFails { p1.bid(2) }
    }

    @Test fun testCannotBidTwice() = testPhaseWithPlayers(1, 0) { _, p2, p3, p4, p ->
        p2.bid(1)
        assertFails { p2.bid(1) }
        p3.bid(1)
        assertFails { p2.bid(1) }
        p4.bid(1)
        assertFails { p2.bid(1) }
        assert(p.isPhaseComplete())
        assertFails { p2.bid(1) }
    }
}