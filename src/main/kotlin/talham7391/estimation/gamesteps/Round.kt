package talham7391.estimation.gamesteps

import talham7391.estimation.Card
import talham7391.estimation.Suit
import talham7391.estimation.Trick

class Round(
    private val players: Collection<String>,
    private val turnOf: Int,
    private val trumpSuit: Suit
) : GameStep {
    private var trick = Trick(trumpSuit, players.size)
    private var played = arrayOfNulls<Boolean>(players.size)
    private var finished = false

    override fun bid(bid: Int): GameStep {
        throw IllegalAccessError("You cannot bid during gameplay.")
    }

    override fun pass(): GameStep {
        throw IllegalAccessError("You cannot pass during gameplay.")
    }

    override fun playCard(card: Card): GameStep {
        if (finished) {
            throw IllegalAccessError("Round is over.")
        }

        played[turnOf] = true
        trick.addCard(players.elementAt(turnOf), card)

        val nextStep = Round(players, (turnOf + 1) % players.size, trumpSuit)
        nextStep.trick = trick
        nextStep.played = played
        if (played.count { it == true } == players.size) {
            nextStep.finished = true
        }

        return nextStep
    }

    override fun done() = finished

    override fun turnOfIndex() = turnOf
}