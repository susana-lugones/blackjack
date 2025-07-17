package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (!cards.isEmpty()) {
            return cards.removeFirst();
        } else {
            return null;
        }
    }

    public void setDeck(List<Card> customCards) {
        cards.clear();
        if (customCards.isEmpty()) {
            for (Card.Suite suit : Card.Suite.values()) {
                for (Card.Rank rank : Card.Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
            shuffle();
        }
        else {
            cards.addAll(customCards);
        }
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return cards.stream().map(Card::toString).collect(Collectors.joining(", "));
    }
}
