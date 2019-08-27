package talham7391.estimation

class Player {
    var score = 0

    lateinit var actions: GameActions

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
}