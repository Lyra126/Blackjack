package oop.practical.blackjack.solution;
import java.util.Collections;
import java.util.Random;

import oop.practical.blackjack.lisp.Ast;

import java.util.List;
import java.util.stream.Collectors;

public final class Commands {
    private Deck deck;
    private final Player player;
    private final Player player2;
    private final Dealer dealer;
    private String error;

    public Commands(){
        deck = new Deck();
        player = new Player();
        //only used when split is called
        player2 = new Player();
        dealer = new Dealer();
        error = "";
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
        error = "";
        if (cards.isEmpty()) {
            deck = new Deck();
            for (Card.Suite suite : Card.Suite.values()) {
                for (Card.Rank rank : Card.Rank.values()) {
                    deck.addCard(new Card(rank, suite).toString());
                }
            }
            long seed = System.nanoTime(); // Use current time as seed
            Collections.shuffle(deck.getCards(), new Random(seed)); // Shuffle the deck with a random seed
        } else
            deck.setDeck(cards);
        deck.updateStatus();
        return "Deck Initialized";

    }

    public String deal(List<String> cards) {
        player.clearHands();
        dealer.clearHand();

        //if there has been no list of cards provided and the deck has cards, deal them
        if (cards.isEmpty() && !deck.isEmpty()) {
            int deckSize = deck.getSize();
            for (int i = 0; i < 4; i++) {
                Card card = deck.dealCard();
                if(card != null ) {
                    if (i % 2 == 0 )
                        player.addCard(card);
                    else
                        dealer.addCard(card);
                }
            }
        } else if (!cards.isEmpty()) { // Deal cards from the provided list
            int cardSize = cards.size();
            if(cardSize < 4){
                error = "Cannot deal 4 alternating cards to dealer and player";
                for (int i = 0; i < cardSize; i++) {
                    Card card = Card.parse(cards.get(i));
                    if ((i % 2 == 0))
                        player.addCard(card);
                    else
                        dealer.addCard(card);
                }

                return "Initial cards cannot be dealt";
            }
            for (int i = 0; i < 4; i++) {
                Card card = Card.parse(cards.get(i));
                if(card!=null) {
                    if ((i % 2 == 0) )
                        player.addCard(card);
                    else
                        dealer.addCard(card);
                } else {
                    error = "Error: Invalid card format";
                    return "Invalid card format";
                }
            }
            for(int i = 4; i < cardSize ; i++){
                deck.addCard(Card.parse(cards.get(i)));
            }
        } else {
            error = "Error: The deck is empty and thus cards cannot be dealt";
            return "Deck is empty";
        }

        // Update statuses
        player.updateHandStatus();
        dealer.updateHandStatus();
        player.hasWon(dealer);
        return "Initial cards dealt";
    }

    public String hit() {
        error = "";
        if((player.getStatus().equals("won")  || player.getStatus().equals("lost") || player.getStatus().equals("busted")) && !player2.hasCards()){
            error = "Error: Game ended already";
            return "Game already ended";
        }
        if(deck.getSize() == 0){
            error = "Error: No cards available to hit";
            return "No cards available to hit";
        }
        if(!deck.isEmpty()) {
            //if player 1 already hit, then it's player2's turn
            player.updateHandStatus();
            if(player.totalCards() % 2 == 1 && player2.totalCards() % 2 == 0) {
                player2.addCard(deck.dealCard());
                if (!deck.isEmpty() && dealer.calculateTotal() <= 16)
                    dealer.addCard(deck.dealCard());
                player2.updateHandStatus();
                player.hit(dealer, player2);
                deck.updateStatus();

            } else{
                player.addCard(deck.dealCard());
                player.updateHandStatus();
                if (!deck.isEmpty() && dealer.calculateTotal() <= 16)
                   dealer.addCard(deck.dealCard());
                if (player2.hasCards())
                    player.hit(dealer, player2);
                else {
                    player.hit(dealer);
                    deck.updateStatus();
                }
            }
        }
        return "Player hits";
    }

    public String stand() {
        error = "";
        //no other action has been called yet
        if(deck.isEmpty() && player.totalCards() == 0 && dealer.totalCards() == 0) {
            error = "Stand cannot be the first action";
            return "Invalid Action";
        }
        if(player.getStatus().equals("resolved") || player2.getStatus().equals("resolved") || player.getStatus().equals("won") || player2.getStatus().equals("won")|| player.getStatus().equals("lost") || player2.getStatus().equals("lost")){
            error = "Stand action cannot be used after resolved";
            return "Invalid Action";
        }
        while(!deck.isEmpty()) {
            dealer.addCard(deck.dealCard());
            deck.updateStatus();
        }
        if(player2.hasCards())
            player.stand(dealer, player2);
        else
            player.stand(dealer);
        deck.updateStatus();
        return "Player Stands";
    }

    public String split() {
        error = "";
        if(player.canSplit() && deck.getSize()>=2) {
            player2.clearHands();
            if (player.totalCards() == 2) {
                player.split(player2);
                player.addCard(deck.dealCard());
                player2.addCard(deck.dealCard());
                player.updateHandStatus();
                player2.updateHandStatus();
                player.splitWin(dealer, player2);
            } else {
                player.split(player2);
                player.updateHandStatus();
                player2.updateHandStatus();
                player.splitWin(dealer, player2);
            }
            return "Hand split";
        }
        error = "Error: Can't Split";
        return "Can't split";
    }

    public String doubleDown() {
        error = "";


        if(player.totalCards() > 2){
            error = "Cannot double down after hit";
            return "Invalid Action";
        }

        if(!deck.isEmpty() || player.hasCards()) {
            if(!deck.isEmpty())
                player.addCard(deck.dealCard());
            else{
                error = "Error: No cards to deal";
                return "No cards to deal";
            }
            player.updateHandStatus();
            player.doubleDown(dealer);
            deck.updateStatus();
            return "Player doubles down, comparing hands";
        }
        if(deck.isEmpty()){
            error = "Error: No cards to deal";
            return "No cards to deal";
        }
        if(player.totalCards() != 2){
            error = "Error: player needs to have 2 cards to play this action";
            return "Player needs to have 2 cards to play this action";
        }
        error = "Error: Unable to play double-down";
        return "Unable to play double-down";
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
            case "error":
                yield error;
            default:
                yield "\n";
        };
    }
}
