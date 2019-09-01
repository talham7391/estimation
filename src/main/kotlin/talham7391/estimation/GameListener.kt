package talham7391.estimation

interface GameListener {
    fun onPlayersTurnToInitialBid(player: Player)
    fun onPlayersTurnToDeclareTrump(player: Player)
    fun onPlayersTurnToFinalBid(player: Player)
    fun onPlayersTurnToPlayCard(player: Player)
}
