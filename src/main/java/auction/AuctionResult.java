package auction;

/**
 * Represents the result of an auction between two bidders.
 * The result can be one of the following:
 * - BIDDER_1_WINS: Bidder 1 wins the auction.
 * - BIDDER_2_WINS: Bidder 2 wins the auction.
 * - TIE: The auction ends in a tie, where both bidders win one unit each.
 */
public enum AuctionResult {
    BIDDER_1_WINS,
    BIDDER_2_WINS,
    TIE;

    /**
     * Calculates the winner based on the final BidderData.
     */

    public static AuctionResult fromBidderData(BidderData bidder1Data, BidderData bidder2Data) {
        if (bidder1Data.getQuantity() != bidder2Data.getQuantity()) {
            return bidder1Data.getQuantity() > bidder2Data.getQuantity() ? BIDDER_1_WINS : BIDDER_2_WINS;
        } else if (bidder1Data.getCash() != bidder2Data.getCash()) {
            return bidder1Data.getCash() > bidder2Data.getCash() ? BIDDER_1_WINS : BIDDER_2_WINS;
        } else {
            return TIE;
        }
    }
}
