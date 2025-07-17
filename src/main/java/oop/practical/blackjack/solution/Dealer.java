package oop.practical.blackjack.solution;

public class Dealer {
    private final Hand hand;
    private Card holeCard;

    public Dealer() {
        this.hand = new Hand();
    }

    public void revealHoleCard() {
        if (holeCard != null) {
            hand.addCard(holeCard);
            holeCard = null;
        }
    }

    public boolean shouldHit() {
        return hand.getValue() <= 16;
    }

    public Hand getHand() {
        return hand;
    }

}


