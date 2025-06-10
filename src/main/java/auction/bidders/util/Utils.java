package auction.bidders.util;

public class Utils {
    public static boolean isLastRound(int startQuantity, int ownQuantity, int otherQuantity) {
        return (startQuantity - ownQuantity - otherQuantity) == 1;
    }
}
