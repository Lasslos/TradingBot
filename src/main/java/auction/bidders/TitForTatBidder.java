package auction.bidders;

/**
 * Places the same bid + 1 as the other bidder in the previous round.
 */
public class TitForTatBidder extends AbstractBidder {

    private int lastOtherBid = -1;

    @Override
    public void init(int quantity, int cash) {
        super.init(quantity, cash);
        // Initialize to -1 in first round. This way, the first bid will be 0.
        this.lastOtherBid = -1;
    }

    @Override
    public int placeBid() {
        // If we can't afford our usual bid, bid all cash that is left.
        return Math.min(lastOtherBid + 1, ownCash);

    }

    @Override
    public void bids(int own, int other) {
        super.bids(own, other);
        this.lastOtherBid = other;
    }
}
