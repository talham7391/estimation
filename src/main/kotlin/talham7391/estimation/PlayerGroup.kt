package talham7391.estimation

class PlayerGroup(
    one: Player,
    two: Player,
    three: Player,
    four: Player
) : GameActions {
    lateinit var actions: GameActions
    val players = listOf(one, two, three, four)

    init {
        players.forEach { it.actions = this }
    }

    override fun bid(player: Player, bid: Int) = actions.bid(player, bid)

    override fun pass(player: Player) = actions.pass(player)

    override fun declareTrump(player: Player, suit: Suit) = actions.declareTrump(player, suit)

    override fun playCard(player: Player, card: Card) = actions.playCard(player, card)

    fun playerAfter(player: Player): Player {
        val idx = players.indexOf(player)
        return players[(idx + 1) % players.size]
    }
}
