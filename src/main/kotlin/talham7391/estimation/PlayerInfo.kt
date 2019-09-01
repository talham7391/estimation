package talham7391.estimation

interface PlayerInfo {
    fun getCardsInHand(player: Player): List<Card>
    fun getTurnIndex(player: Player): Int
}
