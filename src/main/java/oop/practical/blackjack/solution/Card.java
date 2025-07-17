package oop.practical.blackjack.solution;

public record Card(Rank rank, Suite suit) {

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }



    public static Card parse(String input) {
        String rankPart = input.length() == 3 ? "10" : input.substring(0, 1);
        String suitePart = input.substring(input.length() - 1);

        Rank rank = switch(rankPart) {
            case "A" -> Rank.ACE;
            case "2" -> Rank.TWO;
            case "3" -> Rank.THREE;
            case "4" -> Rank.FOUR;
            case "5" -> Rank.FIVE;
            case "6" -> Rank.SIX;
            case "7" -> Rank.SEVEN;
            case "8" -> Rank.EIGHT;
            case "9" -> Rank.NINE;
            case "10" -> Rank.TEN;
            case "J" -> Rank.JACK;
            case "K" -> Rank.KING;
            case "Q" -> Rank.QUEEN;
            default -> throw new IllegalStateException("Error: " + rankPart);
        };

        Suite suit = switch(suitePart) {
            case "H" -> Suite.HEARTS;
            case "C" -> Suite.CLUBS;
            case "D" -> Suite.DIAMONDS;
            case "S" -> Suite.SPADES;
            default -> throw new IllegalStateException("Error: " + suitePart);
        };

        return new Card(rank, suit);
    }

    enum Rank {
        ACE("A"),
        TWO("2"),
        THREE("3"),
        FOUR("4"),
        FIVE("5"),
        SIX("6"),
        SEVEN("7"),
        EIGHT("8"),
        NINE("9"),
        TEN("10"),
        JACK("J"),
        KING("K"),
        QUEEN("Q");

        private final String text;

        Rank(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    enum Suite {
        HEARTS("H"),
        CLUBS("C"),
        DIAMONDS("D"),
        SPADES("S");

        private final String text;

        Suite(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}