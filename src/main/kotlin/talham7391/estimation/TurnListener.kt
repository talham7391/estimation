package talham7391.estimation

interface TurnListener {
    fun initialBidFor(player: Player)
    fun finalBidFor(player: Player)
    fun declaredTrumpFor(player: Player)
    fun turnFor(player: Player)
}
