package talham7391.estimation

import kotlin.random.Random

open class Player {
    private lateinit var actions: GameActions
    private lateinit var playerInfoProvider: PlayerInfoProvider

    internal fun setGameActions(a: GameActions) {
        actions = a
    }

    internal fun setPlayerInfoProvider(p: PlayerInfoProvider) {
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

    fun getScore() = playerInfoProvider.getScore(this)
}

fun Player.playAnyCardInHand(): Card? {
    val cardsInHand = getCardsInHand()
    return if (cardsInHand.isEmpty()) {
        null
    } else {
        val card = cardsInHand[Random.nextInt(cardsInHand.size)]
        playCard(card)
        card
    }
}

fun Player.playAnyCardInHandOfSuit(suit: Suit): Card? {
    val cardsInHand = getCardsInHand().filter { it.suit == suit }
    return if (cardsInHand.isEmpty()) {
        null
    } else {
        val card = cardsInHand[Random.nextInt(cardsInHand.size)]
        playCard(card)
        card
    }
}

fun Player.playAnyCardInHandOfSuitIfPossible(suit: Suit): Card? {
    var card = playAnyCardInHandOfSuit(suit)
    if (card == null) {
        card = playAnyCardInHand()
    }
    return card
}