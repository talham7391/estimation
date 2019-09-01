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

    fun getTurnIndex() = playerInfo.getTurnIndex(this)
}

fun Player.playAnyCardInHand(): Boolean {
    val cardsInHand = getCardsInHand()
    return if (cardsInHand.isEmpty()) {
        false
    } else {
        playCard(cardsInHand[Random.nextInt(cardsInHand.size)])
        true
    }
}

fun Player.playAnyCardInHandOfSuit(suit: Suit): Boolean {
    val cardsInHand = getCardsInHand().filter { it.suit == suit }
    return if (cardsInHand.isEmpty()) {
        false
    } else {
        playCard(cardsInHand[Random.nextInt(cardsInHand.size)])
        true
    }
}