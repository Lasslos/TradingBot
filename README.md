# TradingBot

The goal is to develop a trading bot that can participate in the following:

# Rules
Two bidders start with y monetary units. x quantitative units are available for purchase, where x is even.
There are $\frac x 2$ rounds. In each round, both bidders place a bid and must pay the amount.
The bidder with the higher bid wins the two quantitative units. In case of a tie, both receive one quantitative unit each.

The goal is to win more than half of the quantitative units, or tie with more money left.

# Testing environment

In [Auction](src/main/java/auction/Auction.java) the auction is simulated.
Add your bot to `bidders` in [Main](src/main/java/Main.java) to test it against some default strategies. It will run run and repeat the auction according to the constants at the beginning of the file.

# Default Strategies

Let $m_i$ be the bidder's monetary units, $q_i$ the bidder's quantitative units.
Let $m_o$ be the opponent's monetary units, $q_o$ the opponent's quantitative units.

Let $m_s$ be the starting monetary units, $q_s$ the starting quantitative units.
Let $q_r$ be the reamining quantitative units, which is $q_s - q_i - q_o$.

All of these numbers are known to both bidders, as they are informed about bet sizes after each round.

The simplest strategy is to always bid $\frac{m_s}{q_s} \cdot 2$, that way at the end of the auction, there will be no money left. 
This is [SimpleBidder](src/main/java/bidders/SimpleBidder.java)

Another simple strategy is to bid one more than the opponent's last bid, or all your cash if this is too high.
This is [TitForTatBidder](src/main/java/bidders/TitForTatBidder.java)

Lastly, [RandomBidder](src/main/java/bidders/RandomBidder.java) bids a random amount between 0 and the maximum amount it can bid, with a gaussian distribution to get random numbers.
# Some ideas

### Randomness
Firstly, there is no optimal strategy that does not rely on randomness.
If there was a non-random strategy that is optimal, the opponent could just bet one more than the optimal amount and win almost every round.

This means, we have to use randomness to be less predictable.

Since this is true for the opponent as well, we should not try to predict the opponent's strategy, assuming the opponent
is a sophisticated strategy.

### Win conditions
If $2q_i > q_s$, we can start bidding 0, as we have already won more than half of the quantitative units.

If we need to win $x$ rounds to reach the win condition, and $\frac{m_i}{m_o} > x$, then the SimpleBidder strategy
with bet size $\frac{m_i}{x}$ wins, as the opponent does not have enough money to post the same bet size.
