package auction.bidders;

/**
 * Bidder that bids like SimpleBidder but higher
 * Tests AggressiveStrategy in MyBidder
 */
public class SimpleHighBidder extends AbstractBidder {
    private int bid;

    @Override
    public void init(int quantity, int cash) {
        super.init(quantity, cash);
        this.bid = (cash / quantity) * 3;
    }

    @Override
    public int placeBid() {
        return Math.min(bid, ownCash);
    }
}
