import auction.Auction;
import auction.Bidder;
import auction.bidders.*;
import auction.bidders.mybidder.MyBidder;


public class Main {
    /**
     * How many QU are in each auction simulated below.
     */
    final static int INITIAL_QUANTITY = 30;
    /**
     * How much cash each bidder has in each auction simulated below.
     */
    final static int INITIAL_CASH = 300;
    /**
     * How many times each auction is repeated in the bidder arena.
     */
    final static int AUCTION_REPEATS = 5000;

    public static void main(String[] args) {
        runBidderArena();
    }

    /**
     * Tests a single auction with two bidders in verbose mode.
     */
    public static void runOneAuction() {
        Bidder bidder1 = new SimpleHighBidder();
        Bidder bidder2 = new MyBidder();
        Auction auction = new Auction(bidder1, bidder2, INITIAL_QUANTITY, INITIAL_CASH);
        auction.run(true);
    }

    /**
     * Runs a series of auctions between different bidders to compare their performance.
     * Each bidder is paired with every other bidder, and the results are stored in a matrix.
     */
    public static void runBidderArena() {
        Bidder[] bidders = {
            new SimpleBidder(),
            new RandomBidder(),
            new TitForTatBidder(),
            new RandomBidder2(),
            new SimpleHighBidder(),
            new MyBidder(),
        };

        // Stores how many points each bidder has against each other bidder
        // results[i][j] = points for bidder i against bidder j
        int[][] results = new int[bidders.length][bidders.length];

        for (int i = 0; i < bidders.length; i++) {
            Bidder bidder = bidders[i];

            // Pair this bidder with all other bidders in the list
            for (int j = i + 1; j < bidders.length; j++) {
                Bidder otherBidder = bidders[j];

                Auction auction = new Auction(bidder, otherBidder, INITIAL_QUANTITY, INITIAL_CASH);
                // Repeat the auction multiple times to get a better average result
                for (int k = 0; k < AUCTION_REPEATS; k++) {
                    switch (auction.run(false)) {
                        case BIDDER_1_WINS:
                            results[i][j] += 2;
                            break;
                        case BIDDER_2_WINS:
                            results[j][i] += 2;
                            break;
                        case TIE:
                            results[i][j] += 1;
                            results[j][i] += 1;
                            break;
                    }
                }
            }
        }

        printAuctionResults(bidders, results);
    }

    /**
     * Prints the results of the auction in a matrix format.
     *
     * @param bidders The array of bidders
     * @param results The results matrix
     */
    private static void printAuctionResults(Bidder[] bidders, int[][] results) {
        System.out.println("\n-- Auction Results --\n");
        // Print bidders
        for (int i = 0; i < bidders.length; i++) {
            System.out.printf("%c - %s\n", 'a' + i, bidders[i].getClass().getSimpleName());
        }
        // Print the results as a matrix
        System.out.print("\n  |");
        for (int i = 0; i < bidders.length; i++) {
            System.out.printf(" %c |", 'a' + i);
        }
        for (int i = 0; i < bidders.length; i++) {
            System.out.print("\n");
            System.out.printf("%c |", 'a' + i);
            for (int j = 0; j < bidders.length; j++) {
                /// Print the results out of 100
                System.out.printf("%3d|", Math.round((results[i][j] / (AUCTION_REPEATS * 2.0)) * 100.0));
            }
        }
    }
}
