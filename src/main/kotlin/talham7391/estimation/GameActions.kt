package talham7391.estimation

interface GameActions {
    fun bid(player: Player, bid: Int)
    fun pass(player: Player)
    fun declareTrump(player: Player, suit: Suit)
    fun playCard(player: Player, card: Card)
}