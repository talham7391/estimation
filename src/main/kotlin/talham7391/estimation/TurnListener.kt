package talham7391.estimation

import talham7391.estimation.gamedata.Play

interface TurnListener {
    fun onPlayersTurnToInitialBid(player: Player)
    fun onPlayersTurnToDeclareTrump(player: Player)
    fun onPlayersTurnToFinalBid(player: Player)
    fun onPlayersTurnToPlayCard(player: Player, trickSoFar: List<Play>)
}
