# estimation

Estimation is a trick taking card game meant to be played with 4 players. This library was developed to facilitate gameplay between players on a server.

## Table of Contents

1. [Game Rules](#game-rules)
2. [Usage](#usage)
3. [Reference](#reference)
	1. [BasicGameLogger](#basic-game-logger)
	2. [Card](#card)
	3. [Estimation](#estimation-class)
	4. [GameDriver](#game-driver)
	5. [Interface GameListener](#interface-game-listener)
 	6. [Player](#player)
	7. [PlayerGroup](#player-group)
	8. [Interface TurnListener](#interface-turn-listener)

<a name="game-rules"></a>
## Game Rules

### Setup
Each player is dealt 13 cards.

### Gameplay
There are 13 rounds in a game. Players "estimate" how many rounds they think they can win based on their cards. They must hit their target exactly; winning more or less results in losing points.

Each round, players play 1 card each in clockwise fashion. The first card to be played is called the leading suit. Subsequent players must play the leading suit if they have it. The winner of the round is the player with the highest leading suit unless someone played the trump suit, in which case the player with the highest trump card wins.

### Estimating/Bidding
There are 2 rounds of bidding:

**First Round**

The player with the highest score bids first. Subsequent players must bid higher than the highest bid so far. If they cannot beat the highest bid, they must pass. Once all players but 1 has passed, the remaning player (with the highest bid) declares the trump suit.

**Second Round**

Now that the trump suit has been declared, the players who passed are given another chance to estimate how many they think they can win. The player left to the winning bidder starts the estimation and it proceeds in a clockwise fashion.

### Rounds

The winning bidder starts the first round. Subsequent rounds are started by the player who won the previous round. Players must play 1 card each in a clockwise fashion.

### Game End

Once all 13 rounds are over, players check how many rounds they won, and determine whether they hit/missed their target. If they hit their target, they get that many points. If the missed, they lose the difference.

**Example**

- If bobby estimated he would win 5 rounds and won 5 rounds, he gets +5 points.
- If bobby estimated he would win 5 rounds and won 3 rounds or 7 rounds, he gets -2 points.

If a player estimates they will win 0 rounds and wins 0 rounds by game end, they receive 13 points.

Points are cumulated as games are played. Players determine when they want to stop playing. Player with the highest score after x number of games wins.

<a name="usage"></a>
## Usage

**Setup**
```
val p1 = Player()
val p2 = Player()
val p3 = Player()
val p4 = Player()
val game = Estimation(p1, p2, p3, p4)
```

**First Round Bidding**
```
p1.bid(2)
p2.bid(4)
p3.pass()
p4.pass()
p1.pass()
```

**Winner Declares Trump**
```
p2.declareTrump(Suit.DIAMONDS)
```

**Second Round Bidding**
```
p3.bid(4)
p4.bid(3)
p1.bid(2)
```

**Trick Taking**
```
// assuming the following players have the cards they are playing
// the cards a player is dealt is accessible via player.getCardsInHand()

p2.playCard(Rank.FOUR of Suit.CLUBS)
p3.playCard(Rank.SEVEN of Suit.CLUBS)
p4.playCard(Rank.TWO of Suit.DIAMONDS)
p1.playCard(Rank.KING of Suit.CLUBS)

// p4 wins with the trump suit so they start the next round

p4.playCard(Rank.FIVE of Suit.HEARTS)
// ...

// after 13 rounds like this the game ends and players can start bidding again
```

<a name="referenec"></a>
## Reference

<a name="basic-game-logger"></a>
### BasicGameLogger
Useful in logging game progress.

```
val game = Estimation(Player(), Player(), Player(), Player())
game.addGameListener(BasicGameLogger())

game.getPlayerWithTurn().bid(4)
game.getPlayerWithTurn().pass()
```
Output:
```
Player 1 has bid 4.
Player 2 has passed.
```

<a name="card"></a>
### Card

- `fun newDeck() : List<Card>` - Returns a list of 52 unique cards.

- `fun MutableList<Card>.randomlyTake(num: Int): List<Card>` - Randomly removes `num` number of cards from a mutable list and returns those cards as a list. Useful for distributing cards to players from a mutable list of 52 cards.

- `infix fun Rank.of(suit: Suit)` - Convenience function to write `Rank.THREE of Suit.SPADES` instead of `Card(Rank.THREE, Suit.SPADES)`.

<a name="estimation-class"></a>
### Estimation

- `val playerGroup: PlayerGroup` - `PlayerGroup` created for this game.

- `fun setCardsInHand(player: Player, cards: List<Card>)` - override the autodistributed cards for a player. Useful for testing... use with caution!

- `override fun bid(player: Player, bid: Int)` - used on player's turn.

- `override fun pass(player: Player)` - used on player's turn.

- `override fun declareTrump(player: Player, suit: Suit)` - used on player's turn.

- `override fun playCard(player: Player, card: Card)` - used on player's turn.

- `override fun getCardsInHand(player: Player): List<Card>` - get the cards in a players hand for the current game.

- `override fun getTurnIndex(player: Player): Int` - get where the player is sitting at the table. Stays the same throughout the duration of the game.

- `override fun getScore(player: Player): Int` - get the score of the player. This is updated after every game based on the player's target vs. actual round wins.

- `fun getPlayerWithTurn(): Player` - returns the player that is expected to bid, pass, declareTrump, or playCard next.

- `fun notifyPlayerOfTurn()` - notifies `TurnListener`s of which player must do their turn.

- `fun addTurnListener(listener: TurnListener)` - add a `TurnListener` to this game. It will be notified which player must do their turn as the game progresses. Useful for creating a reactive player.

- `fun addGameListener(listener: GameListener)` - add a `GameListener` to this game. It will be notified of game events as the game progresses. Usefull for tracking/logging game metrics.

- `fun initialBiddingHistory(): List<InitialBid>` - get the bidding history for the first round of bidding. This is reset every game.

- `fun getPlayerBids(): List<Bid>` - get each player's bids during/after the second bidding phase.

- `fun getTrumpSuit(): Suit` - get the trump suit declared by the winning bidder.

- `fun getPastTricks(): List<Trick>` - get tricks played throughout the game. Tricks are remembered game to game.

- `fun cleanup()` - should be called when deleting the game object.

<a name="game-driver"></a>
### GameDriver
Used to play the game for players. This is useful for testing.

```
val game = Estimation(Player(), Player(), Player(), Player())
val driver = GameDriver(game)

driver.doInitialBidding()

// now the winning player must declare a trump

driver.doDeclaringTrump()

// now game.getTrumpSuit() is available

driver.doFinalBidding()

// all players have a bid now and winning bidder must play a card to start the first round

repeat(13) { driver.doTrick() }

// game over, scores are updated
```

<a name="interface-game-listener"></a>
### Interface GameListener

- `fun playerInitiallyBid(player: Player, bid: Int)` - called when a player has bid during the first round of bidding.

- `fun playerPassed(player: Player)` - called when a player passes during the first round of bidding.

- `fun playerDeclaredTrumpSuit(player: Player, trumpSuit: Suit)` - called when the winning bidder declares a trump suit.

- `fun playerFinallyBid(player: Player, bid: Int)` - called when a player bids during the second round of bidding.

- `fun playersPlayCardTurn(player: Player, card: Card)` - called when a card is played.

- `fun trickFinished(trick: Trick)` - called when a trick is finished.

`BaseGameListener` implements stubs for the methods mentioned above. You can inherit from `BaseGameListener` to and only override the methods you need.

<a name="player"></a>
### Player

- `fun bid(bid: Int)` - helper function to use instead of `game.bid(player, 5)`

- `fun pass()` - helper function to use instead of `game.pass(player)`

- `fun declareTrump(suit: Suit)` - helper function to use instead of `game.declareTrump(player, suit)`

- `fun playCard(card: Card)` - helper function to use instead of `game.playCard(player, card)`

- `fun getCardsInHand(): List<Card>` - list of cards in player's hand. Gets updated as cards as played.

- `fun getTurnIndex()` - helper function to use instead of `game.getTurnIndex(player)`

- `fun getScore()` - helper function to use instead of `game.getScore(player)`

- `fun Player.playAnyCardInHand(): Card?` - useful for testing. Returns the card that was played.

- `fun Player.playAnyCardInHandOfSuit(suit: Suit): Card?` - useful for testing. Returns the card that was played.

- `fun Player.playAnyCardInHandOfSuitIfPossible(suit: Suit): Card?` - if the player doesn't have the required suit they will play any card. Useful for testing. Returns the card that was played.

<a name="player-group"></a>
### PlayerGroup

- `val players: List<Player>` - the players that are a part of this group

- `fun playerAfter(player: Player): Player` - returns the player "sitting" after this player. (i.e. returned player would play a card after the given player)

The rest of the methods on this class are useless to the user. User should use the same methods from the `Player` class or the `Estimation` class instead.

<a name="interface-turn-listener"></a>
### Interface TurnListener

- `fun onPlayersTurnToInitialBid(player: Player)` - called when it is a player's turn to bid during the first round of bidding.

- `fun onPlayersTurnToDeclareTrump(player: Player)` - called when it is a player's turn to declare the trump suit.

- `fun onPlayersTurnToFinalBid(player: Player)` - called when it is a player's turn to bid during the second round of bidding.

- `fun onPlayersTurnToPlayCard(player: Player, trickSoFar: List<Play>)` - called when it is a player's turn to play a card. `trickSoFar` contains cards that have been played by previous players.
