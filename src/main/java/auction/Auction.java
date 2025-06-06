package auction;

import eu.laslo_hauschild.SimpleBidder;
import eu.laslo_hauschild.TitForTatBidder;

public class Auction {
    public static void main(String[] args) {
        int quantity = 10; // quantity units
        int money = 100; // monetary units

        Bidder bidder1 = new SimpleBidder();
        Bidder bidder2 = new TitForTatBidder();

        runAuction(bidder1, bidder2, quantity, money);
    }

    /**
     * Runs an auction with two bidders.
     * <p>
     * Both bidders pay their bid. Two QU will be auctioned in each round.
     * The QU goes to the higher bidder. If both bidders bid the same, both recieve one QU.
     *
     * @param bidder1 the first bidder
     * @param bidder2 the second bidder
     * @param quantity the total QU to be auctioned, must be even
     * @param money   the initial MU each bidder has
     */
    public static void runAuction(Bidder bidder1, Bidder bidder2, int quantity, int money) {
        assert quantity % 2 == 0 : "Quantity must be even";
        assert quantity > 0 : "Quantity must be greater than zero";
        assert money > 0 : "Money must be greater than zero";

        bidder1.init(quantity, money);
        bidder2.init(quantity, money);

        int bidder1Cash = money;
        int bidder2Cash = money;
        int bidder1Quantity = 0;
        int bidder2Quantity = 0;

        // Auction rounds
        for (int round = 0; round < quantity / 2; round++) {
            // Get bids
            int bid1 = bidder1.placeBid();
            int bid2 = bidder2.placeBid();
            assert bid1 >= 0 && bid2 >= 0 : "Bids must be non-negative";

            // Ensure bids do not exceed available cash
            bidder1Cash -= bid1;
            bidder2Cash -= bid2;
            assert bidder1Cash >= 0 && bidder2Cash >= 0 : "Bidders cannot overdraw their cash";

            // See who wins the QU
            if (bid1 > bid2) {
                bidder1Quantity += 2; // Bidder 1 wins both QU
            } else if (bid2 > bid1) {
                bidder2Quantity += 2; // Bidder 2 wins both QU
            } else {
                bidder1Quantity++; // Both bidders win one QU each
                bidder2Quantity++;
            }

            // Notify bidders of the bids
            bidder1.bids(bid1, bid2);
            bidder2.bids(bid2, bid1);

            System.out.printf("Round %d: Bidder 1 bids %d, Bidder 2 bids %d%n", round + 1, bid1, bid2);
        }

        System.out.println("\n-- Auction Results --\n");
        System.out.printf("Bidder 1: %d QU, %d MU remaining%n", bidder1Quantity, bidder1Cash);
        System.out.printf("Bidder 2: %d QU, %d MU remaining%n", bidder2Quantity, bidder2Cash);
        if (bidder1Quantity == bidder2Quantity) {
            if (bidder1Cash == bidder2Cash) {
                System.out.println("Tied!");
            }
            System.out.printf("Tied on QU. Bidder %d wins the tiebreak!\n", bidder1Cash > bidder2Cash ? 1 : 2);
        } else {
            System.out.printf("Bidder %d wins!\n", bidder1Quantity > bidder2Quantity ? 1 : 2);
        }

    }
}