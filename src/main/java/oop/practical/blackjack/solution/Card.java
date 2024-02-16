package oop.practical.blackjack.solution;

public record Card(Rank rank, Suite suite) {

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
    }

    public enum Suite {
        HEARTS, DIAMONDS, CLUBS, SPADES;
    }

    @Override
    public String toString() {
        return getRankString() + getSuiteString();
    }

    public static Card parse(String input) {
        Rank rank;
        Suite suite;

        if (input.charAt(0) == '1') {
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


    public int getValue() {
        return switch (rank) {
            case ACE -> 11;
            case TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN -> rank.ordinal() + 1;
            case JACK, QUEEN, KING -> 10;
            default -> 0;
        };
    }


    private String getRankString() {
        return switch (rank) {
            case ACE -> "A";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
            default -> throw new IllegalStateException("Unexpected value: " + rank);
        };
    }

    private String getSuiteString() {
        return switch (suite) {
            case HEARTS -> "H";
            case DIAMONDS -> "D";
            case CLUBS -> "C";
            case SPADES -> "S";
            default -> throw new IllegalStateException("Unexpected value: " + suite);
        };
    }
}
