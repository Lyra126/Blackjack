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
        if (isBust) {
            status = "busted";
        } else {
            status = "playing";
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
        if (card == null || card.isEmpty()) {
            // Handle the case where the card is null or empty
            // Return an appropriate default value or throw an exception
            // For example, you can return an empty string or throw an IllegalArgumentException
            throw new IllegalArgumentException("Card cannot be null or empty");
        }
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

    public boolean canSplit() {
        // Check if the player has exactly two cards in their hand and if both cards have the same rank
        return hand.size() == 2 && getRank(hand.get(0)).equals(getRank(hand.get(1)));
    }

    public List<String> split() {
        // Split the hand into two separate hands
        List<String> splitHand = new ArrayList<>();
        splitHand.add(hand.remove(1)); // Remove the second card and add it to the new hand
        return splitHand;
    }

    public String inspect() {
        StringBuilder playerString = new StringBuilder("    Player: ");
        String total = String.valueOf(calculateTotal());; // Assuming you have a method to calculate the total value of the player's hand

        playerString.append("(").append(total).append("): ");

        for (String card : hand) {
            playerString.append(card).append(", ");
        }

        // Remove the trailing comma and space
        if (playerString.length() > "    Player: ".length()) {
            playerString.delete(playerString.length() - 2, playerString.length());
        }

        playerString.append(" (").append(status).append(")"); // Assuming you have a variable to store the player's status

        return playerString.toString();
    }
}

