package talham7391.estimation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class TestGameDriver {

    @Test fun testGameDriver() {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val game = Estimation(p1, p2, p3, p4)
        val driver = GameDriver(game)

        assertEquals(emptyList(), game.initialBiddingHistory())
        driver.doInitialBidding()
        assertNotEquals(emptyList(), game.initialBiddingHistory())

        assertFails { game.getTrumpSuit() }
        driver.doDeclaringTrump()
        game.getTrumpSuit()

        assertEquals(1, game.getPlayerBids().size)
        driver.doFinalBidding()
        assertEquals(4, game.getPlayerBids().size)

        assertEquals(emptyList(), game.getPastTricks())
        driver.doTrick()
        assertEquals(1, game.getPastTricks().size)
        driver.doTrick()
        assertEquals(2, game.getPastTricks().size)
    }
}