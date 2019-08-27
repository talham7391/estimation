package talham7391.estimation.phases

import talham7391.estimation.GameActions
import talham7391.estimation.Player

interface Phase : GameActions {
    fun getPlayerWithTurn(): Player
    fun isPhaseComplete(): Boolean
}

class PlayerOutOfTurn(player: Player) : Exception("Player $player is playing out of turn.")

class IllegalAction(player: Player, action: String) : Exception("Player $player performed illegal action $action.")

class PhaseComplete : Exception("This phase is complete.")

class PhaseNotComplete : Exception("This phase is not complete.")