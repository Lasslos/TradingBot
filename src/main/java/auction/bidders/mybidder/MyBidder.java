package auction.bidders.mybidder;

import auction.bidders.AbstractBidder;
import auction.bidders.util.WinConditionStrategy;

public class MyBidder extends AbstractBidder {
    OpponentStrategyCategorizer categorizer;
    OpponentStrategyBidder bidder;
    @Override
    public void init(int quantity, int cash) {
        super.init(quantity, cash);
        categorizer = new OpponentStrategyCategorizer(quantity, cash);
        bidder = new OpponentStrategyBidder(quantity, cash);
    }

    @Override
    public int placeBid() {
        // If a win condition is met, switch to the win condition strategy
        if (WinConditionStrategy.isWinConditionMet(startQuantity, ownQuantity, ownCash, otherQuantity, otherCash)) {
            return WinConditionStrategy.getNextBid(startQuantity, ownQuantity, ownCash, otherQuantity, otherCash);
        }

        // Categorize the opponent's strategy
        OpponentStrategy strategy = categorizer.categorize();
        return bidder.getNextBid(strategy);
    }

    @Override
    public void bids(int own, int other) {
        super.bids(own, other);
        categorizer.bids(own, other);
        bidder.bids(own, other);
    }
}
