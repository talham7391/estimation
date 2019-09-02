package talham7391.estimation

interface PlayerInfoProvider {
    fun getCardsInHand(player: Player): List<Card>
    fun getTurnIndex(player: Player): Int
    fun getScore(player: Player): Int
}
