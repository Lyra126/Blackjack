package oop.practical.blackjack.solution;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand; // List to store player's hands
    private boolean isBust; // Flag to indicate if the player is bust
    private String status;

    public boolean hasCards() { return !(hand.isEmpty()); }
    private void updateStatus(String status) { this.status = status; }
    private void updateBustStatus() { isBust = isHandBust(hand); }
    public int totalCards() { return hand.size(); }

    public Player() {
        hand = new ArrayList<>();
        isBust = false;
    }

    public void addCard(Card card) {
        hand.add(card);
        updateBustStatus();
    }

    public void addCards(List<Card> cards) {
        for (Card c : cards)
            addCard(c);
        updateBustStatus();
    }

    public void clearHands() {
        hand.clear();
        isBust = false;
    }

    private boolean isHandBust(List<Card> hand) {
        int totalValue = calculateTotal();
        return totalValue > 21;
    }

    private int calculateTotal() {
        int total = 0;
        int numberOfAces = 0;
        for (Card card : hand) {
            total += card.getValue();
            if (card.rank() == Card.Rank.ACE)
                numberOfAces++;
        }

        while (total > 21 && numberOfAces > 0) {
            total -= 10;
            numberOfAces--;
        }
        return total;
    }

    public void updateHandStatus() {
        if (isHandBust(hand)) {
            isBust = true;
            return;
        }
        isBust = false;
    }

    public Boolean canSplit(){
        if(totalCards() > 2)
            return true;
        return false;
    }

    public String split(Player player2) {
        for (int i = 1; i < hand.size(); i ++)
            player2.addCard(hand.remove(i));
        return "Hand split";
    }

    public void splitWin(Dealer dealer, Player player2){
        int player1Total = calculateTotal();
        int player2Total = player2.calculateTotal();
        int dealerTotal = dealer.calculateTotal();

        if(player1Total > 21 ){
            updateStatus("busted");
            player2.updateStatus("playing");
            dealer.updateStatus("won, waiting");
        } else if(player1Total == 21){
            updateStatus("won");
            if(player2Total == 21)
                player2.updateStatus("won");
            else
                player2.updateStatus("playing");
            dealer.updateStatus("lost, waiting");
        } else if(player2Total > 21 ){
            updateStatus("playing");
            player2.updateStatus("busted");
            dealer.updateStatus("waiting, won");
        } else if(player2Total == 21){
            if(player1Total == 21)
                updateStatus("won");
            else
                updateStatus("playing");
            player2.updateStatus("won");
            dealer.updateStatus("waiting, lost");
        }else if(dealerTotal == 21){
            updateStatus("lost");
            player2.updateStatus("lost");
            dealer.updateStatus("won, won");
        }else {
            updateStatus("playing");
            player2.updateStatus("waiting");
            dealer.updateStatus("waiting, waiting");
        }
    }


    public void hasWon(Dealer dealer) {
        int playerTotal = calculateTotal();
        int dealerTotal = dealer.calculateTotal();

        if(playerTotal > 21 ){
            updateStatus("busted");
            dealer.updateStatus("won");
        } else if(playerTotal == 21){
            updateStatus("won");
            dealer.updateStatus("lost");
        } else if(dealerTotal == 21){
            updateStatus("lost");
            dealer.updateStatus("won");
        }else {
            updateStatus("playing");
            dealer.updateStatus("waiting");
        }
    }
    public void doubleDown(Dealer dealer){
        int playerTotal = calculateTotal();
        int dealerTotal = dealer.calculateTotal();

        if(playerTotal > dealerTotal){
            updateStatus("won");
            dealer.updateStatus("lost");
        } else  if(playerTotal < dealerTotal){
            updateStatus("lost");
            dealer.updateStatus("won");
        } else {
            updateStatus("playing");
            dealer.updateStatus("waiting");
        }
    }

    public void stand(Dealer dealer){
        int playerTotal = calculateTotal();
        int dealerTotal = dealer.calculateTotal();

        if(playerTotal > dealerTotal){
            updateStatus("won");
            dealer.updateStatus("lost");
        } else  if(playerTotal < dealerTotal){
            updateStatus("lost");
            dealer.updateStatus("won");
        } else {
            updateStatus("playing");
            dealer.updateStatus("waiting");
        }
    }

    public void stand(Dealer dealer, Player player2){
        int player1Total = calculateTotal();
        int player2Total = calculateTotal();
        int dealerTotal = dealer.calculateTotal();

        if(player1Total >= 21) {
            if (player1Total == 21) {
                updateStatus("won");
                player2.updateStatus("playing");
                dealer.updateStatus("lost, waiting");
            }else {
                updateStatus("busted");
                player2.updateStatus("playing");
                dealer.updateStatus("won, waiting");
            }
        } else{
            updateStatus("resolved");
            player2.updateStatus("playing");
            dealer.updateStatus("waiting, waiting");
        }
    }

    public void hit(Dealer dealer){
        int playerTotal = calculateTotal();
        int dealerTotal = dealer.calculateTotal();
        if(playerTotal > 21) {
            updateStatus("busted");
            dealer.updateStatus("won");
        } else if(playerTotal == 21) {
            updateStatus("won");
            dealer.updateStatus("lost");
        }else{
            updateStatus("playing");
            dealer.updateStatus("waiting");
        }
    }


    public String inspect() {
        StringBuilder playerString = new StringBuilder();
        playerString.append("Player ");

        int total = calculateTotal();
        playerString.append("(").append(total).append("): ");
        for (Card card : hand)
            playerString.append(card.toString()).append(", ");
        if (!playerString.isEmpty())
            playerString.delete(playerString.length() - 2, playerString.length());
        playerString.append(" (").append(status).append(")");

        return playerString.toString();
    }
}