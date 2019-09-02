package talham7391.estimation

import talham7391.estimation.gamedata.Play
import kotlin.random.Random

class GameDriver(
    private val game: Estimation
) : TurnListener {

    private var isEnabled = false

    init {
        game.addTurnListener(this)
    }

    override fun onPlayersTurnToInitialBid(player: Player) {
        if (isEnabled) {
            player.doInitialBidTurn()
        }
    }

    override fun onPlayersTurnToDeclareTrump(player: Player) {
        disable()
    }

    override fun onPlayersTurnToFinalBid(player: Player) {
        if (isEnabled) {
            player.doFinalBidding()
        }
    }

    override fun onPlayersTurnToPlayCard(player: Player, trickSoFar: List<Play>) {
        if (trickSoFar.isEmpty()) {
            disable()
        } else if (isEnabled && trickSoFar.isNotEmpty()) {
            player.doTrickTurn(trickSoFar)
        }
    }

    fun doInitialBidding() {
        enable()
        game.notifyPlayerOfTurn()
    }

    fun doDeclaringTrump() {
        game.getPlayerWithTurn().doDeclaringBid()
    }

    fun doFinalBidding() {
        enable()
        game.getPlayerWithTurn().doFinalBidding()
    }

    fun doTrick() {
        enable()
        game.getPlayerWithTurn().doTrickTurn(emptyList())
    }

    private fun disable() {
        isEnabled = false
    }

    private fun enable() {
        isEnabled = true
    }

    private fun Player.doInitialBidTurn() {
        val highestBid = game.initialBiddingHistory().maxBy { it.bid }?.bid
        val shouldPass = highestBid != null && (Random.nextBoolean() || highestBid == 13)
        if (shouldPass) {
            pass()
        } else {
            val myBid = Random.nextInt((highestBid ?: 0) + 1, 14)
            bid(myBid)
        }
    }

    private fun Player.doDeclaringBid() {
        val trumpSuit = Random.nextInt(Suit.values().size)
        declareTrump(Suit.values()[trumpSuit])
    }

    private fun Player.doFinalBidding() {
        val highestBid = game.getPlayerBids().maxBy { it.bid }?.bid
        val myBid = Random.nextInt((highestBid ?: 0) + 1)
        bid(myBid)
    }

    private fun Player.doTrickTurn(trickSoFar: List<Play>) {
        if (trickSoFar.isEmpty()) {
            playAnyCardInHand()
        } else {
            val leadingSuit = trickSoFar[0].card.suit
            if (!playAnyCardInHandOfSuit(leadingSuit)) {
                playAnyCardInHand()
            }
        }
    }
}

fun GameDriver.forwardToTrickTaking() {
    doInitialBidding()
    doDeclaringTrump()
    doFinalBidding()
}
