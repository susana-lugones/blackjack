package oop.practical.blackjack.solution;

import oop.practical.blackjack.lisp.Ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Commands {

    private final Game game = new Game();

    public String execute(Ast ast) {
        assert ast instanceof Ast.Function;
        var function = (Ast.Function) ast;
        switch (function.name()) {
            case "do" -> {
                return function.arguments().stream()
                        .map(this::execute)
                        .filter(r -> !r.isEmpty())
                        .collect(Collectors.joining("\n"));
            }
            case "deck" -> {
                assert function.arguments().stream().allMatch(a -> a instanceof Ast.Atom);
                var atoms = function.arguments().stream().map(a -> ((Ast.Atom) a).name()).toList();
                return deck(atoms);
            }
            case "deal" -> {
                assert function.arguments().stream().allMatch(a -> a instanceof Ast.Atom);
                var atoms = function.arguments().stream().map(a -> ((Ast.Atom) a).name()).toList();
                return deal(atoms);
            }
            case "hit" -> {
                assert function.arguments().isEmpty();
                return hit();
            }
            case "stand" -> {
                assert function.arguments().isEmpty();
                return stand();
            }
            case "split" -> {
                assert function.arguments().isEmpty();
                return split();
            }
            case "double-down", "doubleDown" -> {
                assert function.arguments().isEmpty();
                return doubleDown();
            }
            case "inspect" -> {
                assert function.arguments().size() == 1 && function.arguments().getFirst() instanceof Ast.Atom;
                var name = ((Ast.Atom) function.arguments().getFirst()).name();
                return inspect(name);
            }
            default -> throw new AssertionError(function.name());
        }
    }

    public String deck(List<String> cards) {
        if (cards.isEmpty()) {
            game.getDeck().setDeck(Collections.emptyList());
        }
        else {
            List<Card> currentCard = cards.stream().map(Card::parse).collect(Collectors.toList());
            game.getDeck().setDeck(currentCard);
        }
        return "Deck: " + game.getDeck().toString();
    }

    public String deal(List<String> cards) {
        if ((cards.isEmpty() && game.getDeck().size() < 2) || (!cards.isEmpty() && cards.size() < 2)) {
            game.setLastError("Error");
            return "Error";
        }

        if (!cards.isEmpty()) {
            List<Card> customCards = cards.stream().map(Card::parse).collect(Collectors.toList());
            game.getDeck().setDeck(customCards);
        }

        try {
            if (game.getDeck().size() < 2) {
                throw new IllegalStateException("Not enough cards to deal.");
            }

            if (game.checkForBlackjackTie()) {
                return "Tie: Both player and dealer have Blackjack.";
            }

            game.getPlayer().hit(game.getDeck().deal());
            game.getDealer().addCard(game.getDeck().deal());
            game.getPlayer().hit(game.getDeck().deal());
            game.getDealer().addCard(game.getDeck().deal());
        } catch (IllegalStateException e) {
            game.setLastError("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }

        return "Deal successful.";
    }

    public String hit() {
        if (game.isGameOver()) {
            game.setLastError("Cannot hit after the game is resolved.");
            return "Error: Cannot hit after the game is resolved.";
        }

        if (game.getPlayer().getHand().getCards().isEmpty() || game.getDealer().getCards().isEmpty()) {
            game.setLastError("Cannot hit before cards are dealt.");
            return "Error: Cannot hit before cards are dealt.";
        }

        Deck deck = game.getDeck();
        if (deck.isEmpty()) {
            game.setLastError("Cannot hit because the deck is empty.");
            return "Error: The deck is empty.";
        }

        Card currentCard = deck.deal();
        game.getPlayer().getHand().addCard(currentCard);
        Hand playerHand = game.getPlayer().getHand();

        game.checkGameState();

        if (playerHand.isBusted()) {
            return "Player busts with: " + playerHand.toString();
        } else if (playerHand.getValue() == 21) {
            if (game.getDealer().getValue() == 21) {
                return "Player (21): " + playerHand.toString() + " (tied)\nDealer (21): " + game.getDealer().toString() + " (tied)";
            } else {
                return "Player hits 21 with: " + playerHand.toString();
            }
        } else {
            return "Player hand: " + playerHand.toString();
        }
    }

    public String stand() {
        if (game.getPlayer().getHand().getCards().isEmpty() || game.getDealer().getCards().isEmpty()) {
            game.setLastError("womp");
            return "womp";
        }

        if (game.isGameOver()) {
            game.setLastError("womp");
            return "womp";
        }

        this.game.checkGameState();
        this.game.isPlayerTurn = false;

        if (this.game.isGameOver()) {
            String theVerdict;
            Hand playerCurrent = this.game.getPlayer().getHand();
            Hand dealerCurrent = this.game.getDealer();

            if (playerCurrent.isBusted()) {
                theVerdict = "rip";
            }
            else if (dealerCurrent.isBusted()) {
                theVerdict = "rip";
            }
            else {
                int playerValue = playerCurrent.getValue();
                int dealerValue = dealerCurrent.getValue();
                if (playerValue > dealerValue) {
                    theVerdict = "yay";
                }
                else if (playerValue < dealerValue) {
                    theVerdict = "yay";
                }
                else {
                    theVerdict = "oh";
                }
            }
            return theVerdict;
        }
        else {
            return "rolling";
        }
    }

    public String split() {
        if (!game.getPlayer().canSplit() || game.getDeck().isEmpty()) {
            game.setLastError("Cannot split: Invalid state or empty deck.");
            return "Error: Cannot split.";
        }

        Hand playerHand = game.getPlayer().getHand();
        Card firstCard = playerHand.getCards().get(0);
        Card secondCard = playerHand.getCards().get(1);

        Hand newHand1 = new Hand();
        newHand1.addCard(firstCard);
        newHand1.addCard(game.getDeck().deal());

        Hand newHand2 = new Hand();
        newHand2.addCard(secondCard);
        newHand2.addCard(game.getDeck().deal());

        game.updatePlayerHandsAfterSplit(newHand1, newHand2);

        return "Split successful.";
    }

    public String doubleDown() {
        if (game.getPlayer().getHand().getCards().size() != 2 || !game.isPlayerTurn()) {
            game.setLastError("womp womp");
            return inspect("error");
        }
        if (game.getDeck().isEmpty()) {
            game.setLastError("womp womp");
            return inspect("error");
        }

        Card currentCard = game.getDeck().deal();
        game.getPlayer().getHand().addCard(currentCard);
        game.isPlayerTurn = false;
        game.checkGameState();

        return "wompUP";
    }

    public String inspect(String name) {
        switch (name) {
            case "deck" -> {
                if (game.getDeck().isEmpty()) {
                    return "Deck: (empty)";
                }
                else {
                    return "Deck: " + game.getDeck().toString();
                }
            }
            case "player" -> {
                int oneOrTwo = game.getCurrentHandIndex();
                StringBuilder yupYUP = new StringBuilder();
                List<Hand> totalHands = game.getPlayerHands();

                for (int index = 0; index < totalHands.size(); ++index) {
                    Hand currentlyShowing = totalHands.get(index);
                    String whyNot = getString(currentlyShowing, index, oneOrTwo);
                    String finalVar = currentlyShowing.getCards().stream().map(Card::toString).collect(Collectors.joining(", "));
                    yupYUP.append(String.format("Player (%d): %s (%s)\n", currentlyShowing.getValue(), finalVar, whyNot));
                }
                return yupYUP.toString().trim();
            }
            case "dealer" -> {
                boolean current = true;

                Hand forDeal = game.getDealer();
                List<Hand> allPlayer = game.getPlayerHands();
                StringBuilder theFix = new StringBuilder();

                if (game.getPlayer().getHand().hasBlackjack() && forDeal.hasBlackjack() && allPlayer.size() == 1) {
                    String currentDeals = current ? String.valueOf(forDeal.getValue()) : "? + " + forDeal.getCards().get(1).rank().toString();
                    String cardsShow = current ? forDeal.getCards().stream().map(Card::toString).collect(Collectors.joining(", ")) : "?, " + forDeal.getCards().get(1);

                    return String.format("Dealer (%s): %s (tied)", currentDeals, cardsShow, theFix);
                }

                for (Hand playerHand : allPlayer) {
                    if (playerHand.getValue() == 21 && playerHand.getCards().size() == 2) {
                        theFix.append("lost, ");
                    }
                    else if (!game.isGameOver()) {
                        theFix.append("waiting, ");
                        current = false;
                    }
                    else if (!game.isGameOver() && game.isPlayerTurn() && playerHand == game.getCurrentPlayerHand()) {
                        theFix.append("waiting, ");
                        current = false;
                    }
                    else {
                        int playTotal = playerHand.getValue();

                        if (playerHand.isBusted()) {
                            theFix.append("won, ");
                        }
                        else if (playTotal < forDeal.getValue()) {
                            theFix.append("won, ");
                        }
                        else if (forDeal.isBusted() || playTotal > forDeal.getValue()) {
                            theFix.append("lost, ");
                        }
                        else {
                            theFix.append("tied, ");
                        }
                    }
                }

                if (!theFix.isEmpty()) {
                    theFix.delete(theFix.length() - 2, theFix.length());
                }

                String currentDeals = current ? String.valueOf(forDeal.getValue()) : "? + " + forDeal.getCards().get(1).rank().toString();
                String cardsShow = current ? forDeal.getCards().stream().map(Card::toString).collect(Collectors.joining(", ")) : "?, " + forDeal.getCards().get(1);

                return String.format("Dealer (%s): %s (%s)", currentDeals, cardsShow, theFix);
            }
            case "error" -> {
                if (game.lastError().isEmpty() || game.lastError() == null) {
                    return "";
                }
                else {
                    return "Error: " + game.lastError();
                }
            }
            default -> throw new UnsupportedOperationException("ERROR :/ " + name);
        }
    }

    private String getString(Hand hand, int index, int help) {
        String whyNot;

        if (hand.isBusted()) {
            whyNot = "busted";
        }

        else if (game.isGameOver()) {
            if (game.getDealer().isBusted() || hand.getValue() > game.getDealer().getValue()) {
                whyNot = "won";
            }
            else if (hand.getValue() < game.getDealer().getValue()) {
                whyNot = "lost";
            }
            else {
                whyNot = "tied";
            }
        }
        else if (hand.getValue() == 21 && hand.getCards().size() == 2) {
            if (game.isGameOver()) {
                if (game.getDealer().isBusted() || hand.getValue() > game.getDealer().getValue()) {
                    whyNot = "won";
                }
                else if (hand.getValue() < game.getDealer().getValue()) {
                    whyNot = "lost";
                }
                else {
                    whyNot = "tied";
                }
            }
            else {
                whyNot = (index == help) ? "playing" : "won";
            }
        }
        else {
            if (index < help) {
                whyNot = "resolved";
            }
            else if (index == help) {
                whyNot = "playing";
            }
            else {
                whyNot = "waiting";
            }
        }
        return whyNot;
    }

}


