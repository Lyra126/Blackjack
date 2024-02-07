package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<String> hand;
    private boolean isBust;
    private String status;
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

    public Player(){
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
        if (rank.equals("Ace")) {
            // For simplicity, consider Ace as 11
            return 11;
        } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
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
        StringBuilder playerString = new StringBuilder("Player: ");
        String total = String.valueOf(calculateTotal());; // Assuming you have a method to calculate the total value of the player's hand

        playerString.append("(").append(total).append("): ");

        for (String card : hand) {
            playerString.append(card).append(", ");
        }

        // Remove the trailing comma and space
        if (playerString.length() > "Player: ".length()) {
            playerString.delete(playerString.length() - 2, playerString.length());
        }

        playerString.append(" (").append(status).append(")"); // Assuming you have a variable to store the player's status

        return playerString.toString();
    }
}

