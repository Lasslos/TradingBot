package auction;

public class BidderData {
    private int quantity;
    private int cash;

    public BidderData(int quantity, int cash) {
        this.quantity = quantity;
        this.cash = cash;
    }

    /**
     * Processes an auction round.
     * <p>
     * This method updates the bidder's cash and quantity based on the bids made in the round.
     * @param own   the bid of this bidder
     * @param other the bid of the other bidder
     */
    public void processBids(int own, int other) {
        // Deduct the own bid from cash
        cash -= own;
        assert cash >= 0 : "Bidder cannot overdraw their cash";

        if (own > other) {
            // If own bid is higher, we win both units
            quantity += 2;
        } else if (own == other) {
            // If bids are equal, we win one unit
            quantity += 1;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCash() {
        return cash;
    }

    @Override
    public String toString() {
        return "BidderData{" +
                "quantity=" + quantity +
                ", cash=" + cash +
                '}';
    }
}
