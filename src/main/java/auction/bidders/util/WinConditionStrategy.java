package auction.bidders.util;


public class WinConditionStrategy {
    /**
     * Checks if a win condition is met.
     * @param startQuantity The total quantity of items available for bidding.
     * @param ownQuantity The quantity of items owned by the bidder.
     * @param ownCash The cash available to the bidder.
     * @param otherCash The cash available to the other bidder.
     * @return true if a win condition is met, false otherwise.
     */
    public static boolean isWinConditionMet(int startQuantity, int ownQuantity, int ownCash, int otherQuantity, int otherCash) {
        // First win condition: Own quantity is more than half of the total quantity
        if (2 * ownQuantity > startQuantity) {
            return true;
        }
        // Second win condition: Own quantity is half and own cash is more than the other bidder's cash
        if (2 * ownQuantity == startQuantity && ownCash > otherCash) {
            return true;
        }
        // How many quantity units are needed to get more than half of the total quantity
        int quantityNeeded = startQuantity / 2 - ownQuantity + 1;
        int quantityRemaining = startQuantity - ownQuantity - otherQuantity;
        if (quantityNeeded < quantityRemaining) {
            return false; // Not enough quantity remaining to win
        }
        // Third win condition: If we can bet more than the entire cash of the other bidder for [wonRoundsNeeded] rounds.
        int wonRoundsNeeded = (int) Math.ceil(quantityNeeded / 2.0); // Each round we can win 2 quantity units
        if (ownCash > (otherCash+1) * wonRoundsNeeded) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the next bid if a win condition is met.
     * @param startQuantity The total quantity of items available for bidding.
     * @param ownQuantity The quantity of items owned by the bidder.
     * @param ownCash The cash available to the bidder.
     * @param otherCash The cash available to the other bidder.
     * @return The next bid amount if a win condition is met.
     */
    public static int getNextBid(int startQuantity, int ownQuantity, int ownCash, int otherQuantity, int otherCash) {
        if (!isWinConditionMet(startQuantity, ownQuantity, ownCash, otherQuantity, otherCash)) {
            throw new IllegalStateException("Win condition not met, cannot calculate next bid.");
        }
        if (2 * ownQuantity > startQuantity) {
            return 0; // Already won, no next bid needed
        }
        if (2 * ownQuantity == startQuantity && ownCash > otherCash) {
            return 0; // Already won, no next bid needed
        }
        // We can bet more than the entire cash of the other bidder for [wonRoundsNeeded] rounds.
        return otherCash+1;
    }
}
