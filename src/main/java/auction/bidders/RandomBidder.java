package auction.bidders;

import java.util.Random;

/**
 * Bids random amounts with random numbers.
 * Average bid is [cash / quantity * 2].
 * Standard deviation is 50% of that.
 * <p>
 * This bidder does not use all of its cash in the end, even if it is clear that it can win the auction that way.
 * See {@link RandomBidder2} for a more aggressive version that uses all cash.
 */
public class RandomBidder extends AbstractBidder {
    private int averageBid;
    private int standardDeviation;

    @Override
    public void init(int quantity, int cash) {
        super.init(quantity, cash);
        this.averageBid = (cash / quantity) * 2;
        this.standardDeviation = averageBid / 2; // 50% of the average bid
    }

    @Override
    public int placeBid() {
        // Generate a random bid around the average bid with some variation
        int randomBid = (int) (new Random().nextGaussian(averageBid, standardDeviation));
        if (randomBid < 0) {
            randomBid = 0; // Ensure we always bid at least 0
        } else if (randomBid > ownCash) {
            randomBid = ownCash; // Ensure we do not bid more than we have
        }
        return randomBid;
    }
}
