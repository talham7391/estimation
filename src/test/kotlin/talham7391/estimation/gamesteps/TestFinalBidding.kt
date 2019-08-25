package talham7391.estimation.gamesteps

import kotlin.test.Test
import kotlin.test.assertFails

class TestFinalBidding {

    @Test fun testFinalBidding() {
        val players = listOf("1", "2", "3", "4")
        var fb: GameStep = FinalBidding(players, 0)
        fb = fb.bid(4)
        fb = fb.bid(3)
        fb = fb.bid(2)
        fb = fb.bid(3)
        assert(fb.done())
    }

    @Test fun testCannotBidHigherThanFirst() {
        val players = listOf("1", "2", "3", "4")
        var fb: GameStep = FinalBidding(players, 3)
        fb = fb.bid(4)
        fb = fb.bid(4)
        assertFails { fb = fb.bid(6) }
        fb = fb.bid(3)
        fb = fb.bid(1)
        assert(fb.done())
    }

    @Test fun testCannotBidOnceDone() {
        val players = listOf("1", "2", "3", "4")
        var fb: GameStep = FinalBidding(players, 3)
        fb = fb.bid(4)
        fb = fb.bid(4)
        fb = fb.bid(4)
        fb = fb.bid(4)
        assert(fb.done())
        assertFails { fb.bid(3) }
    }

    @Test fun testCannotPass() {
        val players = listOf("1", "2", "3", "4")
        var fb: GameStep = FinalBidding(players, 3)
        fb = fb.bid(4)
        assertFails { fb = fb.pass() }
        fb = fb.bid(2)
        fb = fb.bid(2)
        fb = fb.bid(2)
        assertFails { fb.bid(3) }
    }

    @Test fun testCannotBidTooHighOrTooLow() {
        val players = listOf("1", "2", "3", "4")
        var fb: GameStep = FinalBidding(players, 3)
        assertFails { fb = fb.bid(14) }
        assertFails { fb = fb.bid(16) }
        fb = fb.bid(13)
        assertFails { fb = fb.bid(-1) }
        assertFails { fb = fb.bid(-6) }
        fb = fb.bid(0)
        fb = fb.bid(13)
        fb = fb.bid(0)
        assert(fb.done())
    }
}