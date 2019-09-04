package talham7391.estimation.phases

import talham7391.estimation.Card
import talham7391.estimation.GameActions
import talham7391.estimation.Player
import talham7391.estimation.Suit

internal interface Phase : GameActions {
    fun getPlayerWithTurn(): Player
    fun isPhaseComplete(): Boolean
}

abstract class BasePhase : Phase {

    protected fun insurePlayersTurn(player: Player) {
        if (player != getPlayerWithTurn()) {
            throw PlayerOutOfTurn(player)
        }
    }

    protected fun insureOnGoing() {
        if (isPhaseComplete()) {
            throw PhaseComplete()
        }
    }

    protected fun insureComplete() {
        if (!isPhaseComplete()) {
            throw PhaseNotComplete()
        }
    }

    override fun bid(player: Player, bid: Int) {
        throw IllegalAction(player, "BID")
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
}

class PlayerOutOfTurn(player: Player) : Exception("Player $player is playing out of turn.")

class IllegalAction(player: Player, action: String) : Exception("Player $player performed illegal action $action.")

class PhaseComplete : Exception("This phase is complete.")

class PhaseNotComplete : Exception("This phase is not complete.")