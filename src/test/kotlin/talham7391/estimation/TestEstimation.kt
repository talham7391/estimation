import talham7391.estimation.*
import talham7391.estimation.gamedata.Play
import talham7391.estimation.gamedata.Trick
import talham7391.estimation.gamedata.getWinner
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class TestEstimation {

    inline fun withGame(func: (Estimation, GameDriver) -> Unit) {
        val p1 = Player()
        val p2 = Player()
        val p3 = Player()
        val p4 = Player()
        val game = Estimation(p1, p2, p3, p4)
        val driver = GameDriver(game)
        func(game, driver)
    }

    @Test fun testPlayerCannotPlayADifferentSuitIfTheyHaveLeadingSuit() = withGame { game, driver ->
        driver.forwardToTrickTaking()
        val card = game.getPlayerWithTurn().getCardsInHand()[0]
        game.getPlayerWithTurn().playCard(card)

        while (true) {
            val player = game.getPlayerWithTurn()
            if (player.getCardsInHand().any { it.suit == card.suit }) {
                val notLeadingSuit = player.getCardsInHand().find { it.suit != card.suit }
                if (notLeadingSuit == null) {
                    player.playAnyCardInHand()
                } else {
                    assertFails { player.playCard(notLeadingSuit) }
                    break
                }
            }
        }
    }

    @Test fun testPlayerCannotPlayACardThatHasAlreadyBeenPlayed() = withGame { game, driver ->
        driver.forwardToTrickTaking()
        val firstPlayer = game.getPlayerWithTurn()
        val card = firstPlayer.getCardsInHand()[0]

        firstPlayer.playCard(card)

        assertFails { firstPlayer.playCard(card) }
        assertFails { game.getPlayerWithTurn().playCard(card) }

        game.getPlayerWithTurn().playAnyCardInHandOfSuitIfPossible(card.suit)
        game.getPlayerWithTurn().playAnyCardInHandOfSuitIfPossible(card.suit)
        game.getPlayerWithTurn().playAnyCardInHandOfSuitIfPossible(card.suit)

        // new trick

        assertFails { game.getPlayerWithTurn().playCard(card) }
        game.getPlayerWithTurn().playAnyCardInHand()
    }

    @Test fun testPlayerDoesNotHaveAccessToACardOnceTheyHavePlayedIt() = withGame { game, driver ->
        driver.forwardToTrickTaking()
        val firstPlayer = game.getPlayerWithTurn()
        val firstCard = firstPlayer.getCardsInHand()[2]

        assert(firstPlayer.getCardsInHand().contains(firstCard))
        firstPlayer.playCard(firstCard)
        assert(!firstPlayer.getCardsInHand().contains(firstCard))

        val secondPlayer = game.getPlayerWithTurn()

        assertEquals(13, secondPlayer.getCardsInHand().size)
        secondPlayer.playAnyCardInHandOfSuitIfPossible(firstCard.suit)
        assertEquals(12, secondPlayer.getCardsInHand().size)
    }

    @Test fun testPlayerWhoWonTheTrickStartsTheNextTrick() = withGame { game, driver ->
        driver.forwardToTrickTaking()

        repeat(12) {
            val p1 = game.getPlayerWithTurn()
            val c1 = p1.playAnyCardInHand()!!

            val p2 = game.getPlayerWithTurn()
            val c2 = p2.playAnyCardInHandOfSuitIfPossible(c1.suit)!!

            val p3 = game.getPlayerWithTurn()
            val c3 = p3.playAnyCardInHandOfSuitIfPossible(c1.suit)!!

            val p4 = game.getPlayerWithTurn()
            val c4 = p4.playAnyCardInHandOfSuitIfPossible(c1.suit)!!

            val trick = Trick(listOf(Play(p1, c1), Play(p2, c2), Play(p3, c3), Play(p4, c4)), game.getTrumpSuit())
            assertEquals(trick.getWinner(), game.getPlayerWithTurn())
        }
    }

    @Test fun testReturnsTheCorrectPastTricks() = withGame { game, driver ->
        driver.forwardToTrickTaking()

        val tricks = mutableListOf<Trick>()

        repeat(13) {
            val trumpSuit = game.getTrumpSuit()
            val p1 = game.getPlayerWithTurn()
            val c1 = p1.playAnyCardInHand()!!
            val play1 = Play(p1, c1)

            val plays = (0 until 3).map {
                val p = game.getPlayerWithTurn()
                val c = p.playAnyCardInHandOfSuitIfPossible(c1.suit)!!
                Play(p, c)
            }

            tricks.add(Trick(listOf(play1, plays[0], plays[1], plays[2]), trumpSuit))
        }

        assertEquals(tricks, game.getPastTricks())
    }

    @Test fun testGameEndsAfter13Tricks() = withGame { _, driver ->
        repeat(10) {
            driver.doInitialBidding()
            driver.doDeclaringTrump()
            driver.doFinalBidding()
            repeat(13) {
                driver.doTrick()
            }
        }
    }

    @Test fun testPlayerScoresAreComputedProperly() = withGame { game, _ ->
        val p1 = game.getPlayerWithTurn()
        val p2 = game.playerGroup.playerAfter(p1)
        val p3 = game.playerGroup.playerAfter(p2)
        val p4 = game.playerGroup.playerAfter(p3)

        val bids = mutableMapOf<Player, Int>()
        bids[p1] = 5
        bids[p2] = 4
        bids[p3] = 1
        bids[p4] = 0

        p1.bid(bids[p1]!!)
        p2.pass()
        p3.pass()
        p4.pass()

        p1.declareTrump(Suit.DIAMONDS)

        p2.bid(bids[p2]!!)
        p3.bid(bids[p3]!!)
        p4.bid(bids[p4]!!)

        val cards1 = listOf(
            Rank.ACE of Suit.DIAMONDS,
            Rank.KING of Suit.DIAMONDS,
            Rank.JACK of Suit.DIAMONDS,
            Rank.NINE of Suit.DIAMONDS,
            Rank.TWO of Suit.DIAMONDS,

            Rank.ACE of Suit.CLUBS,
            Rank.NINE of Suit.CLUBS,
            Rank.EIGHT of Suit.CLUBS,

            Rank.ACE of Suit.HEARTS,
            Rank.SEVEN of Suit.HEARTS,
            Rank.SIX of Suit.HEARTS,

            Rank.SIX of Suit.SPADES,
            Rank.FIVE of Suit.SPADES
        )
        game.setCardsInHand(p1, cards1)

        val cards2 = listOf(
            Rank.QUEEN of Suit.DIAMONDS,
            Rank.EIGHT of Suit.DIAMONDS,

            Rank.JACK of Suit.CLUBS,
            Rank.SIX of Suit.CLUBS,

            Rank.KING of Suit.HEARTS,
            Rank.QUEEN of Suit.HEARTS,
            Rank.JACK of Suit.HEARTS,
            Rank.FOUR of Suit.HEARTS,
            Rank.THREE of Suit.HEARTS,

            Rank.ACE of Suit.SPADES,
            Rank.KING of Suit.SPADES,
            Rank.FOUR of Suit.SPADES,
            Rank.TWO of Suit.SPADES
        )
        game.setCardsInHand(p2, cards2)

        val cards3 = listOf(
            Rank.TEN of Suit.DIAMONDS,
            Rank.FOUR of Suit.DIAMONDS,
            Rank.THREE of Suit.DIAMONDS,

            Rank.KING of Suit.CLUBS,
            Rank.QUEEN of Suit.CLUBS,
            Rank.SEVEN of Suit.CLUBS,
            Rank.FIVE of Suit.CLUBS,
            Rank.FOUR of Suit.CLUBS,

            Rank.TEN of Suit.HEARTS,
            Rank.FIVE of Suit.HEARTS,

            Rank.QUEEN of Suit.SPADES,
            Rank.JACK of Suit.SPADES,
            Rank.THREE of Suit.SPADES
        )
        game.setCardsInHand(p3, cards3)

        val cards4 = listOf(
            Rank.SEVEN of Suit.DIAMONDS,
            Rank.SIX of Suit.DIAMONDS,
            Rank.FIVE of Suit.DIAMONDS,

            Rank.TEN of Suit.CLUBS,
            Rank.THREE of Suit.CLUBS,
            Rank.TWO of Suit.CLUBS,

            Rank.NINE of Suit.HEARTS,
            Rank.EIGHT of Suit.HEARTS,
            Rank.TWO of Suit.HEARTS,

            Rank.TEN of Suit.SPADES,
            Rank.NINE of Suit.SPADES,
            Rank.EIGHT of Suit.SPADES,
            Rank.SEVEN of Suit.SPADES
        )
        game.setCardsInHand(p4, cards4)

        val deck = cards1 + cards2 + cards3 + cards4
        assertEquals(52, deck.size)
        for ((i, c1) in deck.withIndex()) {
            for ((j, c2) in deck.withIndex()) {
                if (i != j) {
                    assertNotEquals(c1, c2)
                }
            }
        }

        val wins = mutableMapOf<Player, Int>()

        p1.playCard(Rank.ACE of Suit.CLUBS)
        p2.playCard(Rank.JACK of Suit.CLUBS)
        p3.playCard(Rank.FOUR of Suit.CLUBS)
        p4.playCard(Rank.TWO of Suit.CLUBS)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.playCard(Rank.ACE of Suit.HEARTS)
        p2.playCard(Rank.THREE of Suit.HEARTS)
        p3.playCard(Rank.FIVE of Suit.HEARTS)
        p4.playCard(Rank.TWO of Suit.HEARTS)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.playCard(Rank.SIX of Suit.SPADES)
        p2.playCard(Rank.KING of Suit.SPADES)
        p3.playCard(Rank.THREE of Suit.SPADES)
        p4.playCard(Rank.SEVEN of Suit.SPADES)

        p2.let { wins[it] = (wins[it] ?: 0) + 1 }

        p2.playCard(Rank.QUEEN of Suit.DIAMONDS)
        p3.playCard(Rank.THREE of Suit.DIAMONDS)
        p4.playCard(Rank.FIVE of Suit.DIAMONDS)
        p1.playCard(Rank.TWO of Suit.DIAMONDS)

        p2.let { wins[it] = (wins[it] ?: 0) + 1 }

        p2.playCard(Rank.SIX of Suit.CLUBS)
        p3.playCard(Rank.KING of Suit.CLUBS)
        p4.playCard(Rank.THREE of Suit.CLUBS)
        p1.playCard(Rank.NINE of Suit.CLUBS)

        p3.let { wins[it] = (wins[it] ?: 0) + 1 }

        p3.playCard(Rank.QUEEN of Suit.CLUBS)
        p4.playCard(Rank.TEN of Suit.CLUBS)
        p1.playCard(Rank.EIGHT of Suit.CLUBS)
        p2.playCard(Rank.EIGHT of Suit.DIAMONDS)

        p2.let { wins[it] = (wins[it] ?: 0) + 1 }

        p2.playCard(Rank.ACE of Suit.SPADES)
        p3.playCard(Rank.JACK of Suit.SPADES)
        p4.playCard(Rank.TEN of Suit.SPADES)
        p1.playCard(Rank.FIVE of Suit.SPADES)

        p2.let { wins[it] = (wins[it] ?: 0) + 1 }

        p2.playCard(Rank.KING of Suit.HEARTS)
        p3.playCard(Rank.TEN of Suit.HEARTS)
        p4.playCard(Rank.EIGHT of Suit.HEARTS)
        p1.playCard(Rank.SIX of Suit.HEARTS)

        p2.let { wins[it] = (wins[it] ?: 0) + 1 }

        p2.playCard(Rank.QUEEN of Suit.HEARTS)
        p3.playCard(Rank.FOUR of Suit.DIAMONDS)
        p4.playCard(Rank.NINE of Suit.HEARTS)
        p1.playCard(Rank.SEVEN of Suit.HEARTS)

        p3.let { wins[it] = (wins[it] ?: 0) + 1 }

        p3.playCard(Rank.QUEEN of Suit.SPADES)
        p4.playCard(Rank.EIGHT of Suit.SPADES)
        p1.playCard(Rank.NINE of Suit.DIAMONDS)
        p2.playCard(Rank.TWO of Suit.SPADES)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.playCard(Rank.ACE of Suit.DIAMONDS)
        p2.playCard(Rank.FOUR of Suit.SPADES)
        p3.playCard(Rank.TEN of Suit.DIAMONDS)
        p4.playCard(Rank.SIX of Suit.DIAMONDS)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.playCard(Rank.KING of Suit.DIAMONDS)
        p2.playCard(Rank.FOUR of Suit.HEARTS)
        p3.playCard(Rank.SEVEN of Suit.CLUBS)
        p4.playCard(Rank.SEVEN of Suit.DIAMONDS)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.playCard(Rank.JACK of Suit.DIAMONDS)
        p2.playCard(Rank.JACK of Suit.HEARTS)
        p3.playCard(Rank.FIVE of Suit.CLUBS)
        p4.playCard(Rank.NINE of Suit.SPADES)

        p1.let { wins[it] = (wins[it] ?: 0) + 1 }

        p1.let { assertEquals(Utils.computePlayerScore(bids[it]!!, wins[it] ?: 0), it.getScore()) }
        p2.let { assertEquals(Utils.computePlayerScore(bids[it]!!, wins[it] ?: 0), it.getScore()) }
        p3.let { assertEquals(Utils.computePlayerScore(bids[it]!!, wins[it] ?: 0), it.getScore()) }
        p4.let { assertEquals(Utils.computePlayerScore(bids[it]!!, wins[it] ?: 0), it.getScore()) }
    }

    @Test fun testGameScoresAreRememberedForNextGame() = withGame { game, driver ->
        val scores = mutableMapOf<Player, Int>()

        var init = false

        repeat(10) {
            driver.doInitialBidding()
            driver.doDeclaringTrump()
            driver.doFinalBidding()

            val bids = game.getPlayerBids()
            val playerBids = mutableMapOf<Player, Int>()

            bids.forEach {
                playerBids[it.player] = it.bid
            }

            if (!init) {
                bids.forEach { scores[it.player] = 0 }
                init = true
            }

            repeat(13) {
                driver.doTrick()
            }

            val gameWins = mutableMapOf<Player, Int>()

            val tricks = game.getPastTricks().takeLast(13)
            tricks.forEach {
                val p = it.getWinner()
                gameWins[p] = (gameWins[p] ?: 0) + 1
            }

            val diffs = mutableMapOf<Player, Int>()
            bids.map { it.player }.forEach {
                diffs[it] = abs(playerBids[it]!! - (gameWins[it] ?: 0)) * -1
                if (diffs[it]!! == 0) {
                    diffs[it] = (gameWins[it] ?: 13)
                }

                scores[it] = (scores[it] ?: 0) + diffs[it]!!

                assertEquals(scores[it]!!, it.getScore())
            }
        }
    }

    @Test fun testPlayerWithHighestScoreBidsFirst() = withGame { game, driver ->
        driver.forwardToTrickTaking()
        repeat(13) { driver.doTrick() }

        repeat(10) {
            val p = game.playerGroup.players.maxBy { it.getScore() }!!
            assertEquals(p, game.getPlayerWithTurn())

            driver.forwardToTrickTaking()
            repeat(13) { driver.doTrick() }
        }
    }

    @Test fun testIllegalOperations() = withGame { game, driver ->
        game.getPlayerWithTurn().let {
            assertFails { it.playAnyCardInHand() }
            assertFails { it.declareTrump(Suit.DIAMONDS) }
            assertFails { it.pass() }
            it.bid(5)
        }

        game.getPlayerWithTurn().let {
            assertFails { game.getTrumpSuit() }
            assertFails { it.playAnyCardInHand() }
            assertFails { it.declareTrump(Suit.DIAMONDS) }
            it.pass()
        }

        game.getPlayerWithTurn().pass()
        game.getPlayerWithTurn().pass()

        game.getPlayerWithTurn().let {
            assertFails { it.pass() }
            assertFails { it.bid(6) }
            assertFails { game.getTrumpSuit() }
            assertFails { it.playAnyCardInHand() }
            it.declareTrump(Suit.DIAMONDS)
        }

        game.getPlayerWithTurn().let {
            assertFails { it.declareTrump(Suit.SPADES) }
            assertFails { it.pass() }
            assertFails { it.playAnyCardInHandOfSuitIfPossible(game.getTrumpSuit()) }
            it.bid(4)
        }

        game.getPlayerWithTurn().bid(4)
        game.getPlayerWithTurn().bid(1)

        repeat(3) { driver.doTrick() }

        game.getPlayerWithTurn().let {
            assertFails { it.pass() }
            assertFails { it.bid(4) }
            assertFails { it.declareTrump(Suit.HEARTS) }
            it.playAnyCardInHandOfSuitIfPossible(game.getTrumpSuit())
        }
    }
}