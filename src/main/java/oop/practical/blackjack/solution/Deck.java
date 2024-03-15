package oop.practical.blackjack.solution;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    private String status;

    public int getSize() { return cards.size(); }
    public List<Card> getCards() { return cards; }
    public boolean isEmpty(){
        return cards.isEmpty();
    }
    public Deck(List<Card> cards) { this.cards = new ArrayList<>(cards); }
    public Deck(){
        cards = new ArrayList<>();
        status = "(empty)";
    }

    public void setDeck(List<String> cardStrings) {
        this.cards = new ArrayList<>();
        for (String cardString : cardStrings)
            this.cards.add(Card.parse(cardString));
        updateStatus();
    }

    public Card dealCard(){
        if (cards.isEmpty())
            return null;
        return cards.removeFirst(); // Remove top card from the deck
    }

    public void updateStatus(){
        if (cards.isEmpty())
            status = "(empty)";
    }

    public void addCard(String string) {
        Card card = Card.parse(string);
        cards.add(card);
        updateStatus();
    }

    public void addCard(Card card) {
        cards.add(card);
        updateStatus();
    }

    public String inspect() {
        StringBuilder deckString = new StringBuilder("Deck: ");
        if(cards.isEmpty()) {
            deckString.append(status);
            return deckString.toString();
        }
        for (Card card : cards)
            deckString.append(card).append(", ");
        if (deckString.length() > "Deck: ".length())
            deckString.delete(deckString.length() - 2, deckString.length());
        return deckString.toString();
    }
}
