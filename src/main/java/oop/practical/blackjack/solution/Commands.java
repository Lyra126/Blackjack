package oop.practical.blackjack.solution;

import oop.practical.blackjack.lisp.Ast;

import java.util.List;
import java.util.stream.Collectors;

public final class Commands {

    private Deck deck;
    private final Player player;
    private final Dealer dealer;

    public Commands(){
        deck = new Deck();
        player = new Player();
        dealer = new Dealer();
    }

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
            deck = new Deck(); // Initialize an empty deck
            // Add a standard deck of cards to the deck
            for (Card.Suite suite : Card.Suite.values()) {
                for (Card.Rank rank : Card.Rank.values()) {
                    deck.addCard(new Card(rank, suite).toString());
                }
            }
        } else {
            // Convert strings to Card objects and add them to the deck
            deck.setDeck(cards);
        }
        // Update the status of the deck
        deck.updateStatus();
        return "Deck Initialized";
    }



    public String deal(List<String> cards) {
        player.clearHands();
        dealer.clearHand();
        if (cards.isEmpty() && !deck.isEmpty()) {
            // Deal cards alternatively to player and dealer
            for (int i = 0; i < 4; i++) {
                if (i % 2 == 0) {
                    player.addCard(deck.dealCard());
                } else {
                    dealer.addCard(deck.dealCard());
                }
            }

            // Update hand status
            player.updateHandStatus();
            dealer.updateHandStatus();
            deck.updateStatus();
            player.hasWon(dealer);

            return "Initial cards dealt";
        } else if (cards.isEmpty() ) {
            return "Error: Deck is empty";
        }

        // Deal cards alternatively to player and dealer
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 0) {
                player.addCard(Card.parse(cards.get(i)));
            } else {
                dealer.addCard(Card.parse(cards.get(i)));
            }
        }
        for(int i = 4 ; i < cards.size(); i++){
            deck.addCard(cards.get(i));
        }

        // Update hand status
        player.updateHandStatus();
        dealer.updateHandStatus();
        deck.updateStatus();
        player.hasWon(dealer);

        return "Initial cards dealt";
    }


    public String hit() {
        player.addCard(deck.dealCard());
        player.hit(dealer);
        deck.updateStatus();
        return "Player hits";
    }

    public String stand() {
        player.stand(dealer);
        deck.updateStatus();
        return "Player Stands";
    }

    public String split() {
        if(!player.canSplit()){
            return "Error: Hand cannot be split";
        }
        player.split();
        return "Hand split";
    }

    public String doubleDown() {
        player.addCard(deck.dealCard());
        player.updateHandStatus();
        player.doubleDown(dealer);
        deck.updateStatus();
        return "Player doubles down, comparing hands";
    }

    public String inspect(String name) {
        return switch (name) {
            case "deck" -> deck.inspect();
            case "player" -> player.inspect();
            case "dealer" -> dealer.inspect();
            default -> "\n";
        };
    }
}
