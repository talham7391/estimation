package talham7391.estimation

import talham7391.estimation.gamedata.Trick

interface GameListener {
    fun playerInitiallyBid(player: Player, bid: Int)
    fun playerPassed(player: Player)
    fun playerDeclaredTrumpSuit(player: Player, trumpSuit: Suit)
    fun playerFinallyBid(player: Player, bid: Int)
    fun playersPlayCardTurn(player: Player, card: Card)

    fun trickFinished(trick: Trick)
}