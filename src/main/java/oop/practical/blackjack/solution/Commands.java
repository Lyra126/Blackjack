package oop.practical.blackjack.solution;

import oop.practical.blackjack.lisp.Ast;

import java.util.ArrayList;
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

    // initializes the deck with the given list of cards or creates a new deck with a standard set of cards if the list is empty
    public String deck(List<String> cards) {
        if(cards.isEmpty()){
            deck = new Deck();
        } else {
            deck = new Deck(cards);
        }
        return "Deck Initialized";
    }

    //starts the game by dealing initial cards to the player(s) and the dealer, if cards are empty, should use the current deck to deal cards
    public String deal(List<String> cards) {
        if (cards.isEmpty())
            return "Error: No cards provided for dealing";

        // Clear hands before dealing new cards
        player.clearHand();
        dealer.clearHand();

        for (String card : cards) {
            player.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
        }

        // Update hand status
        player.updateHandStatus();
        dealer.updateHandStatus();

        return "Initial cards dealt";
    }

    // method allows the player to take an additional card from the deck, need to deal a card from the deck to the player's hand.
    public String hit() {
        player.addCard(deck.dealCard());
        return "Player hits";
    }

    // signifies that the player doesn't want any more cards
    public String stand() {
        return "Player Stands";
    }

    //allows the player to split their hand if the initial two cards have the same value
    public String split() {
        //split the hand into two separate hands and deal an additional card to each hand
        if(!player.canSplit()){
            return "Error: Hand cannot be split";
        }
        player.addCards(player.split());
        return "Hand split";
    }

    // allows the player to double their bet and receive one additional card
    public String doubleDown() {
        //deal one more card to the player's hand and possibly adjust their bet
        return "Player doubles down";
    }

    //returns the state of a specific part of the game, such as the deck, player's hand, or dealer's hand
    public String inspect(String name) {
        return switch (name) {
            case "deck" -> deck.inspect();
            case "player" -> player.inspect();
            case "dealer" -> dealer.inspect();
            default -> "Invalid inspection target";
        };
    }
}
