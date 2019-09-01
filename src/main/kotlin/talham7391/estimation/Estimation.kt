/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package talham7391.estimation

import talham7391.estimation.gamedata.Bid
import talham7391.estimation.gamedata.Trick
import talham7391.estimation.gamedata.getWinner
import talham7391.estimation.phases.DeclaringTrumpPhase
import talham7391.estimation.phases.FinalBiddingPhase
import talham7391.estimation.phases.InitialBiddingPhase
import talham7391.estimation.phases.TrickTakingPhase


class Estimation(
    p1: Player,
    p2: Player,
    p3: Player,
    p4: Player
) : GameActions, PlayerInfoProvider {

    private val playerGroup = PlayerGroup(p1, p2, p3, p4)
    private val initialBiddingPhase = InitialBiddingPhase(playerGroup)
    private var declaringTrumpPhase: DeclaringTrumpPhase? = null
    private var finalBiddingPhase: FinalBiddingPhase? = null
    private var trickTakingPhase: TrickTakingPhase? = null

    private val turnListeners = mutableListOf<TurnListener>()
    private val gameListeners = mutableListOf<GameListener>()

    private var cardsInHand = mutableMapOf<Player, MutableList<Card>>()
    private var pastTricks = mutableListOf<Trick>()

    init {
        playerGroup.let {
            it.actions = this
            it.playerInfoProvider = this
        }

        val deck = newDeck().toMutableList()
        val numCards = deck.size
        for (player in playerGroup.players) {
            cardsInHand[player] = deck.randomlyTake(numCards / playerGroup.players.size).toMutableList()
        }
    }

    override fun bid(player: Player, bid: Int) {
        if (!initialBiddingPhase.isPhaseComplete()) {

            initialBiddingPhase.bid(player, bid)
            gameListeners.forEach { it.playerInitiallyBid(player, bid) }

            tryMovingToDeclaringTrumpPhase()

        } else {

            if (finalBiddingPhase == null) {
                throw NotFinalBiddingPhaseYet()
            }

            finalBiddingPhase!!.bid(player, bid)
            gameListeners.forEach { it.playerFinallyBid(player, bid) }

            tryMovingToTrickTakingPhase()
        }

        notifyPlayerOfTurn()
    }

    override fun pass(player: Player) {
        initialBiddingPhase.pass(player)
        gameListeners.forEach { it.playerPassed(player) }

        tryMovingToDeclaringTrumpPhase()

        notifyPlayerOfTurn()
    }

    override fun declareTrump(player: Player, suit: Suit) {
        if (declaringTrumpPhase == null) {
            throw NotDeclaringTrumpPhaseYet()
        }

        declaringTrumpPhase!!.declareTrump(player, suit)
        gameListeners.forEach { it.playerDeclaredTrumpSuit(player, suit) }

        tryMovingToFinalBiddingPhase()

        notifyPlayerOfTurn()
    }

    override fun playCard(player: Player, card: Card) {
        if (trickTakingPhase == null) {
            throw NotTrickTakingPhaseYet()
        }

        trickTakingPhase!!.playCard(player, card)
        gameListeners.forEach { it.playersPlayCardTurn(player, card) }

        tryMovingToNextTrick()

        notifyPlayerOfTurn()
    }

    private fun tryMovingToDeclaringTrumpPhase() {
        if (initialBiddingPhase.isPhaseComplete()) {
            declaringTrumpPhase = DeclaringTrumpPhase(playerGroup, initialBiddingPhase.getWinningBid().player)
        }
    }

    private fun tryMovingToFinalBiddingPhase() {
        if (declaringTrumpPhase!!.isPhaseComplete()) {
            finalBiddingPhase = FinalBiddingPhase(playerGroup, initialBiddingPhase.getWinningBid())
        }
    }

    private fun tryMovingToTrickTakingPhase() {
        if (finalBiddingPhase!!.isPhaseComplete()) {
            trickTakingPhase = TrickTakingPhase(
                playerGroup,
                initialBiddingPhase.getWinningBid().player,
                declaringTrumpPhase!!.getTrumpSuit()
            )
        }
    }

    private fun tryMovingToNextTrick() {
        if (trickTakingPhase!!.isPhaseComplete()) {
            val trick = trickTakingPhase!!.getTrick()
            pastTricks.add(trick)
            gameListeners.forEach { it.trickFinished(trick) }

            trickTakingPhase = TrickTakingPhase(
                playerGroup,
                trick.getWinner(),
                trick.trumpSuit
            )
        }
    }

    private fun notifyPlayerOfTurn() {
        if (!initialBiddingPhase.isPhaseComplete()) {
            turnListeners.forEach { it.onPlayersTurnToInitialBid(initialBiddingPhase.getPlayerWithTurn()) }
        } else if (!declaringTrumpPhase!!.isPhaseComplete()) {
            turnListeners.forEach { it.onPlayersTurnToDeclareTrump(declaringTrumpPhase!!.getPlayerWithTurn()) }
        } else if (!finalBiddingPhase!!.isPhaseComplete()) {
            turnListeners.forEach { it.onPlayersTurnToFinalBid(finalBiddingPhase!!.getPlayerWithTurn()) }
        } else if (!trickTakingPhase!!.isPhaseComplete()) {
            turnListeners.forEach {
                it.onPlayersTurnToPlayCard(
                    trickTakingPhase!!.getPlayerWithTurn(),
                    trickTakingPhase!!.getPlays()
                )
            }
        }
    }

    override fun getCardsInHand(player: Player): List<Card> {
        return cardsInHand[player] ?: throw PlayerNotInGame()
    }

    override fun getTurnIndex(player: Player): Int {
        return playerGroup.players.indexOf(player)
    }

    fun start() {
        notifyPlayerOfTurn()
    }

    fun addTurnListener(listener: TurnListener) {
        turnListeners.add(listener)
    }

    fun addGameListener(listener: GameListener) {
        gameListeners.add(listener)
    }

    fun initialBiddingHistory() = initialBiddingPhase.getInitialBiddingHistory()

    fun getPlayerBids(): List<Bid> = finalBiddingPhase?.getFinalBids() ?: emptyList()

    fun getTrumpSuit(): Suit {
        if (declaringTrumpPhase == null) {
            throw TrumpSuitNotAvailable()
        }
        return declaringTrumpPhase!!.getTrumpSuit()
    }

    fun getPastTricks(): List<Trick> = pastTricks

    fun cleanup() {
        turnListeners.clear()
        gameListeners.clear()
    }
}

class TrumpSuitNotAvailable : Exception()
class NotDeclaringTrumpPhaseYet : Exception()
class NotFinalBiddingPhaseYet : Exception()
class NotTrickTakingPhaseYet : Exception()
class PlayerNotInGame : Exception()
