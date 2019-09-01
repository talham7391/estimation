package talham7391.estimation

import talham7391.estimation.gamedata.Play
import kotlin.random.Random

class GameDriver(
    private val game: Estimation
) : TurnListener {

    private var winningPlayer: Player? = null
    private var playerToStartFinalBidding: Player? = null
    private var playerToStartTrick: Player? = null

    init {
        game.addTurnListener(this)
    }

    override fun onPlayersTurnToInitialBid(player: Player) {
        player.doInitialBidTurn()
    }

    override fun onPlayersTurnToDeclareTrump(player: Player) {
        winningPlayer = player
    }

    override fun onPlayersTurnToFinalBid(player: Player) {
        if (playerToStartFinalBidding == null) {
            playerToStartFinalBidding = player
        } else {
            player.doFinalBidding()
        }
    }

    override fun onPlayersTurnToPlayCard(player: Player, trickSoFar: List<Play>) {
        if (trickSoFar.isEmpty()) {
            playerToStartTrick = player
        } else {
            player.doTrickTurn(trickSoFar)
        }
    }

    fun doInitialBidding() {
        game.start()
    }

    fun doDeclaringTrump() {
        winningPlayer?.doDeclaringBid()
    }

    fun doFinalBidding() {
        playerToStartFinalBidding?.doFinalBidding()
    }

    fun doTrick() {
        playerToStartTrick?.doTrickTurn(emptyList())
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