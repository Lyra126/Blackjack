package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand; // List to store player's hands
    private boolean isBust; // Flag to indicate if the player is bust
    private String status;

    public Player() {
        hand = new ArrayList<>();
        isBust = false;
    }

    public void addCard(Card card) {
        hand.add(card); // Add the card to the first hand
        updateBustStatus();
    }

    public void addCards(List<Card> cards) {
        for (Card c : cards)
            addCard(c); // Add the cards to the first hand
        updateBustStatus();
    }

    public void clearHands() {
        hand.clear();
        isBust = false; // Reset bust status when clearing hands
    }

    private void updateBustStatus() {
        isBust = isHandBust(hand); // Determine bust status based on the single hand
    }

    private boolean isHandBust(List<Card> hand) {
        int totalValue = calculateTotal(hand);
        return totalValue > 21;
    }


    private int calculateTotal(List<Card> hand) {
        int total = 0;
        int numberOfAces = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.getRank() == Card.Rank.ACE) {
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

    public void updateHandStatus() {
        // Update bust status for each hand
        if (isHandBust(hand)) {
            isBust = true;
            return; // No need to continue checking other hands if one hand is bust
        }
        // If none of the hands are bust, set isBust to false
        isBust = false;
    }

    public boolean canSplit() {
        // Check if the player has exactly two cards in their hand and if both cards have the same rank
        if (hands.get(0).size() != 2) {
            return false;
        }

        Card firstCard = hands.get(0).get(0);
        Card secondCard = hands.get(0).get(1);
        return firstCard.getRank() == secondCard.getRank();
    }

    public String split() {
        // Check if the player can split their hand
        if (!canSplit()) {
            return "Error: Hand cannot be split";
        }

        // Create a new hand for splitting
        List<Card> newHand = new ArrayList<>();
        newHand.add(hands.get(0).remove(1)); // Remove the second card from the original hand and add it to the new hand

        // Add the new hand to the player's hands
        hands.add(newHand);

        return "Hand split";
    }


    public void hasWon(Dealer dealer) {
        // Implement logic to determine if the player has won
        int playerTotal = calculateTotal(hand);
        int dealerTotal = dealer.calculateTotal();
        //if player busted
        if(playerTotal > 21 ){
            status = "busted";
            dealer.updateStatus("won");
        } else if(playerTotal == 21){
            status = "won";
            dealer.updateStatus("lost");
        } else if(dealerTotal == 21){
            status = "lost";
            dealer.updateStatus("won");
        }else {
            status = "playing";
            dealer.updateStatus("waiting");
        }
    }
    public void doubleDown(Dealer dealer){
        int playerTotal = calculateTotal(hand);
        int dealerTotal = dealer.calculateTotal();

        if(playerTotal > dealerTotal){
            status = "won";
            dealer.updateStatus("lost");
        } else  if(playerTotal < dealerTotal){
            status = "lost";
            dealer.updateStatus("won");
        } else {
            status = "playing";
            dealer.updateStatus("waiting");
        }

    }

    public void stand(Dealer dealer){
        int playerTotal = calculateTotal(hand);
        int dealerTotal = dealer.calculateTotal();

        if(playerTotal > dealerTotal){
            status = "won";
            dealer.updateStatus("lost");
        } else  if(playerTotal < dealerTotal){
            status = "lost";
            dealer.updateStatus("won");
        } else {
            status = "playing";
            dealer.updateStatus("waiting");
        }

    }

    public void hit(Dealer dealer){
        int playerTotal = calculateTotal(hand);
        int dealerTotal = dealer.calculateTotal();
        if(playerTotal > 21) {
            status = "busted";
            dealer.updateStatus("won");
        } else if(playerTotal == 21) {
            status = "won";
            dealer.updateStatus("lost");
        }else{
            status = "playing";
            dealer.updateStatus("waiting");
        }
    }



    public String inspect() {
        StringBuilder playerString = new StringBuilder();
        playerString.append("Player ");

        int total = calculateTotal(hand);

        playerString.append("(").append(total).append("): ");

        for (Card card : hand) {
            playerString.append(card.toString()).append(", ");
        }

        // Remove the trailing comma and space
        if (!playerString.isEmpty()) {
            playerString.delete(playerString.length() - 2, playerString.length());
        }

        // Append bust status

        playerString.append(" (").append(status).append(")");

        return playerString.toString();
    }
}