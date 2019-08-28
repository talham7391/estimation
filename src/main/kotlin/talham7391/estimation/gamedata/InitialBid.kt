package talham7391.estimation.gamedata

import talham7391.estimation.Player

data class InitialBid(
    override val player: Player,
    override val bid: Int,
    val passed: Boolean
) : Bid
