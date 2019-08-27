import talham7391.estimation.gamesteps.FinalBidding
import talham7391.estimation.gamesteps.GameStep
import talham7391.estimation.gamesteps.InitialBidding
import talham7391.estimation.transitionToFinalBidding
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEstimation {
    @Test fun testProperlyTransitionsFromInitialToFinalBidding() {
        val players = listOf("1", "2", "3", "4")
        var ib: GameStep = InitialBidding(players, 1)

        ib = ib.bid(3)
        ib = ib.bid(4)
        ib = ib.pass()
        ib = ib.bid(5)
        ib = ib.pass()
        ib = ib.bid(6)
        ib = ib.pass()

        var fb = (ib as InitialBidding).transitionToFinalBidding()

        assertEquals(players[fb.turnOfIndex()], "4")

        fb = fb.bid(2)
        fb = fb.bid(6)
        fb = fb.bid(3)

        val bids = (fb as FinalBidding).getBids()

        assertEquals(6, bids.elementAt(0))
        assertEquals(3, bids.elementAt(1))
        assertEquals(6, bids.elementAt(2))
        assertEquals(2, bids.elementAt(3))
    }
}