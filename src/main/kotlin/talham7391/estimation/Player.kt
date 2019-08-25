package talham7391.estimation

interface Player {
    fun getId(): String
    fun onTurn(cardsInHand: Collection<Card>)
    fun onRoundWon(pile: Collection<Card>)
    fun onRoundLost(pile: Collection<Card>)
}
