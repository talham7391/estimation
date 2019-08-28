package talham7391.estimation.phases

import talham7391.estimation.*
import talham7391.estimation.gamedata.getWinner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestTrickTakingPhase {

    inline fun testPhaseWithPlayers(turnIdx: Int, trumpSuit: Suit, testFunc: (Player, Player, Player, Player, TrickTakingPhase) -> Unit) {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val players = listOf(p1, p2, p3, p4)
        val p = TrickTakingPhase(PlayerGroup(p1, p2, p3, p4), players[turnIdx], trumpSuit, true)
        testFunc(p1, p2, p3, p4, p)
    }

    @Test fun testTrickTakingPhase() = testPhaseWithPlayers(0, Suit.HEARTS) { p1, p2, p3, p4, p ->
        p1.playCard(Card(Suit.CLUBS, Rank.FOUR))
        p2.playCard(Card(Suit.CLUBS, Rank.KING))
        p3.playCard(Card(Suit.HEARTS, Rank.FIVE))
        p4.playCard(Card(Suit.CLUBS, Rank.TWO))
        assert(p.isPhaseComplete())
        assertEquals(p3, p.getTrick().getWinner())
    }

    @Test fun testCorrectPlayersTurn() = testPhaseWithPlayers(2, Suit.CLUBS) { p1, p2, p3, p4, p ->
        assertEquals(p3, p.getPlayerWithTurn())
        assertFails { p4.playCard(Card(Suit.SPADES, Rank.KING)) }
        p3.playCard(Card(Suit.HEARTS, Rank.FOUR))
        assertEquals(p4, p.getPlayerWithTurn())
        p4.playCard(Card(Suit.SPADES, Rank.KING))
        assertEquals(p1, p.getPlayerWithTurn())
        p1.playCard(Card(Suit.DIAMONDS, Rank.FIVE))
        assertFails { p3.playCard(Card(Suit.HEARTS, Rank.FOUR)) }
        assertEquals(p2, p.getPlayerWithTurn())
        p2.playCard(Card(Suit.CLUBS, Rank.TWO))
        assert(p.isPhaseComplete())
    }

    @Test fun testCannotPlayTwice() = testPhaseWithPlayers(0, Suit.SPADES) { p1, p2, p3, p4, _ ->
        p1.playCard(Card(Suit.HEARTS, Rank.FOUR))
        assertFails { p1.playCard(Card(Suit.HEARTS, Rank.FOUR)) }
        p2.playCard(Card(Suit.HEARTS, Rank.TEN))
        p3.playCard(Card(Suit.SPADES, Rank.ACE))
        p4.playCard(Card(Suit.DIAMONDS, Rank.TEN))
        assertFails { p1.playCard(Card(Suit.HEARTS, Rank.FOUR)) }
    }

    @Test fun testCannotPlaySameCardTwice() = testPhaseWithPlayers(3, Suit.DIAMONDS) { p1, p2, p3, p4, _ ->
        p4.playCard(Card(Suit.HEARTS, Rank.FOUR))
        p1.playCard(Card(Suit.CLUBS, Rank.FOUR))
        assertFails { p2.playCard(Card(Suit.HEARTS, Rank.FOUR)) }
        p2.playCard(Card(Suit.DIAMONDS, Rank.FOUR))
        assertFails { p3.playCard(Card(Suit.CLUBS, Rank.FOUR)) }
        p3.playCard(Card(Suit.SPADES, Rank.FOUR))
    }

    @Test fun testIllegalActions() = testPhaseWithPlayers(1, Suit.CLUBS) { _, p2, p3, _, _ ->
        assertFails { p2.bid(4) }
        assertFails { p2.pass() }
        assertFails { p2.declareTrump(Suit.HEARTS) }
        p2.playCard(Card(Suit.SPADES, Rank.KING))
        assertFails { p3.bid(4) }
        assertFails { p3.pass() }
        assertFails { p3.declareTrump(Suit.HEARTS) }
    }

    @Test fun testCannotPerformActionsAfterCompletion() = testPhaseWithPlayers(0, Suit.CLUBS) { p1, p2, p3, p4, p ->
        p1.playCard(Card(Suit.HEARTS, Rank.TWO))
        p2.playCard(Card(Suit.SPADES, Rank.THREE))
        p3.playCard(Card(Suit.SPADES, Rank.FOUR))
        p4.playCard(Card(Suit.SPADES, Rank.TEN))
        assert(p.isPhaseComplete())
        assertFails { p4.playCard(Card(Suit.DIAMONDS, Rank.FOUR)) }
        assertFails { p1.playCard(Card(Suit.DIAMONDS, Rank.FOUR)) }
    }

    @Test fun testCannotGetDataBeforeCompletion() = testPhaseWithPlayers(0, Suit.CLUBS) { p1, p2, p3, p4, p ->
        assertFails { p.getTrick() }
        p1.playCard(Card(Suit.HEARTS, Rank.TWO))
        assertFails { p.getTrick() }
        p2.playCard(Card(Suit.SPADES, Rank.THREE))
        assertFails { p.getTrick() }
        p3.playCard(Card(Suit.SPADES, Rank.FOUR))
        assertFails { p.getTrick() }
        p4.playCard(Card(Suit.SPADES, Rank.TEN))
        assert(p.isPhaseComplete())
        p.getTrick()
    }
}