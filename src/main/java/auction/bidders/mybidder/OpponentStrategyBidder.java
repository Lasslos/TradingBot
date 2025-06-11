package auction.bidders.mybidder;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class OpponentStrategyBidder {
    private List<Integer> ownBids;
    private List<Integer> otherBids;

    private int initialQuantity;
    private int initialCash;

    public OpponentStrategyBidder(int quantity, int cash) {
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

    public int getNextBid(OpponentStrategy strategy) {
        switch (strategy) {
            case UNKNOWN -> {
                // Place low conservative bid
                int mediumBid = initialCash / initialQuantity;
                // Random bid, minimum is 0, maximum is double the medium bid
                return Math.max(mediumBid * 2, Math.min(0, (int) (new Random().nextGaussian(mediumBid, mediumBid / 3.0))));
                // Random bid, minimum is 0, maximum is double the medium bid
            }
            case TIT_FOR_TAT -> {
                return getNextTitForTatBid();
            }
            case SIMPLE_AGGRESSIVE -> {
                //TODO
                return 0;
                //TODO
            }
            case SIMPLE_CONSERVATIVE -> {
                //TODO
                return 0;
                //TODO
            }
            case RANDOM -> {
                //TODO
                return 0;
            }
        }
    }

    private int getNextTitForTatBid() {
        //TODO
        return 0;
    }
}
