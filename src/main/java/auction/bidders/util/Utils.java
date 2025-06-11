package auction.bidders.util;

import java.util.List;

public class Utils {
    public static boolean isLastRound(int startQuantity, int ownQuantity, int otherQuantity) {
        return (startQuantity - ownQuantity - otherQuantity) == 1;
    }

    /**
     * Returns [value], if it fufills low <= value <= high. Returns the broken boundary otherwise
     * @param low the lower bound
     * @param value the value
     * @param high the higher bound
     * @return the value if it's within bounds, or the broken bound.
     */
    public static int clamp(int low, int value, int high) {
        assert low <= high : "low must be less than high!";
        return Math.max(low, Math.min(value, high));
    }
}
