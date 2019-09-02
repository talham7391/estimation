package talham7391.estimation

import talham7391.estimation.gamedata.Play
import kotlin.test.Test
import kotlin.test.assertEquals

class TestTurnListener {

    val ti = TestInfo()
    val i = Incrementer(ti)

    val p1 = Player()
    val p2 = Player()
    val p3 = Player()
    val p4 = Player()
    val game = Estimation(p1, p2, p3, p4).apply {
        addTurnListener(i)
    }

    init {
        game.notifyPlayerOfTurn()
    }

    @Test fun testGameListener() {

        // testCorrectPlayerNotifiedOnInitialBid

        assertEquals(1, ti.initialBids[p1])
        p1.bid(3)
        assertEquals(1, ti.initialBids[p2])
        p2.bid(4)
        assertEquals(1, ti.initialBids[p3])
        p3.pass()
        assertEquals(1, ti.initialBids[p4])
        p4.bid(5)
        assertEquals(2, ti.initialBids[p1])
        p1.pass()
        assertEquals(2, ti.initialBids[p2])
        p2.pass()

        assertEquals(2, ti.initialBids[p1])
        assertEquals(2, ti.initialBids[p2])
        assertEquals(1, ti.initialBids[p3])
        assertEquals(1, ti.initialBids[p4])

        // testCorrectPlayerNotifiedOnDeclareTrump

        assertEquals(0, ti.declareTrumps.getOrDefault(p1, 0))
        assertEquals(0, ti.declareTrumps.getOrDefault(p1, 0))
        assertEquals(0, ti.declareTrumps.getOrDefault(p1, 0))
        assertEquals(1, ti.declareTrumps[p4])

        p4.declareTrump(Suit.DIAMONDS)

        // testCorrectPlayerNotifiedOnFinalBid

        assertEquals(1, ti.finalBids[p1])
        p1.bid(3)
        assertEquals(1, ti.finalBids[p2])
        p2.bid(2)
        assertEquals(1, ti.finalBids[p3])
        p3.bid(4)

        assertEquals(1, ti.finalBids[p1])
        assertEquals(1, ti.finalBids[p2])
        assertEquals(1, ti.finalBids[p3])
        assertEquals(0, ti.finalBids.getOrDefault(p4, 0))

        // testCorrectPlayerNotifiedOnTrickTaking

//        assertEquals(1, ti.playCards[p4])
//        p4.playAnyCardInHand()
//        assertEquals(1, ti.playCards[p1])
//        p1.playAnyCardInHand()
//        assertEquals(1, ti.playCards[p2])
//        p2.playAnyCardInHand()
//        assertEquals(1, ti.playCards[p3])
//        p3.playAnyCardInHand()

        // this next part is super flaky

//        assertEquals(2, ti.playCards[p4])
//        p4.playAnyCardInHand()
//        assertEquals(2, ti.playCards[p1])
//        p1.playAnyCardInHand()
//        assertEquals(2, ti.playCards[p2])
//        p2.playAnyCardInHand()
//        assertEquals(2, ti.playCards[p3])
//        p3.playAnyCardInHand()
    }

    @Test fun testCorrectTrickSoFarIsReturned() {

    }

    data class TestInfo(
        var initialBids: MutableMap<Player, Int> = mutableMapOf(),
        val declareTrumps: MutableMap<Player, Int> = mutableMapOf(),
        val finalBids: MutableMap<Player, Int> = mutableMapOf(),
        val playCards: MutableMap<Player, Int> = mutableMapOf()
    )

    class Incrementer(val ti: TestInfo) : TurnListener {

        override fun onPlayersTurnToInitialBid(player: Player) {
            ti.initialBids[player] = (ti.initialBids[player] ?: 0) + 1
        }

        override fun onPlayersTurnToDeclareTrump(player: Player) {
            ti.declareTrumps[player] = (ti.declareTrumps[player] ?: 0) + 1
        }

        override fun onPlayersTurnToFinalBid(player: Player) {
            ti.finalBids[player] = (ti.finalBids[player] ?: 0) + 1
        }

        override fun onPlayersTurnToPlayCard(player: Player, trickSoFar: List<Play>) {
            ti.playCards[player] = (ti.playCards[player] ?: 0) + 1
        }
    }
}