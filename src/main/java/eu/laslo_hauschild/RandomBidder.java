package eu.laslo_hauschild;

import auction.Bidder;

import java.util.Random;

/**
 * Bids random amounts with random numbers.
 * Average bid is [cash / quantity * 2].
 * Standard deviation is 20% of that.
 */
public class RandomBidder implements Bidder {
    private int cash;
    private int quantity;

    private int averageBid;
    private int standardDeviation;

    @Override
    public void init(int quantity, int cash) {
        this.cash = cash;
        this.quantity = quantity;

        this.averageBid = (cash / quantity) * 2;
        this.standardDeviation = averageBid / 5; // 20% of the average bid
    }

    @Override
    public int placeBid() {
        // Generate a random bid around the average bid with some variation
        int randomBid = (int) (new Random().nextGaussian(averageBid, standardDeviation));
        return Math.min(randomBid, cash); // Ensure the bid does not exceed available cash
    }

    @Override
    public void bids(int own, int other) {
        cash -= own; // Deduct the own bid from cash
    }
}
