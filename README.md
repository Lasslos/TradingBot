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
This is [SimpleBidder](src/main/java/auction/bidders/SimpleBidder.java)

Another simple strategy is to bid one more than the opponent's last bid, or all your cash if this is too high.
This is [TitForTatBidder](src/main/java/auction/bidders/TitForTatBidder.java)

Lastly, [RandomBidder](src/main/java/auction/bidders/RandomBidder.java) bids a random amount between 0 and the maximum amount it can bid, with a gaussian distribution to get random numbers.
[RandomBidder2](src/main/java/auction/bidders/RandomBidder2.java) is similar, but uses a uniform distribution instead.
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

# First Analysis

Running the arena with those four strategies, we get the following results:
a - SimpleBidder
b - RandomBidder
c - TitForTatBidder
d - RandomBidder2

  | a | b | c | d |
a |  0| 63|  0| 61|
b | 37|  0| 49| 40|
c |100| 51|  0| 57|
d | 39| 60| 43|  0|

This shows TitForTatBidder marginally outperforms all other strategies, and RandomBidder is the worst strategy.
This is because TitForTatBidder is the only strategy that adapts to the opponent.

So, for my final strategy, I will do a more sophisticated Bidder which tries to predict the opponent's strategy.
This will work well against static strategies, but might be worse against strategies that use the fact that I try to predict them.

# MyStrategy

The final strategy is a sophisticated bidder that tries to predict the opponent's strategy and adapts to it.
See [MyBidder](src/main/java/auction/bidders/mybidder/MyBidder.java).

### Edge cases
- Very few QU are auctioned (3 rounds or less): Predicting the opponent's strategy is not worth it, as there are not enough rounds to adapt.
So, just bid 0 one time and the other times bid half of the money, this is not optimal, but good enough.
- Very few MU are available (MU <= QU): Assume this doesn't happen, as it doesn't really make sense.

### Phases
- First phase: Bid low and analyze the opponent's strategy.
- Second phase: Adapt to the opponent's strategy according to set rules.
- Third phase: If win condition is reached, bid 0, else, bid very high to win the last rounds.

### Analyzing the opponent's strategy
Amount categories: 
- $x > \frac{ m_s}{\lceil \frac{q_s + 1}{4} \rceil}$ In this case, the opponent bids very high amounts. He needs to win 
$\lceil \frac{q_s + 1}{4} \rceil$ QU, but has only $m_s$ monetary units, so he cannot win all rounds with that amount. Bet 0 until the opponent is low on money, then outbet him.
- $x > \frac{2m_s}{q_s}$ Opponent bids high amounts, more than average. In the end, he will need to bid lower. Bid zero and sometimes overbid his average to win some rounds, then in the end outbid him.
- $x > \frac{m_s}{q_s}$ Opponent bids medion amounts. Outbid his average amount, stay just under high amount.
- $x > 0$ Opponent bids low amounts. Outbid his average amount.

We categorize the opponent's strategy into these categories:
- Simple_Aggressive: Bids high or very high amounts as well as sometimes very low amounts
- Simple_Conservative: Bids medium or low amounts
- TitForTat: Bids close to our last bid
- Random: Bids random amounts across all categories

# Adapting to the opponent's strategy
According to the opponent's strategy, we adapt our own strategy:
- Simple_Aggressive: See amount categories above
- Simple_Conservative: See amount categories above
- TitForTat: Bid very low amounts, then slowly increase the bid, then drop back to zero.
- Random: Calculate average and bid slightly above it.

# Last rounds
If win condition is not reached, use all the money to win the last rounds.
Use knowledge of opponent's available money.

# Second Analysis

My Bidder performs well against the default strategies:
a - SimpleBidder
b - RandomBidder
c - TitForTatBidder
d - RandomBidder2
e - SimpleHighBidder
f - MyBidder

  | a | b | c | d | e | f |
a |  0| 64|  0| 60|  0|  0|
b | 36|  0| 48| 40| 11| 30|
c |100| 52|  0| 57|100|  0|
d | 40| 60| 43|  0|  0| 18|
e |100| 89|  0|100|  0|  0|
f |100| 70|100| 82|100|  0|

I'd like to question if this strategy is actually any good against other sophisticated strategies,
but it performs good enough against the default strategies.
Especially against RandomBidder and RandomBidder2, there is room for improvement, as they are not always being beaten.