package auction.bidders;

/**
 * This counts how many rounds it still needs to win, and either bets zero or a much higher amount.
 */
public class SwitchBidder extends AbstractBidder {


    @Override
    public void init(int quantity, int cash) {

    }

    @Override
    public int placeBid() {
        return 0;
    }

    @Override
    public void bids(int own, int other) {

    }
}
