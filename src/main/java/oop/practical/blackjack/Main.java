package oop.practical.blackjack;

import oop.practical.blackjack.lisp.Lisp;
import oop.practical.blackjack.lisp.ParseException;
import oop.practical.blackjack.solution.Commands;

import java.util.Scanner;

public final class Main {

    public static void main(String[] args) {
        var commands = new Commands();
        var scanner = new Scanner(System.in);
        while (true) {
            var input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            try {
                var ast = Lisp.parse(input);
                System.out.println(ast);
                System.out.println(commands.execute(ast));
            } catch (ParseException e) {
                System.out.println("Error parsing input: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected exception: " + e.getMessage());
            }
        }
    }

}


/*
2. **Player Class**:
   - The `Player` class remains similar, representing a player in the game. It should hold a list of strings representing the player's hand.

3. **Dealer Class**:
   - The `Dealer` class remains similar, representing the dealer in the game. It should hold a list of strings representing the dealer's hand.

4. **Commands Class**:
   - The `Commands` class interprets player commands and orchestrates the game flow. It initializes the game, deals cards, allows players to perform actions, and inspects the game state.

Here's how you can adjust the logic:

1. **Initialization**:
   - The `Commands` class initializes the game by creating instances of the `Deck`, `Player`, and `Dealer` classes. It populates the deck with a standard set of cards.

2. **Dealing Cards**:
   - The `Commands` class handles the "deal" command, which deals initial cards to the player and the dealer by drawing cards from the deck and adding them to the respective hands.

3. **Player Actions**:
   - The `Commands` class interprets player commands such as hitting, standing, splitting, and doubling down. It then calls the appropriate methods on the `Player` class to carry out these actions. For example, when the player hits, a card is drawn from the deck and added to the player's hand.

4. **Dealer Actions**:
   - After the player has completed their actions, the `Dealer` class should play its hand according to the rules of Blackjack. This might involve hitting until a certain value is reached (e.g., 17). Similar to the player, the dealer draws cards from the deck and adds them to its hand until it decides to stand.

5. **Game Outcome**:
   - Once both the player and the dealer have completed their actions, the `Commands` class should determine the outcome of the game based on the total values of their hands.

6. **Game State Inspection**:
   - The `Commands` class allows inspection of different parts of the game state, such as the deck, player's hand, or dealer's hand. This is done by accessing the corresponding lists in the `Deck`, `Player`, and `Dealer` classes.

By making these adjustments, you can still implement the Blackjack game logic without needing a `Card` class. Instead, you'll directly manipulate lists of strings representing cards.
 */
