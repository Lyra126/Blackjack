package oop.practical.blackjack.solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private String status;

    public Deck(){
        // Generate a standard deck of cards
        cards = new ArrayList<>();
        status = "(empty)";
        //initializeDeck();
        //shuffle();
    }

    public Deck(List<Card> cards){
        this.cards = new ArrayList<>(cards);
    }

    public void setDeck(List<String> cardStrings) {
        this.cards = new ArrayList<>();
        for (String cardString : cardStrings) {
            this.cards.add(Card.parse(cardString));
        }
        updateStatus();
    }

    public int getSize(){
        return cards.size();
    }

    private void initializeDeck() {
        for (Card.Suite suite : Card.Suite.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suite));
            }
        }
    }

    public Card dealCard(){
        if (cards.isEmpty()) {
            return null; // No cards left in the deck
        }
        return cards.removeFirst(); // Remove and return the top card from the deck
    }

    public void updateStatus(){
        if (cards.isEmpty()) {
            status = "(empty)";
        }
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public String inspect() {
        StringBuilder deckString = new StringBuilder("Deck: ");
        if(cards.isEmpty()) {
            deckString.append(status);
            return deckString.toString();
        }
        for (Card card : cards) {
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

    public void addCard(String string) {
        Card card = Card.parse(string); // Parse the input string to create a Card object
        cards.add(card); // Add the card to the deck
        updateStatus(); // Update the status of the deck after adding the card
    }

}
