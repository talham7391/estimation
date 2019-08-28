package talham7391.estimation.gamedata

import talham7391.estimation.Player

interface Bid {
    val player: Player
    val bid: Int
}

private data class BidImpl(
    override val player: Player,
    override val bid: Int
) : Bid

fun NewBid(player: Player, bid: Int): Bid = BidImpl(player, bid)
