import talham7391.estimation.gamesteps.GameStep
import talham7391.estimation.gamesteps.InitialBidding
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestInitialBidding {
    @Test fun testInitialBidding() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 0)

        ib = ib.bid(2)
        assert(!ib.done())

        ib = ib.bid(4)
        assert(!ib.done())

        ib = ib.pass()
        assert(!ib.done())

        ib = ib.pass()
        assert(!ib.done())

        ib = ib.bid(5)
        assert(!ib.done())

        ib = ib.bid(6)
        assert(!ib.done())

        ib = ib.pass()
        assert(ib.done())
    }

    @Test fun testFirstPlayerCannotPass() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 0)
        assertFails { ib.pass() }
        ib = ib.bid(0)
        ib = ib.pass()
        ib = ib.pass()
        ib = ib.pass()
        assert(ib.done())
    }

    @Test fun testTurnCanStartFromMiddle() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 2)
        ib = ib.bid(3)
        ib = ib.pass()
        ib = ib.pass()
        ib = ib.pass()
        assert(ib.done())
    }

    @Test fun testPlayerCannotBidLowerThanHighestBid() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 2)
        ib = ib.bid(5)
        ib = ib.pass()
        assertFails { ib = ib.bid(4) }
        assertFails { ib = ib.bid(5) }
        ib = ib.pass()
        ib = ib.pass()
        assert(ib.done())
    }

    @Test fun testCannotDoActionsAfterBidOver() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 2)
        ib = ib.bid(1)
        ib = ib.bid(2)
        ib = ib.bid(3)
        ib = ib.bid(4)
        ib = ib.bid(5)
        ib = ib.pass()
        ib = ib.pass()
        ib = ib.bid(6)
        ib = ib.pass()
        assertFails { ib = ib.bid(7) }
        assertFails { ib = ib.pass() }
    }

    @Test fun testCorrectPlayerHasTurn() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 1)
        assertEquals(1, ib.turnOfIndex())
        ib = ib.bid(2)
        assertEquals(2, ib.turnOfIndex())
        ib = ib.bid(3)
        assertEquals(3, ib.turnOfIndex())
        ib = ib.pass()
        assertEquals(0, ib.turnOfIndex())
        ib = ib.bid(5)
        assertEquals(1, ib.turnOfIndex())
        ib = ib.pass()
        assertEquals(2, ib.turnOfIndex())
        ib = ib.bid(6)
        assertEquals(0, ib.turnOfIndex())
        ib = ib.pass()
        assertEquals(2, ib.turnOfIndex())
        assert(ib.done())
    }

    @Test fun testCannotBidTooHighOrTooLow() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 1)
        assertFails { ib = ib.bid(-1) }
        assertFails { ib = ib.bid(-5) }
        ib = ib.bid(0)
        assertFails { ib = ib.bid(14) }
        assertFails { ib = ib.bid(15) }
        ib = ib.bid(13)
        assertFails { ib = ib.bid(13) }
        ib = ib.pass()
        assertFails { ib = ib.bid(0) }
        ib = ib.pass()
        assertFails { ib = ib.bid(-1) }
        ib = ib.pass()
        assert(ib.done())
    }

    @Test fun testReturnsCorrentWinningBid() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 1)
        ib = ib.bid(4)
        ib = ib.bid(5)
        ib = ib.pass()
        ib = ib.pass()
        ib = ib.bid(6)
        ib = ib.pass()
        assertEquals(6, (ib as InitialBidding).getWinningBid())
    }

    @Test fun testCannotReturnWinningBidWhenProcessIsNotOver() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 1)
        ib = ib.bid(1)
        ib = ib.bid(2)
        ib = ib.bid(3)
        ib = ib.bid(4)
        assertFails { (ib as InitialBidding).getWinningBid() }
    }
}