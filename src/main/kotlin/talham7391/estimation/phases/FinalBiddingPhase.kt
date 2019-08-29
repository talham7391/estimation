package talham7391.estimation.phases

import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import talham7391.estimation.gamedata.Bid
import talham7391.estimation.gamedata.NewBid

class FinalBiddingPhase(
    private val playerGroup: PlayerGroup,
    winningBid: Bid,
    setGroupActions: Boolean = false
) : BasePhase() {

    private var turnOf = playerGroup.playerAfter(winningBid.player)
    private val bids = mutableListOf(winningBid)

    init {
        if (setGroupActions) {
            playerGroup.actions = this
        }
    }

    override fun bid(player: Player, bid: Int) {
        insureOnGoing()
        insurePlayersTurn(player)

        if (bid < 0 || bid > 13) {
            throw IllegalAction(player, "BID")
        } else if (bids[0].bid < bid) {
            throw IllegalAction(player, "BID")
        }

        bids.add(NewBid(player, bid))
        turnOf = playerGroup.playerAfter(player)
    }

    override fun isPhaseComplete() = bids.size == 4

    override fun getPlayerWithTurn() = turnOf

    fun getFinalBids(): List<Bid> = bids
}