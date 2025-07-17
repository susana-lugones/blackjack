package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Hand> playerHands = new ArrayList<>();
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private String lastError;
    private int currentHandIndex = 0;
    boolean isPlayerTurn;

    public Game() {
        this.deck = new Deck();
        this.player = new Player();
        this.dealer = new Dealer();
        this.isPlayerTurn = true;
        this.lastError = "";
        this.playerHands.add(player.getHand());
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Hand getDealer() {
        return this.dealer.getHand();
    }

    public String lastError() {
        return this.lastError;
    }

    public boolean isPlayerTurn() {
        return this.isPlayerTurn;
    }

    public void setLastError(String toOut) {
        this.lastError = toOut;
    }

    public Hand getCurrentPlayerHand() {
        return this.playerHands.get(currentHandIndex);
    }

    public int getCurrentHandIndex() {
        return currentHandIndex;
    }

    public List<Hand> getPlayerHands() {
        return playerHands;
    }

    public void checkGameState() {
        if (this.player.getHand().getValue() == 21 || this.player.getHand().hasBlackjack()) {
            this.isPlayerTurn = false;
            if (this.dealer.getHand().getValue() >= 17 || !this.dealer.shouldHit()) {
                this.dealer.revealHoleCard();
            }
            else {
                while (this.dealer.shouldHit()) {
                    this.dealer.getHand().addCard(this.deck.deal());
                }
                this.dealer.revealHoleCard();
            }
        }
    }

    public boolean isGameOver() {
        if (player.getHand().isBusted() || dealer.getHand().isBusted()) {
            return true;
        }
        if (!isPlayerTurn && (!dealer.shouldHit() || dealer.getHand().getValue() >= 17)) {
            return true;
        }
        return this.player.getHand().getValue() == 21 || this.player.getHand().hasBlackjack() || !this.isPlayerTurn || this.dealer.getHand().getValue() == 21;
    }

    public void updatePlayerHandsAfterSplit(Hand hand1, Hand hand2) {
        playerHands.clear();
        playerHands.add(hand1);
        playerHands.add(hand2);
        currentHandIndex = 0;
        isPlayerTurn = true;
    }

    public boolean checkForBlackjackTie() {
        boolean playerHasBlackjack = this.player.getHand().hasBlackjack();
        boolean dealerHasBlackjack = this.dealer.getHand().hasBlackjack();

        if (playerHasBlackjack && dealerHasBlackjack) {
            System.out.println("It's a tie! Both player and dealer have Blackjack.");
            return true;
        }

        return false;
    }



}