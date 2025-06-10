package bidders;

import auction.Bidder;

/**
 * Works like {@link RandomBidder}, but re-calculates the average bid and standard deviation every time it places a bid.
 * Plus, it switches strategy if a win condition is met.
 */
public class RandomBidder2 implements Bidder {
    @Override
    public void init(int quantity, int cash) {

    }

    @Override
    public int placeBid() {
        return 0;
    }

    @Override
    public void bids(int own, int other) {

    }
}
