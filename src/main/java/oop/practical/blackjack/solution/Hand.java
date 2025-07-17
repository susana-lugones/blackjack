package oop.practical.blackjack.solution;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    @Override
    public String toString() {
        return cards.stream()
                .map(Card::toString)
                .collect(Collectors.joining(", "));
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getValue() {
        int total = 0;
        int forAce = 0;

        for (Card card : cards) {
            switch (card.rank()) {
                case TWO -> total += 2;
                case THREE -> total += 3;
                case FOUR -> total += 4;
                case FIVE -> total += 5;
                case SIX -> total += 6;
                case SEVEN -> total += 7;
                case EIGHT -> total += 8;
                case NINE -> total += 9;
                case TEN, JACK, QUEEN, KING -> total += 10;
                case ACE -> forAce++;
            }
        }

        for (int i = 0; i < forAce; i++) {
            if (total + 11 > 21) {
                total += 1;
            } else {
                total += 11;
            }
        }

        return total;
    }

    public boolean isBusted() {
        return getValue() > 21;
    }

    public boolean hasBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

}