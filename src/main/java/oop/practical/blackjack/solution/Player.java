package oop.practical.blackjack.solution;

import java.util.List;

public class Player {
    private final Hand hand;

    public Hand getHand() {
        return hand;
    }

    public Player() {
        this.hand = new Hand();
    }

    public void hit(Card card) {
        hand.addCard(card);
    }

    public boolean canSplit() {
        List<Card> forCards = hand.getCards();
        return forCards.size() == 2 && forCards.get(0).rank().equals(forCards.get(1).rank());
    }

}

