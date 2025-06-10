package auction.bidders;

import auction.Bidder;

/**
 * Some common functionality for bidders.
 * Keeps track of quantity and cash.
 */
public abstract class AbstractBidder implements Bidder {
    /*+
     * The initial quantity units that are available for bidding.
     */
    protected int startQuantity;
    /**
     * The initial cash available for bidding.
     */
    protected int startCash;
    /**
     * The number of QU owned by this bidder.
     */
    protected int ownQuantity = 0;
    /**
     * The MU remaining for this bidder.
     */
    protected int ownCash;
    /**
     * The number of QU owned by the other bidder.
     */
    protected int otherQuantity = 0;
    /**
     * The MU remaining for the other bidder.
     */
    protected int otherCash;

    @Override
    public void init(int quantity, int cash) {
        this.startQuantity = quantity;
        this.startCash = cash;
        this.ownCash = cash;
        this.otherCash = cash;
        this.ownQuantity = 0;
        this.otherQuantity = 0;
    }

    @Override
    public void bids(int own, int other) {
        ownCash -= own; // Deduct the own bid from cash
        otherCash -= other; // Deduct the other bid from cash
        if (own > other) {
            ownQuantity += 2;
        } else if (own == other) {
            ownQuantity += 1;
            otherQuantity += 1;
        } else {
            otherQuantity += 2;
        }
    }
}
