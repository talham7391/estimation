package talham7391.estimation.phases

import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import talham7391.estimation.Suit

class DeclaringTrumpPhase(
    private val playerGroup: PlayerGroup,
    private val playerWithWinningBid: Player,
    setGroupActions: Boolean = false
) : BasePhase() {

    init {
        if (setGroupActions) {
            playerGroup.actions = this
        }
    }

    private var trumpSuit: Suit? = null

    override fun declareTrump(player: Player, suit: Suit) {
        insureOnGoing()
        insurePlayersTurn(player)
        trumpSuit = suit
    }

    override fun isPhaseComplete() = trumpSuit != null

    override fun getPlayerWithTurn() = playerWithWinningBid

    fun getTrumpSuit(): Suit {
        insureComplete()
        return trumpSuit!!
    }
}