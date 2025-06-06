package bidders;

import auction.Bidder;

/**
 * Places the same bid + 1 as the other bidder in the previous round.
 */
public class TitForTatBidder implements Bidder {
    private int quantity;
    private int cash;

    // Initialize to -1 in first round. This way, the first bid will be 0.
    private int lastOtherBid = -1;

    @Override
    public void init(int quantity, int cash) {
        this.cash = cash;
        this.quantity = quantity;
    }

    @Override
    public int placeBid() {
        // If we can't afford our usual bid, bid all cash that is left.
        return Math.min(lastOtherBid + 1, cash);

    }

    @Override
    public void bids(int own, int other) {
        quantity -= 2;
        cash -= own;
        this.lastOtherBid = other;
    }
}
