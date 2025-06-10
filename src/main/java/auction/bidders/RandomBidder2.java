package auction.bidders;

import auction.bidders.util.WinConditionStrategy;

import java.util.Random;

import static auction.bidders.util.Utils.isLastRound;

/**
 * Works like {@link RandomBidder}, but re-calculates the average bid and standard deviation every time it places a bid.
 * Plus, it switches strategy if a win condition is met.
 */
public class RandomBidder2 extends AbstractBidder {
    @Override
    public int placeBid() {
        // If a win condition is met, switch to the win condition strategy
        if (WinConditionStrategy.isWinConditionMet(startQuantity, ownQuantity, ownCash, otherCash)) {
            return WinConditionStrategy.getNextBid(startQuantity, ownQuantity, ownCash, otherCash);
        }
        // In the last round, bid all remaining cash
        if (isLastRound(startQuantity, ownQuantity, otherQuantity)) {
            return ownCash; // Bid all remaining cash in the last round
        }

        int averageBid = (ownCash / (startQuantity - ownQuantity - otherQuantity)) * 2;
        int standardDeviation = averageBid / 2; // 50% of the average bid
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
