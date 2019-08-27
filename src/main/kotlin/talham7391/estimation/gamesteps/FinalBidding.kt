package talham7391.estimation.gamesteps

import talham7391.estimation.Card
import talham7391.estimation.Suit

class FinalBidding(
    val players: Collection<String>,
    private val turnOf: Int
) : GameStep {
    private var bids = arrayOfNulls<Int>(players.size)
    private var firstBid: Int? = null
    private var finished = false

    fun getBids(): Collection<Int> {
        if (!done()) {
            throw IllegalAccessError("Final bidding process is ongoing.")
        }
        return bids.map { it!! }
    }

    override fun bid(bid: Int): GameStep {
        if (finished) {
            throw IllegalAccessError("Initial bidding process is over.")
        }
        if (bid < 0 || bid > 13) {
            throw IllegalFinalBid()
        } else if (firstBid == null) {
            firstBid = bid
        } else if (bid > firstBid!!) {
            throw IllegalFinalBid()
        }

        bids[turnOf] = bid

        val nextStep = FinalBidding(players, (turnOf + 1) % players.size)

        if (bids.count { it != null } == players.size) {
            nextStep.finished = true
        }

        nextStep.bids = bids
        nextStep.firstBid = firstBid
        return nextStep
    }

    override fun pass(): GameStep {
        throw IllegalAccessError("You cannot pass during the final bidding process.")
    }

    override fun playCard(card: Card): GameStep {
        throw IllegalAccessError("You cannot play a card during the bidding process.")
    }

    override fun turnOfIndex() = turnOf

    override fun done() = finished
}

class IllegalFinalBid : Exception("You must bid lower or the same as the highest bid.")
