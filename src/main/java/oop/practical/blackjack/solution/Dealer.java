package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> hand;
    private boolean isBust;
    private String status;

    public Dealer() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void addCards(List<Card> cards) {
        hand.addAll(cards);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        hand.clear();
    }

    public int totalCards(){
        return hand.size();
    }

    public void updateHandStatus() {
        int totalValue = calculateTotal();
        isBust = totalValue > 21;
    }

    // Get the value of the card
    public int getValue(Card card) {
        Card.Rank rank = card.getRank();
        if (rank == Card.Rank.ACE) {
            // For simplicity, consider Ace as 11
            return 11;
        } else if (rank == Card.Rank.JACK || rank == Card.Rank.QUEEN || rank == Card.Rank.KING) {
            // Face cards are worth 10 points
            return 10;
        } else {
            // Numeric cards are worth their face value
            return rank.ordinal() + 1;
        }
    }

    // Calculate total value of hand
    public int calculateTotal() {
        int total = 0;
        int numberOfAces = 0;
        for (Card card : hand) {
            if(card != null){
                total += getValue(card);
                if (card.getRank() == Card.Rank.ACE) {
                    numberOfAces++;
                }
            }
        }
        // Adjust the value of Aces if needed
        while (total > 21 && numberOfAces > 0) {
            total -= 10; // Treat Ace as 1 instead of 11
            numberOfAces--;
        }
        return total;
    }

    public boolean isBust() {
        return calculateTotal() > 21;
    }

    public void updateStatus(String status){
        this.status = status;
    }

    public String inspect() {
        StringBuilder dealerString = new StringBuilder("Dealer ");
        String shownCard = "?"; // Default to unknown card
        int total = calculateTotal();

        if (!hand.isEmpty() && (status.equals("waiting")  || status.equals("waiting, waiting") || status.equals("waiting, lost")) ){
            dealerString.append("(").append("? + ").append(hand.get(1).getValue()).append("): ");
            dealerString.append("?").append(", "); // Append the hidden card
            dealerString.append(hand.get(1)); // Append the shown card
        } else if (!hand.isEmpty()) {
            shownCard = String.valueOf(getValue(hand.get(1))); // Get the value of the second card (first visible card)
            dealerString.append("(").append(total).append("): ");
            dealerString.append(hand.get(0)).append(", "); // Append the hidden card
            dealerString.append(hand.get(1)); // Append the shown card

        }
        dealerString.append(" (").append(status).append(")");
        return dealerString.toString();
    }
}