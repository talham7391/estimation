package talham7391.estimation.gamesteps

import talham7391.estimation.Card

class InitialBidding(
    private val players: Collection<String>,
    private val turnOf: Int
) : GameStep {
    private var bids = (0 until players.size).map { BiddingInfo(-1, false) }
    private var finished = false

    override fun bid(bid: Int): GameStep {
        if (finished) {
            throw IllegalAccessError("Initial bidding process is over.")
        }
        if (bid < 0 || bid > 13) {
            throw IllegalInitialBid()
        } else if (bid <= highestBid()) {
            throw IllegalInitialBid()
        }
        bids[turnOf].bid = bid
        return gotoNextStep()
    }

    override fun pass(): GameStep {
        if (finished) {
            throw IllegalAccessError("Initial bidding process is over.")
        }
        if (!hasAnyoneBidOrPassed()) {
            throw IllegalPass()
        }
        bids[turnOf].hasPassed = true
        return gotoNextStep()
    }

    override fun playCard(card: Card): GameStep {
        throw IllegalAccessError("You cannot play a card during the bidding process.")
    }

    override fun done() = finished

    override fun turnOfIndex() = turnOf

    private fun gotoNextStep(): GameStep {
        var nextHasNotPassed: Int? = null
        var turn = turnOf
        for (i in 0 until players.size) {
            turn = (turn + 1) % players.size
            if (!bids[turn].hasPassed) {
                nextHasNotPassed = turn
                break
            }
        }

        if (nextHasNotPassed == null) throw Exception("Everyone has passed...")

        val nextStep = InitialBidding(players, nextHasNotPassed)
        if (numStillBidding() == 1) {
            nextStep.finished = true
        }
        nextStep.bids = bids
        return nextStep
    }

    private fun highestBid() = bids.maxBy { it.bid }?.bid!!

    private fun hasAnyoneBidOrPassed() = bids.any { it.hasPassed || it.bid != -1 }

    private fun numStillBidding() = bids.count { !it.hasPassed }
}

data class BiddingInfo(
    var bid: Int,
    var hasPassed: Boolean
)

class IllegalInitialBid : Exception("You must bid higher than the highest bid.")
class IllegalPass : Exception("The first player cannot pass.")