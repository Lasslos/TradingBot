package auction.bidders;

import auction.bidders.mybidder.OpponentStrategyCategorizer;

public class MyBidder extends AbstractBidder {
    OpponentStrategyCategorizer categorizer;
    @Override
    public void init(int quantity, int cash) {
        super.init(quantity, cash);
        categorizer = new OpponentStrategyCategorizer(quantity, cash);
    }

    @Override
    public int placeBid() {
        return 0;
    }

    @Override
    public void bids(int own, int other) {
        super.bids(own, other);
        categorizer.bids(own, other);
    }
}
