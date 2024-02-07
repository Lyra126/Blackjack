package oop.practical.blackjack.solution;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<String> cards;

    public Deck(){
        // Generate a standard deck of cards
        cards = new ArrayList<>();
        String[] suits = {"S", "H", "C", "D"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(rank + suit);
            }
        }

        // Shuffle the deck to create a random permutation
        Collections.shuffle(cards);
    }

    public Deck(List<String> cards){
        this.cards = new ArrayList<>(cards);
    }

    public String dealCard(){
        if (cards.isEmpty()) {
            return null; // No cards left in the deck
        }
        return cards.remove(0); // Remove and return the top card from the deck
    }

    public String inspect() {
        StringBuilder deckString = new StringBuilder("Deck: ");
        for (String card : cards) {
            deckString.append(card).append(", ");
        }
        // Remove the trailing comma and space
        if (deckString.length() > "Deck: ".length()) {
            deckString.delete(deckString.length() - 2, deckString.length());
        }
        return deckString.toString();
    }

    public void shuffle(){
        Collections.shuffle(cards); // Shuffle the list of cards
    }
}
