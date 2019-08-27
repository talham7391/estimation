package talham7391.estimation.phases

import talham7391.estimation.Card
import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import talham7391.estimation.Suit

class FinalBiddingPhase(
    private val playerGroup: PlayerGroup,
    setGroupActions: Boolean = false
) : Phase {

    private lateinit var turnOf: Player

    init {
        if (setGroupActions) {
            playerGroup.actions = this
        }
    }

    override fun bid(player: Player, bid: Int) {
        TODO()
    }

    override fun pass(player: Player) {
        throw IllegalAction(player, "PASS")
    }

    override fun declareTrump(player: Player, suit: Suit) {
        throw IllegalAction(player, "DECLARE_TRUMP")
    }

    override fun playCard(player: Player, card: Card) {
        throw IllegalAction(player, "PLAY_CARD")
    }

    override fun isPhaseComplete(): Boolean {
        TODO()
    }

    override fun getPlayerWithTurn(): Player {
        TODO()
    }
}