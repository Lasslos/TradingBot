package bidders;

import auction.Bidder;

public class MyBidder implements Bidder {
    private int initialQuantity;
    private int initialCash;
    /**
     * The number of QU owned by this bidder.
     */
    private int ownQuantity = 0;
    /**
     * The MU remaining for this bidder.
     */
    private int ownCash;
    /**
     * The number of QU owned by the other bidder.
     */
    private int otherQuantity = 0;
    /**
     * The MU remaining for the other bidder.
     */
    private int otherCash;

    @Override
    public void init(int quantity, int cash) {
        this.initialQuantity = quantity;
        this.initialCash = cash;
        this.ownCash = cash;
        this.otherCash = cash;
    }

    @Override
    public int placeBid() {
        return 0;
    }

    @Override
    public void bids(int own, int other) {

    }
}
