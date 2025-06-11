package auction.bidders.mybidder;

import auction.bidders.util.Utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class OpponentStrategyBidder {
    private final LinkedList<Integer> ownBids;
    private final LinkedList<Integer> otherBids;

    private final int initialQuantity;
    private final int initialCash;

    private int ownCash;

    private int otherCash;

    public OpponentStrategyBidder(int quantity, int cash) {
        this.initialQuantity = quantity;
        this.initialCash = cash;
        this.ownBids = new LinkedList<>();
        this.otherBids = new LinkedList<>();
        this.ownCash = initialCash;
        this.otherCash = initialCash;
    }

    /**
     * Records a bid made by the bidder and the opponent.
     *
     * @param own The bid made by this bidder.
     * @param other The bid made by the opponent.
     */
    public void bids(int own, int other) {
        ownBids.add(own);
        otherBids.add(other);
        ownCash -= own; // Update own cash after the bid
        otherCash -= other; // Update opponent's cash after the bid
    }

    /**
     * Returns the next bid based on the opponent's strategy.
     *
     * @param strategy The strategy of the opponent.
     * @return The next bid to be made. This bid is guaranteed to be non-negative and within the bounds of the bidder's cash.
     */
    public int getNextBid(OpponentStrategy strategy) {
        int nextBid = getNextBidUnsafe(strategy);
        // Clamp the bid to be non-negative and not exceed own cash, and if the opponent has not enough cash, just bid one more than their cash
        return Utils.clamp(0, nextBid, Math.min(ownCash, otherCash + 1));
    }

    /**
     * Gets next bid, but does not guarantee positive bids or bids below own money
     * @return The next unclamped bid
     */
    private int getNextBidUnsafe(OpponentStrategy strategy) {
        switch (strategy) {
            case UNKNOWN -> {
                return getNextUnknownBid();
            }
            case TIT_FOR_TAT -> {
                return getNextTitForTatBid();
            }
            case SIMPLE_AGGRESSIVE -> {
                return getNextSimpleAggressiveBid();
            }
            case SIMPLE_CONSERVATIVE -> {
                return getNextSimpleConservativeBid();
            }
            case RANDOM -> {
                return getNextRandomBid();
            }
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        }
    }

    private int getNextUnknownBid() {
        // Place low conservative bid
        int mediumBid = initialCash / initialQuantity;
        // Random bid, minimum is 0, maximum is double the medium bid
        return Math.min(
                (int) (new Random().nextGaussian(mediumBid, mediumBid / 3.0)),
                2 * mediumBid
        );
    }

    /**
     * Calculates a good next bid if the opponent uses tit-for-tat.
     * @return The next bid to be made
     */
    private int getNextTitForTatBid() {
        //If last bid is high, start from zero
        int highThreshold = 2 * initialCash / initialQuantity;
        if (ownBids.getLast() >= highThreshold) {
            return 0;
        }

        // Compare lists, offset by one
        Iterator<Integer> ownBidsIt = ownBids.subList(0, ownBids.size() - 1).iterator();
        Iterator<Integer> otherBidsIt = otherBids.subList(1, otherBids.size()).iterator();

        int averageOffset = 0;

        // Iterate over matches
        for (int i = 0; i < ownBids.size() - 1; i++) {
            int ownBid = ownBidsIt.next();
            int otherBid = otherBidsIt.next();
            averageOffset += Math.abs(ownBid - otherBid);
        }
        averageOffset /= ownBids.size() - 1;
        // Return (last bid + averageOffset) * (120%)
        return (int) ((ownBids.getLast() + averageOffset) * 1.2);
    }

    /**
     * Returns a good next bid if the opponent uses a simple aggressive strategy.
     *
     * @return The next bid to be made.
     */
    public int getNextSimpleAggressiveBid() {
        // See doc for details on the thresholds
        int highThreshold = 2 * initialCash / initialQuantity;
        int veryHighThreshold = initialCash / (int) Math.ceil((initialQuantity + 1) / 4.0);

        double averageHighBid = 0.0;
        double averageLowBid = 0.0;
        int numberOfHighBids = 0;

        for (int bid : otherBids) {
            if (bid >= highThreshold) {
                numberOfHighBids++;
                averageHighBid += bid;
            } else {
                averageLowBid += bid;
            }
        }

        averageHighBid = numberOfHighBids > 0 ? averageHighBid / numberOfHighBids : 0; // Avoid division by zero
        averageLowBid = numberOfHighBids < otherBids.size() ? averageLowBid / (otherBids.size() - numberOfHighBids) : 0; // Avoid division by zero

        // If the opponent is betting very high and still has a lot of cash, let him bleed out
        if (averageHighBid >= veryHighThreshold) {
            if (otherCash > initialCash / 2) {
                // if he sometimes does low bids, return more than his average low bid
                if (averageLowBid > 0) {
                    return (int) Math.ceil(averageLowBid * 1.2); // 20% more than average low bid
                } else {
                    return 0; // If no low bids, just bid 0
                }
            } else {
                // If the opponent is betting very high but has little cash, he cannot keep his very high bids up.
                // Take the last two otherBids and average them, then add a small offset.
                // Even if it still is very high, we now can afford to outbid him, and it's going to be lower.

                if (ownBids.size() < 2) {
                    return 0; // Not enough data to make a bid, this should never happen in practice, edge case
                }
                int lastBid = otherBids.getLast();
                int secondLastBid = otherBids.get(otherBids.size() - 2);
                return (int) Math.ceil(((lastBid + secondLastBid) / 2.0) * 1.2); // 120% more than the average of the last two bids
            }
        }

        // Otherwise averageHighBid >= highThreshold.
        // In this case, sometimes outbid the opponent, sometimes bid very low. See docs for details.

        // We can afford to outbid the opponent. To keep our cash, bet low sometimes.
        int highBid = (int) Math.ceil(averageHighBid * 1.2); // 20% more than the average high bid
        int roundsRemaining = initialQuantity / 2 - ownBids.size();
        // If we have less cash than the opponent, we need to be careful with our bids.
        if (ownCash < otherCash) {
            return 0;
        } else {
            return highBid; // We can afford to outbid the opponent
        }
    }
    public int getNextSimpleConservativeBid() {
        // Calculate average low bid based on the opponent's bids.
        int highThreshold = 2 * initialCash / initialQuantity;
        double averageLowBid = 0.0;
        for (int bid : otherBids) {
            if (bid <= highThreshold) {
                averageLowBid += bid;
            }
        }
        averageLowBid /= otherBids.size(); // Average of all low bids
        // Bid 120% of the average low bid. See docs for details.
        return (int) Math.ceil(averageLowBid * 1.2);
    }

    public int getNextRandomBid() {
        double averageBid = 0;
        for (int bid : otherBids) {
            averageBid += bid;
        }
        averageBid /= otherBids.size();
        // If we have less cash than the opponent, we need to be careful with our bids.
        if (ownCash < otherCash) {
            return 0;
        } else {
            // There is not much we can do if the opponent bets randomly but not in any range like in "AggressiveSimple" or "ConservativeSimple".
            // Just bid more, and if we don't have enough cash, bid zero. See docs for details.
            return (int) Math.ceil(averageBid * 1.2);
        }
    }
}
