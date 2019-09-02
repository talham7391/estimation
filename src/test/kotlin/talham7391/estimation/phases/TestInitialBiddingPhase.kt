package talham7391.estimation.phases

import talham7391.estimation.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestInitialBiddingPhase {

    inline fun withPlayers(
        score1: Int = 0,
        score2: Int = 0,
        score3: Int = 0,
        score4: Int = 0,
        func: (SelfProvidingPlayer, SelfProvidingPlayer, SelfProvidingPlayer, SelfProvidingPlayer, InitialBiddingPhase) -> Unit
    ) {
        val p1 = SelfProvidingPlayer(score1)
        val p2 = SelfProvidingPlayer(score2)
        val p3 = SelfProvidingPlayer(score3)
        val p4 = SelfProvidingPlayer(score4)
        val group = PlayerGroup(p1, p2, p3, p4, false)
        val ip = InitialBiddingPhase(group, true)
        func(p1, p2, p3, p4, ip)
    }

    @Test fun testPhaseWorks() = withPlayers { p1, p2, p3, p4, ip ->
        p1.bid(2)
        p2.pass()
        p3.bid(4)
        p4.pass()
        p1.bid(5)
        p3.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testFirstPlayerCannotPass() = withPlayers { p1, p2, p3, p4, ip ->
        assertFails { p1.pass() }
        p1.bid(3)
        p2.pass()
        p3.pass()
        p4.pass()

        assert(ip.isPhaseComplete())
    }

    @Test fun testCorrectPlayerStarts() = withPlayers(0, 0, 4, 4) { p1, p2, p3, p4, ip ->
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

    @Test fun testSomeoneWhoPassedCannotBidAgain() = withPlayers { p1, p2, p3, p4, ip ->
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

    @Test fun testIllegalActions() = withPlayers { p1, _, p3, _, _ ->
        assertFails { p1.declareTrump(Suit.DIAMONDS) }
        assertFails { p1.playCard(Card(Suit.CLUBS, Rank.THREE)) }
        p1.bid(4)
        assertFails { p3.declareTrump(Suit.DIAMONDS) }
        assertFails { p3.playCard(Card(Suit.CLUBS, Rank.THREE)) }
    }

    @Test fun testCannotBidLowerOrSameThanPreviousBids() = withPlayers { p1, p2, p3, _, _ ->
        p1.bid(1)
        assertFails { p2.bid(1) }
        assertFails { p2.bid(0) }
        p2.bid(4)
        assertFails { p3.bid(3) }
        assertFails { p3.bid(4) }
    }

    @Test fun testCannotBidOutOfBounds() = withPlayers { p1, p2, p3, p4, ip ->
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

    @Test fun testCannotBidOrPassOutOfTurn() = withPlayers { p1, p2, p3, p4, ip ->
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

    @Test fun testCannotDoActionsOncePhaseComplete() = withPlayers { p1, p2, p3, p4, _ ->
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

    @Test fun testWinningBidAndWinningPlayer() = withPlayers { p1, p2, p3, p4, ip ->
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

    @Test fun testNoWinningBidOrPlayerUntilPhaseComplete() = withPlayers(0, 4, 0, 0) { p1, p2, p3, p4, ip ->
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

class SelfProvidingPlayer(

    var playerScore: Int = 0

) : Player(), PlayerInfoProvider {

    init {
        setPlayerInfoProvider(this)
    }

    override fun getCardsInHand(player: Player): List<Card> = emptyList()

    override fun getTurnIndex(player: Player): Int = 0

    override fun getScore(player: Player): Int = playerScore
}
