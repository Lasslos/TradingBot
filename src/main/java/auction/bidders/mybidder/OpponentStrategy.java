package auction.bidders.mybidder;

/**
 * Category of strategies that the opponent can use.
 * See docs for details.
 */
public enum OpponentStrategy {
    SIMPLE_AGGRESSIVE,
    SIMPLE_CONSERVATIVE,
    TIT_FOR_TAT,
    RANDOM,
    UNKNOWN,
}
