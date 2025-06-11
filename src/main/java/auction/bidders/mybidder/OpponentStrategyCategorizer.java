package auction.bidders.mybidder;

import java.util.*;

public class OpponentStrategyCategorizer {
    private List<Integer> ownBids;
    private List<Integer> otherBids;

    private int initialQuantity;
    private int initialCash;

    private final static double TIT_FOR_TAT_THRESHOLD = 0.8;
    private final static double SIMPLE_AGGRESSIVE_THRESHOLD = 0.8;
    private final static double SIMPLE_CONSERVATIVE_THRESHOLD = 0.8;

    public OpponentStrategyCategorizer(int quantity, int cash) {
        this.initialQuantity = quantity;
        this.initialCash = cash;
        this.ownBids = new LinkedList<>();
        this.otherBids = new LinkedList<>();
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
    }

    /**
     * Analyzes the bids to categorize the opponent's strategy.
     *
     * @return The categorized strategy of the opponent.
     */
    public OpponentStrategy categorize() {
        if (ownBids.size() < 2) {
            return OpponentStrategy.UNKNOWN; // Not enough data to categorize
        }

        // Check for simple aggressive strategy
        if (isTitForTat() > TIT_FOR_TAT_THRESHOLD) {
            return OpponentStrategy.TIT_FOR_TAT;
        }
        if (isSimpleAggressive() > SIMPLE_AGGRESSIVE_THRESHOLD) {
            return OpponentStrategy.SIMPLE_AGGRESSIVE;
        }
        if (isSimpleConservative() > SIMPLE_CONSERVATIVE_THRESHOLD) {
            return OpponentStrategy.SIMPLE_CONSERVATIVE;
        }
        return OpponentStrategy.RANDOM;
    }

    /**
     * Checks if the opponent is using a tit-for-tat strategy.
     * @return 0, if it's definitely not a tit-for-tat strategy, 1 if it's a perfect tit-for-tat strategy
     */
    private double isTitForTat() {
        if (ownBids.size() < 2) {
            return 0;
        }
        // average of how close two consecutive bids are
        double result = 0.0;
        // Compare lists, offset by one
        Iterator<Integer> ownBidsIt = ownBids.subList(1, ownBids.size()).iterator();
        Iterator<Integer> otherBidsIt = otherBids.subList(0, otherBids.size() - 1).iterator();

        // Iterate over matches
        for (int i = 0; i < ownBids.size() - 1; i++) {
            int ownBid = ownBidsIt.next();
            int otherBid = otherBidsIt.next();
            int averageBid = 2 * initialCash / initialQuantity;

            // A bid is a perfect match (=1.0) if the same amount was bet.
            // A bid is not a match if there is a difference of half the average bid size.
            // In between, linear curve.
            result += Math.min(1.0 - (Math.abs(ownBid - otherBid) / (averageBid / 2.0)), 0.0);
        }
        result /= ownBids.size() - 1;
        return result;
    }

    /**
     * Check if the opponent is using a simple aggressive strategy.
     *
     * @return A value between 0 and 1 indicating the degree of aggressiveness.
     */
    private double isSimpleAggressive() {
        int[] frequencies = getBidFrequencies();
        int nonAggressiveCount = frequencies[2] + frequencies[3];
        int aggressiveCount = frequencies[0] + frequencies[1];
        // If there are more than 50% bids that are very high or high, we consider it aggressive.
        if (nonAggressiveCount >= ownBids.size() / 2) {
            return 0.0; // Not aggressive
        }

        // Linear interpolation between 0 and 1 based on the ratio of aggressive to total bids
        return (double) aggressiveCount / ownBids.size();
    }

    /**
     * Check if the opponent is using a simple conservative strategy.
     *
     * @return A value between 0 and 1 indicating the degree of conservativeness.
     */
    private double isSimpleConservative() {
        int[] frequencies = getBidFrequencies();
        int nonConservativeCount = frequencies[0] + frequencies[1];
        int conservativeCount = frequencies[2] + frequencies[3];
        // If there are more than 50% bids that are medium or low, we consider it conservative.
        if (nonConservativeCount >= ownBids.size() / 2) {
            return 0.0; // Not conservative
        }

        // Linear interpolation between 0 and 1 based on the ratio of conservative to total bids
        return (double) conservativeCount / ownBids.size();
    }

    /**
     * Counts the number of very high, high, medium and low bids.
     * Very high bid: > initialCash / (Math.ceil((initialQuantity + 1) / 4))
     * High bid: > 2 * initialCash / initialQuantity
     * Medium bid: > initialCash / initialQuantity
     * Low bid: > 0
     *
     * @return A 4-element array where each element is the count of bids in the respective category.
     */
    private int[] getBidFrequencies() {
        int[] result = new int[4];
        int veryHighThreshold = initialCash / (int) Math.ceil((initialQuantity + 1) / 4.0);
        int highThreshold = 2 * initialCash / initialQuantity;
        int mediumThreshold = initialCash / initialQuantity;
        for (int bid : otherBids) {
            if (bid > veryHighThreshold) {
                result[0]++;
            } else if (bid > highThreshold) {
                result[1]++;
            } else if (bid > mediumThreshold) {
                result[2]++;
            } else if (bid > 0) {
                result[3]++;
            }
        }
        return result;
    }
}
