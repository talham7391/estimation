package talham7391.estimation.phases

import talham7391.estimation.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestInitialBiddingPhase {

    @Test fun testPhaseWorks() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        p1.bid(2)
        p2.pass()
        p3.bid(4)
        p4.pass()
        p1.bid(5)
        p3.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testFirstPlayerCannotPass() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        assertFails { p1.pass() }
        p1.bid(3)
        p2.pass()
        p3.pass()
        p4.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testCorrectPlayerStarts() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player(); p3.score = 4
        val p4 = Player(); p3.score = 4

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        assertFails { p1.bid(1) }
        assertFails { p2.bid(1) }
        assertFails { p4.bid(1) }

        p3.bid(3)
        p4.bid(4)
        p1.pass()
        p2.pass()
        p3.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testSomeoneWhoPassedCannotBidAgain() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        p1.bid(3)
        p2.pass()
        p3.bid(4)
        assertFails { p2.bid(8) }
        assertFails { p2.pass() }
        p4.bid(5)
        assertFails { p2.bid(8) }
        assertFails { p2.pass() }
        p1.bid(6)
        assertFails { p2.bid(8) }
        assertFails { p2.pass() }
        p3.pass()
        assertFails { p2.bid(8) }
        assertFails { p2.pass() }
        p4.pass()
        assertFails { p2.bid(8) }
        assertFails { p2.pass() }

        assert(ip.isPhaseComplete())
    }

    @Test fun testIllegalActions() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        InitialBiddingPhase(group, true)

        assertFails { p1.declareTrump(Suit.DIAMONDS) }
        assertFails { p1.playCard(Card(Suit.CLUBS, Rank.THREE)) }
        p1.bid(4)
        assertFails { p3.declareTrump(Suit.DIAMONDS) }
        assertFails { p3.playCard(Card(Suit.CLUBS, Rank.THREE)) }
    }

    @Test fun testCannotBidLowerOrSameThanPreviousBids() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        InitialBiddingPhase(group, true)

        p1.bid(1)
        assertFails { p2.bid(1) }
        assertFails { p2.bid(0) }
        p2.bid(4)
        assertFails { p3.bid(3) }
        assertFails { p3.bid(4) }
    }

    @Test fun testCannotBidOutOfBounds() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        assertFails { p1.bid(-1) }
        p1.bid(0)
        assertFails { p2.bid(0) }
        assertFails { p2.bid(14) }
        p2.bid(13)
        assertFails { p3.bid(0) }
        assertFails { p3.bid(13) }
        p3.pass()
        p4.pass()
        p1.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testCannotBidOrPassOutOfTurn() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        p1.bid(4)
        p2.pass()
        assertFails { p1.bid(9) }
        assertFails { p1.pass() }
        p3.pass()
        assertFails { p1.bid(9) }
        assertFails { p1.pass() }
        p4.bid(5)
        p1.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testCannotDoActionsOncePhaseComplete() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        InitialBiddingPhase(group, true)

        p1.bid(1)
        p2.bid(2)
        p3.bid(3)
        p4.bid(4)
        p1.pass()
        p2.pass()
        p3.pass()

        assertFails { p4.bid(5) }
        assertFails { p4.pass() }
        assertFails { p2.bid(5) }
        assertFails { p2.pass() }
    }

    @Test fun testWinningBidAndWinningPlayer() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        p1.bid(4)
        p2.pass()
        p3.bid(5)
        p4.pass()
        p1.bid(6)
        p3.bid(7)
        p1.pass()

        assert(ip.isPhaseComplete())
        assertEquals(p3, ip.getWinningBid().player)
        assertEquals(7, ip.getWinningBid().bid)
    }

    @Test fun testNoWinningBidOrPlayerUntilPhaseComplete() {
        val p1 = Player()
        val p2 = Player(); p2.score = 4
        val p3 = Player()
        val p4 = Player()

        val group = PlayerGroup(p1, p2, p3, p4)
        val ip = InitialBiddingPhase(group, true)

        p2.bid(4)
        assertFails { ip.getWinningBid() }
        p3.pass()
        assertFails { ip.getWinningBid() }
        p4.pass()
        assertFails { ip.getWinningBid() }
        p1.pass()

        ip.getWinningBid()
    }
}
