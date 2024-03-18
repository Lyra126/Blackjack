package oop.practical.blackjack.solution;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> hand;
    private boolean isBust;
    private String status;

    public Dealer() {
        hand = new ArrayList<>();
        status = "(waiting)";
        isBust = false;
    }
    public List<Card> getHand() { return hand; }
    public void addCard(Card card) {
        hand.add(card);
    }
    public void addCards(List<Card> cards) {
        hand.addAll(cards);
    }
    public void clearHand() {
        hand.clear();
    }
    public int totalCards() { return hand.size(); }
    public boolean isBust() { return calculateTotal() > 21; }
    public void updateStatus(String status) {this.status = status; }

    public void updateHandStatus() {
        int totalValue = calculateTotal();
        isBust = totalValue > 21;
    }

    public int getValue(Card card) {
        Card.Rank rank = card.rank();
        if (rank == Card.Rank.ACE)
            return 11;
        else if (rank == Card.Rank.JACK || rank == Card.Rank.QUEEN || rank == Card.Rank.KING)
            return 10;
        else
            return rank.ordinal() + 1;
    }

    public int calculateTotal() {
        int total = 0;
        int numberOfAces = 0;
        for (Card card : hand) {
            if(card != null){
                total += getValue(card);
                if (card.rank() == Card.Rank.ACE)
                    numberOfAces++;
            }
        }

        while (total > 21 && numberOfAces > 0) {
            total -= 10;
            numberOfAces--;
        }
        return total;
    }


    public String inspect() {
        StringBuilder dealerString = new StringBuilder("Dealer ");
        String shownCard = "?"; // Default to unknown card
        int total = calculateTotal();

        if (!hand.isEmpty() && (status.equals("waiting")  || status.equals("waiting, waiting")|| status.equals("won, waiting") || status.equals("lost, waiting") || status.equals("waiting, lost")) ){
            dealerString.append("(").append("? + ").append(hand.get(1).getValue()).append("): ");
            dealerString.append("?").append(", "); //append hidden card
            dealerString.append(hand.get(1));
        } else if (!hand.isEmpty()) {
            shownCard = String.valueOf(getValue(hand.get(1)));
            dealerString.append("(").append(total).append("): ");
            for (int i = 0; i < hand.size(); i++) {
                dealerString.append(hand.get(i));
                if (i < hand.size() - 1)
                    dealerString.append(", ");
            }
        }
        dealerString.append(" (").append(status).append(")");
        return dealerString.toString();
    }
}