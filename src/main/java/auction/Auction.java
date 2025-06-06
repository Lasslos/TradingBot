package auction;

/**
 * Runs an auction with two bidders.
 */
public class Auction {
    private final Bidder bidder1;
    private final Bidder bidder2;
    // The total quantity of the product to be auctioned.
    // Must be positive and even.
    private final int initialQuantity;
    // The initial amount of money each bidder has.
    // Must be positive.
    private final int initialMoney;

    /**
     * Creates a new auction with two bidders.
     *
     * @param bidder1      the first bidder
     * @param bidder2      the second bidder
     * @param quantity     the total quantity of the product to be auctioned
     * @param initialMoney the initial amount of money each bidder has
     */
    public Auction(Bidder bidder1, Bidder bidder2, int quantity, int initialMoney) {
        this.bidder1 = bidder1;
        this.bidder2 = bidder2;
        this.initialQuantity = quantity;
        this.initialMoney = initialMoney;

        assert quantity % 2 == 0 : "Initial quantity must be even";
        assert quantity > 0 : "Initial quantity must be greater than zero";
        assert initialMoney > 0 : "Initial money must be greater than zero";
    }

    /**
     * Runs the auction.
     * <p>
     * This method initializes both bidders and runs the auction rounds.
     *
     * @param verbose If true, prints detailed information about each round.
     */
    public AuctionResult run(boolean verbose) {
        bidder1.init(initialQuantity, initialMoney);
        bidder2.init(initialQuantity, initialMoney);

        // Initial quantities and cash for both bidders
        BidderData bidder1Data = new BidderData(initialQuantity, initialMoney);
        BidderData bidder2Data = new BidderData(initialQuantity, initialMoney);
        // Auction rounds
        for (int round = 0; round < initialQuantity / 2; round++) {
            if (verbose) System.out.printf("Round %d/%d", round + 1, initialQuantity / 2);
            runRound(bidder1Data, bidder2Data, verbose);
        }
        AuctionResult result = AuctionResult.fromBidderData(bidder1Data, bidder2Data);
        if (verbose) printResults(result, bidder1Data, bidder2Data);
        return result;
    }

    /**
     * Runs a single round of the auction.
     * @param bidder1Data The data for bidder 1
     * @param bidder2Data The data for bidder 2
     * @param verbose If true, prints detailed information about the bids
     */
    public void runRound(BidderData bidder1Data, BidderData bidder2Data, boolean verbose) {
        // Get bids
        int bid1 = bidder1.placeBid();
        int bid2 = bidder2.placeBid();
        if (verbose) System.out.printf("Bidder 1 bids %d, Bidder 2 bids %d%n", bid1, bid2);
        assert bid1 >= 0 && bid2 >= 0 : "Bids must be non-negative";

        // Process bids
        bidder1Data.processBids(bid1, bid2);
        bidder2Data.processBids(bid2, bid1);

        // Notify bidders of the bids
        bidder1.bids(bid1, bid2);
        bidder2.bids(bid2, bid1);
    }

    /**
     * Prints the results of the auction.
     * @param bidder1Data The data for bidder 1
     * @param bidder2Data The data for bidder 2
     */
    public void printResults(AuctionResult result, BidderData bidder1Data, BidderData bidder2Data) {
        System.out.println("\n-- Auction Results --\n");
        System.out.printf("Bidder 1: %d QU, %d MU remaining%n", bidder1Data.getQuantity(), bidder1Data.getCash());
        System.out.printf("Bidder 2: %d QU, %d MU remaining%n", bidder2Data.getQuantity(), bidder2Data.getCash());
        switch (result) {
            case BIDDER_1_WINS:
                System.out.println("Bidder 1 wins!");
                break;
            case BIDDER_2_WINS:
                System.out.println("Bidder 2 wins!");
                break;
            case TIE:
                System.out.println("Tie!");
                break;
        }
    }
}

