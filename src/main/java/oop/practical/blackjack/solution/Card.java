package oop.practical.blackjack.solution;

public class Card {

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
    }

    public enum Suite {
        HEARTS, DIAMONDS, CLUBS, SPADES;
    }

    private final Rank rank;
    private final Suite suite;

    public Card(Rank rank, Suite suite) {
        this.rank = rank;
        this.suite = suite;
    }

    public static Card parse(String input) {
        Rank rank;
        Suite suite;

        //if rank is 10
        if(input.charAt(0) ==  '1') {
            if (input.length() < 3 || (input.charAt(1) != '0')) {
                throw new IllegalArgumentException("Invalid rank: " + input);
            }
            rank = Rank.TEN;
            switch (input.charAt(2)) {
                case 'H' -> suite = Suite.HEARTS;
                case 'D' -> suite = Suite.DIAMONDS;
                case 'C' -> suite = Suite.CLUBS;
                case 'S' -> suite = Suite.SPADES;
                default -> throw new IllegalArgumentException("Invalid suite: " + input.charAt(1));
            }
            return new Card(rank, suite);
        }

        switch (input.charAt(0)) {
            case 'A' -> rank = Rank.ACE;
            case '2' -> rank = Rank.TWO;
            case '3' -> rank = Rank.THREE;
            case '4' -> rank = Rank.FOUR;
            case '5' -> rank = Rank.FIVE;
            case '6' -> rank = Rank.SIX;
            case '7' -> rank = Rank.SEVEN;
            case '8' -> rank = Rank.EIGHT;
            case '9' -> rank = Rank.NINE;
            case 'J' -> rank = Rank.JACK;
            case 'Q' -> rank = Rank.QUEEN;
            case 'K' -> rank = Rank.KING;
            default -> throw new IllegalArgumentException("Invalid rank: " + input.charAt(0));
        }

        switch (input.charAt(1)) {
            case 'H' -> suite = Suite.HEARTS;
            case 'D' -> suite = Suite.DIAMONDS;
            case 'C' -> suite = Suite.CLUBS;
            case 'S' -> suite = Suite.SPADES;
            default -> throw new IllegalArgumentException("Invalid suite: " + input.charAt(1));
        }

        return new Card(rank, suite);
    }


    public Rank getRank() {
        return rank;
    }

    public Suite getSuite() {
        return suite;
    }

    public int getValue() {
        switch (rank) {
            case ACE:
                return 11; // Returning 11 for now, as it can be 1 or 11 depending on the player's hand
            case TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN:
                return rank.ordinal() + 1; // Ordinal value for numeric cards is their face value
            case JACK, QUEEN, KING:
                return 10; // Face cards have a value of 10
            default:
                return 0; // This should not happen if the input is valid
        }
    }

    @Override
    public String toString() {
        return getRankString() + getSuiteString();
    }

    private String getRankString() {
        switch (rank) {
            case ACE:
                return "A";
            case TWO:
                return "2";
            case THREE:
                return "3";
            case FOUR:
                return "4";
            case FIVE:
                return "5";
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case JACK:
                return "J";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            default:
                throw new IllegalStateException("Unexpected value: " + rank);
        }
    }

    private String getSuiteString() {
        switch (suite) {
            case HEARTS:
                return "H";
            case DIAMONDS:
                return "D";
            case CLUBS:
                return "C";
            case SPADES:
                return "S";
            default:
                throw new IllegalStateException("Unexpected value: " + suite);
        }
    }

}
