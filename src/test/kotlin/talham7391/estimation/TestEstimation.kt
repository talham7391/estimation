import talham7391.estimation.*
import talham7391.estimation.gamedata.Play
import talham7391.estimation.gamedata.Trick
import talham7391.estimation.gamedata.getWinner
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

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

    @Test fun testPlayerScoresAreComputedProperly() = withGame { game, driver ->
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
                    diffs[it] = gameWins[it]!!
                }

                scores[it] = (scores[it] ?: 0) + diffs[it]!!

                assertEquals(scores[it]!!, it.getScore())
            }
        }

        // add a manual test
    }

    @Test fun testGameScoresAreRememberedForNextGame() {

    }

    @Test fun testPlayerWithHighestScoreBidsFirst() {

    }

    @Test fun testIllegalOperations() {

    }
}