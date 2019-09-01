package talham7391.estimation

import talham7391.estimation.gamedata.Trick
import talham7391.estimation.gamedata.getWinner

class BasicGameLogger : GameListener {

    private var tricksStarted = false

    override fun playerInitiallyBid(player: Player, bid: Int) {
        println("Player ${player.getTurnIndex()} has bid $bid.")
    }

    override fun playerPassed(player: Player) {
        println("Player ${player.getTurnIndex()} has passed.")
    }

    override fun playerDeclaredTrumpSuit(player: Player, trumpSuit: Suit) {
        println()
        println("Player ${player.getTurnIndex()} has declared trump: ${trumpSuit.name}.")
        println()
    }

    override fun playerFinallyBid(player: Player, bid: Int) {
        println("Player ${player.getTurnIndex()} has bid $bid as their final bid.")
    }

    override fun playersPlayCardTurn(player: Player, card: Card) {
        if (!tricksStarted) {
            tricksStarted = true
            println()
            println("===")
            println()
        }
        println("Player ${player.getTurnIndex()} has played ${card.rank} of ${card.suit}.")
    }

    override fun trickFinished(trick: Trick) {
        println("--")
        println("Winner: Player ${trick.getWinner().getTurnIndex()}.")
        println()
    }
}