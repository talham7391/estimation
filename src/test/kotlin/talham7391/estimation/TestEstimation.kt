import talham7391.estimation.*
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

    @Test fun testGameScoresAreRememberedForNextGame() {

    }

    @Test fun testPlayerWithHighestScoreBidsFirst() {

    }

    @Test fun testPlayerWhoWonTheTrickStartsTheNextTrick() {

    }

    @Test fun testGameEndsAfter13Tricks() {

    }

    @Test fun testPlayerScoresAreComputedProperly() {

    }

    @Test fun testIllegalOperations() {

    }

    @Test fun testReturnsTheCorrectPastTricks() {

    }
}