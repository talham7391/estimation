package talham7391.estimation

import kotlin.random.Random

class Player {
    var score = 0

    lateinit var actions: GameActions
    lateinit var playerInfo: PlayerInfo

    fun bid(bid: Int) {
        actions.bid(this, bid)
    }

    fun pass() {
        actions.pass(this)
    }

    fun declareTrump(suit: Suit) {
        actions.declareTrump(this, suit)
    }

    fun playCard(card: Card) {
        actions.playCard(this, card)
    }

    fun getCardsInHand() = playerInfo.getCardsInHand(this)
}

fun Player.playAnyCardInHand() {
    val cardsInHand = getCardsInHand()
    playCard(cardsInHand[Random.nextInt(cardsInHand.size)])
}