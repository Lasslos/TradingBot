package bidders;

import auction.Bidder;

/**
 * There will be [quantity / 2] bidding rounds. Bid [cash / quantity * 2] in each round.
 */
public class SimpleBidder implements Bidder {
    private int bid;

    @Override
    public void init(int quantity, int cash) {
        this.bid = (cash / quantity) * 2;
    }

    @Override
    public int placeBid() {
        return bid;
    }

    @Override
    public void bids(int own, int other) {
    }
}
