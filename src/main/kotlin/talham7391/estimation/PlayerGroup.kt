package talham7391.estimation

class PlayerGroup(
    one: Player,
    two: Player,
    three: Player,
    four: Player,
    setAsPlayerInfoProvider: Boolean = true
) : GameActions, PlayerInfoProvider {

    private lateinit var playerInfoProvider: PlayerInfoProvider
    private lateinit var actions: GameActions

    val players = listOf(one, two, three, four)

    init {
        players.forEach {
            it.setGameActions(this)

            if (setAsPlayerInfoProvider) {
                it.setPlayerInfoProvider(this)
            }
        }
    }

    fun setGameActions(a: GameActions) {
        actions = a
    }

    fun setPlayerInfoProvider(p: PlayerInfoProvider) {
        playerInfoProvider = p
    }

    override fun bid(player: Player, bid: Int) = actions.bid(player, bid)

    override fun pass(player: Player) = actions.pass(player)

    override fun declareTrump(player: Player, suit: Suit) = actions.declareTrump(player, suit)

    override fun playCard(player: Player, card: Card) = actions.playCard(player, card)

    override fun getCardsInHand(player: Player) = playerInfoProvider.getCardsInHand(player)

    override fun getTurnIndex(player: Player) = playerInfoProvider.getTurnIndex(player)

    override fun getScore(player: Player) = playerInfoProvider.getScore(player)

    fun playerAfter(player: Player): Player {
        val idx = players.indexOf(player)
        return players[(idx + 1) % players.size]
    }
}
