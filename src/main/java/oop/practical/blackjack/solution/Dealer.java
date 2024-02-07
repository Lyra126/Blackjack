package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<String> hand;
    private boolean isBust;
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

    public Dealer(){
        hand = new ArrayList<>();
    }

    public void addCard(String hand){
        this.hand.add(hand);
    }

    public void addCards(List<String> hand){
        this.hand.addAll(hand);
    }


    public List<String> getHand(){
        return hand;
    }

    public void clearHand(){
        this.hand.clear();
    }

    public void updateHandStatus() {
        int totalValue = calculateTotal();
        if (totalValue > 21) {
            isBust = true;
        } else {
            isBust = false;
        }
    }

    // Get the value of the card
    public int getValue(String card) {
        String rank = getRank(card);
        if (rank.equals("A")) {
            // For simplicity, consider Ace as 11
            return 11;
        } else if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
            // Face cards are worth 10 points
            return 10;
        } else {
            // Numeric cards are worth their face value
            return Integer.parseInt(rank);
        }
    }

    // Get the rank of the card
    public String getRank(String card) {
        // Assuming the card string has the rank followed by the suit (e.g., "2S" for 2 of Spades)
        return card.substring(0, card.length() - 1); // Extract the rank part
    }


    //calculate total value of hand
    public int calculateTotal(){
        int total = 0;
        int numberOfAces = 0;
        for (String card : hand) {
            total += getValue(card);
            if (getRank(card).equals("Ace")) {
                numberOfAces++;
            }
        }
        // Adjust the value of Aces if needed
        while (total > 21 && numberOfAces > 0) {
            total -= 10; // Treat Ace as 1 instead of 11
            numberOfAces--;
        }
        return total;
    }

    public String inspect() {


        StringBuilder dealerString = new StringBuilder("    Dealer: ");
        String shownCard = hand.isEmpty() ? "?" : String.valueOf(getValue(hand.get(1))); // Get the value of the second card (first visible card)
        int total = calculateTotal(); // Assuming you have a method to calculate the total value of the dealer's hand

        dealerString.append("(? + ").append(shownCard).append("): ");

        if (hand.isEmpty()) {
            dealerString.append("?");
        } else {
            dealerString.append("?, ").append(hand.get(1)); // Append the shown card value
        }

        dealerString.append(" (waiting)");

        return dealerString.toString();

    }

}
