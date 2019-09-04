# estimation

Estimation a trick taking card game meant to be played with 4 players. This library was developed to facilitate gameplay between players on a server.

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
