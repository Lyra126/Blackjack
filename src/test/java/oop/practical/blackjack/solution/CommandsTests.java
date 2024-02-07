package oop.practical.blackjack.solution;

import oop.practical.blackjack.lisp.Lisp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public final class CommandsTests {

    @Nested
    public final class DeckTests {

        @ParameterizedTest
        @MethodSource
        public void testDeck(String name, String setup, String expected) {
            test(setup, "(inspect :deck)", expected);
        }

        private static Stream<Arguments> testDeck() {
            return Stream.of(
                Arguments.of("Single Card", """
                    (deck :2S)
                """, "Deck: 2S"),
                Arguments.of("Multiple Cards", """
                    (deck :2S :10H :AC :7D)
                """, "Deck: 2S, 10H, AC, 7D")
            );
        }

    }

    @Nested
    public final class DealTests {

        @ParameterizedTest
        @MethodSource
        public void testDeal(String name, String setup, String expected) {
            testState(setup, expected);
        }

        private static Stream<Arguments> testDeal() {
            return Stream.of(
                Arguments.of("Arguments", """
                    (deal :2S :10H :AC :7D)
                """, """
                    Deck: (empty)
                    Player: (13): 2S, AC (playing)
                    Dealer: (? + 7): ?, 7D (waiting)
                """),
                Arguments.of("No Arguments", """
                    (deck :2S :10H :AC :7D)
                    (deal)
                """, """
                    Deck: (empty)
                    Player: (13): 2S, AC (playing)
                    Dealer: (? + 7): ?, 7D (waiting)
                """),
                Arguments.of("Empty Deck", """
                    (deal)
                """, null)
            );
        }

    }

    private static void test(String setup, String command, String expected) {
        var commands = new Commands();
        Assertions.assertDoesNotThrow(() -> commands.execute(Lisp.parse("(do " + setup + ")")));
        if (expected != null) {
            var result = Assertions.assertDoesNotThrow(() -> commands.execute(Lisp.parse(command)));
            Assertions.assertEquals(expected, result);
        } else {
            var error = Assertions.assertDoesNotThrow(() -> commands.execute(Lisp.parse("(inspect :error)")));
            Assertions.assertNotEquals("", error);
        }
    }

    private static void testState(String setup, String expected) {
        test(setup, "(do (inspect :deck) (inspect :player) (inspect :dealer) (inspect :error))", expected);
    }

}
