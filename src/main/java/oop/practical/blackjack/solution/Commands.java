package oop.practical.blackjack.solution;

import oop.practical.blackjack.lisp.Ast;

import java.util.List;
import java.util.stream.Collectors;

public final class Commands {

    private Deck deck;
    private final Player player;
    private final Player player2;
    private final Dealer dealer;

    public Commands(){
        deck = new Deck();
        player = new Player();
        player2 = new Player();
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
            for (int i = 0; i < deck.getSize(); i++) {
                Card card = deck.dealCard();
                if(card != null ) {
                    if (i % 2 == 0 || dealer.totalCards() == 2) {
                        player.addCard(card);
                    } else {
                        dealer.addCard(card);
                    }
                    System.out.println(player.inspect());
                    System.out.println(dealer.inspect());
                }
            }
        } else if (!cards.isEmpty()) {
            // Deal cards from the provided list
            for (int i = 0; i < cards.size(); i++) {
                if ((i % 2 == 0 ) || dealer.totalCards() >= 2){
                    Card playerCard = Card.parse(cards.get(i));
                    if (playerCard != null) {
                        player.addCard(playerCard);
                    } else {
                        // Handle the case where parsing fails
                        return "Error: Invalid card format";
                    }
                } else {
                    Card dealerCard = Card.parse(cards.get(i));
                    if (dealerCard != null) {
                        dealer.addCard(dealerCard);
                    } else {
                        // Handle the case where parsing fails
                        return "Error: Invalid card format";
                    }
                }
            }
        } else {
            return "Error: Deck is empty";
        }

        // Update hand status
        player.updateHandStatus();
        dealer.updateHandStatus();
        player.hasWon(dealer);

        return "Initial cards dealt";
    }



    public String hit() {
        if(!deck.isEmpty()) {
            player.addCard(deck.dealCard());
            player.hit(dealer);
            deck.updateStatus();
        }
        return "Player hits";
    }

    public String stand() {
        if(player2.hasCards())
            player.stand(dealer, player2);
        else
            player.stand(dealer);
        deck.updateStatus();
        return "Player Stands";
    }

    public String split() {
        player2.clearHands();
        if(player.hasCards()) {
            player.split(player2);
            player.updateHandStatus();
            player2.updateHandStatus();
            player.splitWin(dealer, player2);
        }
        return "Hand split";
    }

    public String doubleDown() {
        if(!deck.isEmpty() || player.hasCards()) {
            if(!deck.isEmpty())
                player.addCard(deck.dealCard());
            player.updateHandStatus();
            player.doubleDown(dealer);
            deck.updateStatus();
        }
        return "Player doubles down, comparing hands";
    }

    public String inspect(String name) {
        return switch (name) {
            case "deck":
                yield deck.inspect();
            case "player":
                StringBuilder players = new StringBuilder();
                players.append(player.inspect());
                if(player2.hasCards())
                    players.append("\n").append(player2.inspect());
                yield players.toString();
            case "dealer":
                yield dealer.inspect();
            default:
                yield "\n";
        };
    }
}
