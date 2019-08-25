package talham7391.estimation

interface Player {
    fun getId(): String
    fun doTurn(game: ActionReceiver)
}

interface ActionReceiver {
    fun bid(bid: Int)
    fun pass()
    fun playCard(card: Card)
}

