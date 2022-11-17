import static org.junit.Assert.assertEquals;

import tictactoe.*;

import java.io.StringReader;
import java.util.Arrays;
import org.junit.Test;

/**
 * Test cases for the tic tac toe controller, using mocks for readable and appendable.
 */
public class TicTacToeControllerTest {


  @Test
  public void testSingleValidMove() {
    TicTacToe m = new TicTacToeModel();
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(new StringReader("2 2 q"), gameLog);
    c.playGame(m);

    assertEquals("   |   |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "Enter a move for X:\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   | X |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "Enter a move for O:\n"
        + "Game quit! Ending game state:\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   | X |  \n"
        + "-----------\n"
        + "   |   |  \n", gameLog.toString());

  }

  /**
   * Test If not valid data is provided for Row
   */
  @Test
  public void testBogusInputAsRow() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("!#$ 2 q");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);

    // split the output into an array of lines
    String[] lines = gameLog.toString().split("\n");
    // check that it's the correct number of lines
    assertEquals(13, lines.length);
    // check that the last 6 lines are correct
    String lastMsg = String.join("\n",
        Arrays.copyOfRange(lines, lines.length - 6, lines.length));
    assertEquals("Game quit! Ending game state:\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   |   |  \n"
        + "-----------\n"
        + "   |   |  ", lastMsg);
    // note no trailing \n here, because of the earlier split
  }

  /**
   * Test the output of a tie game
   */
  @Test
  public void testTieGame() {
    TicTacToe m = new TicTacToeModel();
    // note the entire sequence of user inputs for the entire game is in this one string:
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
    String[] lines = gameLog.toString().split("\n");
    assertEquals(60, lines.length);
    assertEquals("Game is over! Tie game.", lines[lines.length - 1]);
  }

  /**
   * Test a Failing Appendable Error
   */
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    TicTacToe m = new TicTacToeModel();
    StringReader input = new StringReader("2 2 1 1 3 3 1 2 1 3 2 3 2 1 3 1 3 2");
    Appendable gameLog = new FailingAppendable();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);
  }

  // Play game to completion, where there is a winner
  @Test
  public void testWinnerX () {
    String lastMsg = this.runGameHelper("1 1 1 2 1 3 2 1 2 3 1 3 3 1 2 2 3 2 3 3", 0);
    assertEquals(
            " X | O | X\n" +
            "-----------\n" +
            " O | X | X\n" +
            "-----------\n" +
            " O | O | X\n" +
            "Game is over! X wins.", lastMsg);
  }

  // Play game to completion, where there is a winner
  @Test
  public void testTie () {
    String lastMsg = this.runGameHelper("2 2 2 2 1 1 1 1 3 3 3 3 1 2 1 2 1 3 1 3 2 3 2 3 2 1 2 1 3 1 3 1 3 2 3 2", 0);
    assertEquals(
            " O | O | X\n" +
                    "-----------\n" +
                    " X | X | O\n" +
                    "-----------\n" +
                    " O | X | X\n" +
                    "Game is over! Tie game.", lastMsg);
  }

  // PlayGame, where there is a winner O
  @Test
  public void testWinnerO () {

    String lastMsg = this.runGameHelper("1 1 1 2 1 3 1 1 1 2 1 3 2 1 3 3 2 2 3 2 2 3 2 2", 0);
    assertEquals(
            " X | O | X\n" +
                    "-----------\n" +
                    " O | O | O\n" +
                    "-----------\n" +
                    "   | X | X\n" +
                    "Game is over! O wins.", lastMsg);
  }


  // Input where the q comes instead of an integer for the row
  @Test
  public void testQInRow () {

    String lastMsg = this.runGameHelper("2 2 q 2 ", 0);
    assertEquals("Game quit! Ending game state:\n"
            + "   |   |  \n"
            + "-----------\n"
            + "   | X |  \n"
            + "-----------\n"
            + "   |   |  ", lastMsg);

  }

  // Input where the q comes instead of an integer for the column
  @Test
  public void testQInColumn () {
    String lastMsg = this.runGameHelper("2 2 1 Q ", 0);
    assertEquals("Game quit! Ending game state:\n"
            + "   |   |  \n"
            + "-----------\n"
            + "   | X |  \n"
            + "-----------\n"
            + "   |   |  ", lastMsg);
  }


  // Input where non-integer garbage comes instead of an integer for the row
  @Test
  public void testGarbageRow () {

    String lastMsg = this.runGameHelper("2 2 A B C D E F q", 6);
    assertEquals(
            "Not a valid number: A\n" +
            "Not a valid number: B\n" +
            "Not a valid number: C\n" +
            "Not a valid number: D\n" +
            "Not a valid number: E\n" +
            "Not a valid number: F" , lastMsg);

  }
  // Input where non-integer garbage comes instead of an integer for the column
  @Test
  public void testGarbageColumn () {
    String lastMsg = this.runGameHelper("2 2 1 A B C D E F q", 6);
    assertEquals(
    "Not a valid number: A\n" +
            "Not a valid number: B\n" +
            "Not a valid number: C\n" +
            "Not a valid number: D\n" +
            "Not a valid number: E\n" +
            "Not a valid number: F" , lastMsg);

  }
  // Input where the move is integers, but outside the bounds of the board
  @Test
  public void testOutsideBoundaries () {
      String lastMsg = this.runGameHelper("2 2 4 5 8 8 -1 -1 3 8 3 4 4 5 6 6 q", 6);
      assertEquals(
              "Not a valid move: 8, 8\n" +
                      "Not a valid move: -1, -1\n" +
                      "Not a valid move: 3, 8\n" +
                      "Not a valid move: 3, 4\n" +
                      "Not a valid move: 4, 5\n" +
                      "Not a valid move: 6, 6"  , lastMsg);

  }
  // Input where the move is integers, but invalid because the cell is occupied
  @Test
  public void testTakenCell () {

    String lastMsg = this.runGameHelper("2 2 1 1 3 3 2 2 1 1 3 3 2 2 1 1 3 3 q ", 6);
    assertEquals(
            "Not a valid move: 2, 2\n" +
                    "Not a valid move: 1, 1\n" +
                    "Not a valid move: 3, 3\n" +
                    "Not a valid move: 2, 2\n" +
                    "Not a valid move: 1, 1\n" +
                    "Not a valid move: 3, 3" , lastMsg);

  }
  // Multiple invalid moves in a row of various kinds
  @Test
  public void testMultipleInvalidRows () {
    String lastMsg = this.runGameHelper("2 2 1 1 3 3 a 1 b c 1a -100 -4 q", 6);
    assertEquals(
            "Enter a move for O:\n" +
                    "Not a valid number: a\n" +
                    "Not a valid number: b\n" +
                    "Not a valid number: c\n" +
                    "Not a valid number: 1a\n" +
                    "Not a valid move: 1, -100", lastMsg);
  }
  // Input including valid moves interspersed with invalid moves, game is played to completion
  @Test
  public void testInterspersedMoves () {
    String lastMsg = this.runGameHelper("1 1 1 1 2 2 2 2 3 3 3 3 1 3 1 3 2 3 2 3 2 1 2 1 3 1 3 1 3 2 3 2 1 2", 0);
    assertEquals(
            " X | X | O\n" +
                    "-----------\n" +
                    " O | O | X\n" +
                    "-----------\n" +
                    " X | O | X\n" +
                    "Game is over! Tie game.", lastMsg);

  }
  // What happens when the input ends "abruptly" -- no more input, but not quit, and game not over
  @Test (expected = IllegalStateException.class)
  public void testTerminatedAbruptly () {
    this.runGameHelper("1 1 2 2 2 3 3", 0);
  }

  /**
   * Check the model is valid, if null passed throw error
   */
  @Test (expected = IllegalStateException.class)
  public void invalidModel () {

    StringReader input = new StringReader("2 2");
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(null);

  }

  /**
   * Check the model is valid, if null passed throw error
   */
  @Test (expected = IllegalArgumentException.class)
  public void invalidArgumentsController() {
    new TicTacToeConsoleController(null, null);
  }

  /**
   * Helper method to create a based on the gamePath that was provided
   * @param gamePath Instructions to follow, each movement should be separated by a space
   * @param shift From bottom to top it moves the returned string according to this input
   * @return Return the 6 last elements of the gameLog (the shift argument will alter the position)
   */
  private String runGameHelper(String gamePath, int shift) throws IllegalArgumentException {

    //Provide a valid path to create the game
    if (gamePath.length() == 0) throw new IllegalArgumentException();

    TicTacToe m = new TicTacToeModel();
    // note the entire sequence of user inputs for the entire game is in this one string:
    StringReader input = new StringReader(gamePath);
    StringBuilder gameLog = new StringBuilder();
    TicTacToeController c = new TicTacToeConsoleController(input, gameLog);
    c.playGame(m);

    String[] lines = gameLog.toString().split("\n");

    return String.join("\n",
            Arrays.copyOfRange(lines, lines.length - 6 - shift, lines.length - shift));
  }
}
