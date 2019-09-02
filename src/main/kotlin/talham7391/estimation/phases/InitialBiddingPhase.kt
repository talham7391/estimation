package talham7391.estimation.phases

import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import talham7391.estimation.gamedata.InitialBid

class InitialBiddingPhase(
    private val playerGroup: PlayerGroup,
    setGroupActions: Boolean = false
) : BasePhase() {

    private val bids = mutableListOf<InitialBid>()
    private var turnOf = playerGroup.players.maxBy { it.score }!!

    init {
        if (setGroupActions) {
            playerGroup.setGameActions(this)
        }
    }

    private fun bookKeep(player: Player) {
        insurePlayersTurn(player)
        insureOnGoing()
    }

    private fun getPlayerWithNextTurn(): Player {
        var next = bids.last().player
        while (true) {
            next = playerGroup.playerAfter(next)
            if (bids.find { it.player == next && it.passed } == null) {
                return next
            }
        }
    }

    private fun wrapUp() {
        val nextPlayer = getPlayerWithNextTurn()
        turnOf = nextPlayer
    }

    override fun bid(player: Player, bid: Int) {
        bookKeep(player)
        if (bid < 0 || bid > 13) {
            throw IllegalAction(player, "BID")
        } else if (bids.any { !it.passed && bid <= it.bid }) {
            throw IllegalAction(player, "BID")
        }
        bids.add(InitialBid(player, bid, false))
        wrapUp()
    }

    override fun pass(player: Player) {
        bookKeep(player)
        if (bids.size == 0) {
            throw IllegalAction(player, "PASS")
        }
        bids.add(InitialBid(player, 0, true))
        wrapUp()
    }

    override fun getPlayerWithTurn() = turnOf

    override fun isPhaseComplete() = bids.count { it.passed } == 3

    fun getWinningBid(): InitialBid {
        insureComplete()
        return bids.last { !it.passed }
    }

    fun getInitialBiddingHistory(): List<InitialBid> = bids
}
