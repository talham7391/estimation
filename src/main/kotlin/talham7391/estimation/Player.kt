package talham7391.estimation

import kotlin.random.Random

class Player {
    var score = 0

    private lateinit var actions: GameActions
    private lateinit var playerInfoProvider: PlayerInfoProvider

    fun setGameActions(a: GameActions) {
        actions = a
    }

    fun setPlayerInfoProvider(p: PlayerInfoProvider) {
        playerInfoProvider = p
    }

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

    fun getCardsInHand() = playerInfoProvider.getCardsInHand(this)

    fun getTurnIndex() = playerInfoProvider.getTurnIndex(this)
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

fun Player.playAnyCardInHandOfSuitIfPossible(suit: Suit): Boolean {
    if (playAnyCardInHandOfSuit(suit)) {
        return true
    }
    return playAnyCardInHand()
}