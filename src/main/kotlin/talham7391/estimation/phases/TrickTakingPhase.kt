package talham7391.estimation.phases

import talham7391.estimation.Card
import talham7391.estimation.Player
import talham7391.estimation.PlayerGroup
import talham7391.estimation.Suit
import talham7391.estimation.gamedata.Play
import talham7391.estimation.gamedata.Trick

class TrickTakingPhase(
    private val playerGroup: PlayerGroup,
    startingPlayer: Player,
    private val trumpSuit: Suit,
    setGroupActions: Boolean = false
) : BasePhase() {

    init {
        if (setGroupActions) {
            playerGroup.actions = this
        }
    }

    private var turnOf = startingPlayer
    private val plays = mutableListOf<Play>()

    override fun playCard(player: Player, card: Card) {
        insureOnGoing()
        insurePlayersTurn(player)

        if (plays.any { it.card == card }) {
            throw IllegalAction(player, "PLAY_CARD")
        }

        plays.add(Play(player, card))
        turnOf = playerGroup.playerAfter(player)
    }

    override fun getPlayerWithTurn() = turnOf

    override fun isPhaseComplete() = plays.size == 4

    fun getTrick(): Trick {
        insureComplete()
        return Trick(plays, trumpSuit)
    }
}