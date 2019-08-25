package talham7391.estimation.gamesteps

import talham7391.estimation.Card
import talham7391.estimation.Rank
import talham7391.estimation.Suit
import kotlin.test.Test

class TestRound {
    @Test fun testRound() {
        val players = listOf("1", "2", "3", "4")
        var round: GameStep = Round(players, 0, Suit.DIAMONDS)

        round = round.playCard(Card(Suit.HEARTS, Rank.TWO))
        round = round.playCard(Card(Suit.CLUBS, Rank.THREE))
        round = round.playCard(Card(Suit.SPADES, Rank.JACK))
        round = round.playCard(Card(Suit.DIAMONDS, Rank.ACE))

        assert(round.done())
    }
}