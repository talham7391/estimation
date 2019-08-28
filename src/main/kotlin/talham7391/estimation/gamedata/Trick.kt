package talham7391.estimation.gamedata

import talham7391.estimation.Player
import talham7391.estimation.Suit

data class Trick(
    val plays: List<Play>,
    val trumpSuit: Suit
)

fun Trick.getWinner(): Player {
    val highestTrump = plays.filter { it.card.suit == trumpSuit }.maxBy { it.card.rank.ordinal }
    highestTrump?.let { return it.player }

    val leadingSuit = plays[0].card.suit
    val highestLeading = plays.filter { it.card.suit == leadingSuit }.maxBy { it.card.rank.ordinal }
    return highestLeading!!.player
}