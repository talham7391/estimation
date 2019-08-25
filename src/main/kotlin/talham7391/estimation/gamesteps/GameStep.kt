package talham7391.estimation.gamesteps

import talham7391.estimation.Card

interface GameStep {
    fun bid(bid: Int): GameStep
    fun pass(): GameStep
    fun playCard(card: Card): GameStep
    fun done(): Boolean
    fun turnOfIndex(): Int
}
