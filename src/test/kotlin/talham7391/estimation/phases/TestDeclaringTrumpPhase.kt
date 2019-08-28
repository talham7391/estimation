package talham7391.estimation.phases

import talham7391.estimation.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class TestDeclaringTrumpPhase {

    inline fun testPhaseWithPlayers(turnIdx:Int, testFunc: (Player, Player, Player, Player, DeclaringTrumpPhase) -> Unit) {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val players = listOf(p1, p2, p3, p4)
        val p = DeclaringTrumpPhase(PlayerGroup(p1, p2, p3, p4), players[turnIdx], true)
        testFunc(p1, p2, p3, p4, p)
    }

    @Test fun testDeclaringTrumpPhase() = testPhaseWithPlayers(0) { p1, _, _, _, p ->
        p1.declareTrump(Suit.DIAMONDS)
        assert(p.isPhaseComplete())
        assertEquals(Suit.DIAMONDS, p.getTrumpSuit())
    }

    @Test fun testWinningPlayerCanChooseTrump() = testPhaseWithPlayers(2) { p1, p2, p3, p4, p ->
        assertFails { p1.declareTrump(Suit.CLUBS) }
        assertFails { p2.declareTrump(Suit.CLUBS) }
        assertFails { p4.declareTrump(Suit.CLUBS) }
        p3.declareTrump(Suit.CLUBS)
        assert(p.isPhaseComplete())
    }

    @Test fun testIllegalActions() = testPhaseWithPlayers(0) { p1, _, _, _, _ ->
        assertFails { p1.bid(4) }
        assertFails { p1.pass() }
        assertFails { p1.playCard(Card(Suit.HEARTS, Rank.TEN)) }
    }

    @Test fun testCannotGetTrumpWhenNotComplete() = testPhaseWithPlayers(0) { _, _, _, _, p ->
        assertFails { p.getTrumpSuit() }
    }

    @Test fun testCannotDeclareTrumpMoreThanOnce() = testPhaseWithPlayers(1) { _, p2, _, _, p ->
        p2.declareTrump(Suit.HEARTS)
        assert(p.isPhaseComplete())
        assertFails { p2.declareTrump(Suit.HEARTS) }
        assert(p.isPhaseComplete())
    }

    @Test fun testTurnNeverChanges() = testPhaseWithPlayers(3) { p1, _, p3, p4, p ->
        assertEquals(p4, p.getPlayerWithTurn())
        assertNotEquals(p1, p.getPlayerWithTurn())
        assertNotEquals(p3, p.getPlayerWithTurn())
        p4.declareTrump(Suit.SPADES)
        assertEquals(p4, p.getPlayerWithTurn())
        assertNotEquals(p1, p.getPlayerWithTurn())
        assertNotEquals(p3, p.getPlayerWithTurn())
    }
}