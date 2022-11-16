import org.junit.Test;

import tictactoe.Player;
import tictactoe.TicTacToe;
import tictactoe.TicTacToeModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test cases for the tic-tac-toe model. Verifying that game state is properly managed, and
 * all game actions are properly validated.
 */
public class TicTacToeModelTest {

  private final TicTacToe  ttt1 = new TicTacToeModel();

  /**
   * Check the second player to play is always O
   */
  @Test
  public void testMove() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
  }

  /**
   * Test the game  row's boundaries, a player can only move within 0 - 2
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoveIllegalInput() {
    ttt1.move(20, 0);
  }

  /**
   * Test the game column's boundaries, a player can only move within 0 - 2
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoveIllegalInputColumn() {
    ttt1.move(0, -10);
  }


  /**
   * Test there is not possible to move on a position that has already taken
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMoveSpaceTaken() {
    ttt1.move(0, 0);
    ttt1.move(0, 0);
  }


  @Test
  public void name() {
  }

  /**
   * Test that in any of the 3 2d arrays, there is only one Player
   */
  @Test
  public void testHorizontalWin() {
    ttt1.move(0, 0); // X takes upper left
    assertFalse(ttt1.isGameOver());
    ttt1.move(1, 0); // O takes middle left
    ttt1.move(0, 1); // X takes upper middle
    assertNull(ttt1.getWinner());
    ttt1.move(2, 0); // O takes lower left
    ttt1.move(0, 2); // X takes upper right
    assertTrue(ttt1.isGameOver());
    assertEquals(Player.X, ttt1.getWinner());
    assertEquals(" X | X | X\n"
                          + "-----------\n"
                          + " O |   |  \n"
                          + "-----------\n"
                          + " O |   |  ", ttt1.toString());
  }

  /**
   * Test that in the abstract diagonal of the 2d arrays, there is only one Player
   */
  @Test
  public void testDiagonalWin() {
    diagonalWinHelper();
    assertTrue(ttt1.isGameOver());
    assertEquals(Player.O, ttt1.getWinner());
    assertEquals(" X | X | O\n"
            + "-----------\n"
            + " X | O |  \n"
            + "-----------\n"
            + " O |   |  ", ttt1.toString());
  }

  // set up situation where game is over, O wins on the diagonal, board is not full
  private void diagonalWinHelper() {
    ttt1.move(0, 0); // X takes upper left
    assertFalse(ttt1.isGameOver());
    ttt1.move(2, 0); // O takes lower left
    ttt1.move(1, 0); // X takes middle left
    assertNull(ttt1.getWinner());
    ttt1.move(1, 1); // O takes center
    ttt1.move(0, 1); // X takes upper middle
    ttt1.move(0, 2); // O takes upper right
  }

  /**
   * Test all cases where the player can not move.
   */
  @Test
  public void testInvalidMove() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
    assertEquals(Player.X, ttt1.getMarkAt(0, 0));
    try {
      ttt1.move(0, 0);
      fail("Invalid move should have thrown exception");
    } catch (IllegalArgumentException iae) {
      //assertEquals("Position occupied", iae.getMessage());
      assertTrue(iae.getMessage().length() > 0);
    }
    try {
      ttt1.move(-1, 0);
      fail("Invalid move should have thrown exception");
    } catch (IllegalArgumentException iae) {
      //assertEquals("Position occupied", iae.getMessage());
      assertTrue(iae.getMessage().length() > 0);
    }
  }

  /**
   * Test that there is not movement after the game is over
   */
  @Test(expected = IllegalStateException.class)
  public void testMoveAttemptAfterGameOver() {
    diagonalWinHelper();
    ttt1.move(2, 2); // 2,2 is an empty position
  }

  /**
   * Test a tie, game ends not winner
   */
  @Test
  public void testCatsGame() {
    ttt1.move(0, 0);
    assertEquals(Player.O, ttt1.getTurn());
    ttt1.move(1, 1);
    assertEquals(Player.X, ttt1.getTurn());
    ttt1.move(0, 2);
    ttt1.move(0, 1);
    ttt1.move(2, 1);
    ttt1.move(1, 0);
    ttt1.move(1, 2);
    ttt1.move(2, 2);
    ttt1.move(2, 0);
    assertTrue(ttt1.isGameOver());
    assertNull(ttt1.getWinner());
    assertEquals( " X | O | X\n"
            + "-----------\n"
            + " O | O | X\n"
            + "-----------\n"
            + " X | X | O", ttt1.toString());
  }

  /**
   * Test trying to get a mark out of boundary on Row
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetMarkAtRow() {
    ttt1.getMarkAt(-12, 0);
  }

  /**
   * Test trying to get a mark out of boundary on Column
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidGetMarkAtCol() {
    ttt1.getMarkAt(0, -30);
  }

  /**
   * Test that the getBoard method is returning a copy of the inner
   * implementation of the board, that way an outsider can not modify this game
   */
  @Test
  public void testGetBoard() {
    diagonalWinHelper();
    Player[][] bd = ttt1.getBoard();
    assertEquals(Player.X, bd[0][0]);
    assertEquals(Player.O, bd[1][1]);
    assertEquals(Player.X, bd[0][1]);

    // attempt to cheat by mutating board returned by getBoard()
    // check correct preconditions
    assertEquals(Player.O, bd[2][0]);
    assertEquals(Player.O, ttt1.getMarkAt(2, 0));
    bd[2][0] = Player.X;  // mutate
    // check correct post conditions
    assertEquals(Player.O, ttt1.getMarkAt(2, 0));
    Player[][] bd2 = ttt1.getBoard();
    assertEquals(Player.O, bd2[2][0]);
  }

  /**
   * Check that the game is over and at the end the last player
   * to play will win the game.
   */
  @Test
  public void testBoardIsFullAndWinner () {

    ttt1.move(0,0); //x
    ttt1.move(0,1); //o
    ttt1.move(0,2); //x

    ttt1.move(1,0); //o
    ttt1.move(1,2); //x
    ttt1.move(1,1); //o

    ttt1.move(2,1); //x
    ttt1.move(2,0); //o
    ttt1.move(2,2); //x Column 2 fill of Xs and the board is full.

    assertTrue(ttt1.isGameOver());
    assertEquals(Player.X, ttt1.getWinner());

  }


  /**
   * Check that the game is over and there is not a winner
   */
  @Test
  public void testBoardIsFullAndNotWinner() {

    ttt1.move(0,0); //x
    ttt1.move(0,1); //o
    ttt1.move(0,2); //x

    ttt1.move(1,0); //o
    ttt1.move(1,2); //x
    ttt1.move(1,1); //o

    ttt1.move(2,1); //x
    ttt1.move(2,2); //o
    ttt1.move(2,0); //x

    assertTrue(ttt1.isGameOver());
    assertNull(ttt1.getWinner());

  }

  /**
   * The initial Player of the game is always X.
   */
  @Test
  public void testInitialPlayer() {
    assertEquals(Player.X, ttt1.getTurn());
  }

  /**
   * Check that the Player is changed everytime
   * there is a movement in our game
   */
  @Test
  public void testChangeOfPlayerOrder() {
    assertEquals(Player.X, ttt1.getTurn());
    ttt1.move(0,0);
    assertEquals(Player.O, ttt1.getTurn());
    ttt1.move(0,1);
    assertEquals(Player.X, ttt1.getTurn());
    ttt1.move(0,2);
    assertEquals(Player.O, ttt1.getTurn());
    ttt1.move(1,0);
  }

  /**
   * Check that the game is really over when there is a winner
   */
  @Test
  public void testGameIsOver2 (){
    assertEquals(Player.X, ttt1.getTurn());
    ttt1.move(0,0);
    assertFalse(ttt1.isGameOver());
    ttt1.move(1,1);
    assertFalse(ttt1.isGameOver());
    ttt1.move(0,1);
    assertFalse(ttt1.isGameOver());
    ttt1.move(2,2);
    assertFalse(ttt1.isGameOver());
    ttt1.move(0,2);
    assertTrue(ttt1.isGameOver());
  }


  /**
   * Check winner Dinner is testing there is a winner
   * when a Player has completed the valid parameter for this, otherwise
   * no Chicken == null
   */
  @Test
  public void testWinnerChickenDinner (){
    assertNull(ttt1.getWinner());
    ttt1.move(0,0);
    assertNull(ttt1.getWinner());
    ttt1.move(1,1);
    assertNull(ttt1.getWinner());
    ttt1.move(0,1);
    assertNull(ttt1.getWinner());
    ttt1.move(2,2);
    assertNull(ttt1.getWinner());
    ttt1.move(0,2);
    assertEquals(Player.X,ttt1.getWinner());
  }

  /**
   * Test vertical winner
   */
  @Test
  public void testVerticalWinner (){
    assertNull(ttt1.getWinner());
    ttt1.move(0,0);
    assertNull(ttt1.getWinner());
    ttt1.move(1,1);
    assertNull(ttt1.getWinner());
    ttt1.move(1,0);
    assertNull(ttt1.getWinner());
    ttt1.move(2,2);
    assertNull(ttt1.getWinner());
    ttt1.move(2,0);
    assertEquals(Player.X,ttt1.getWinner());
  }
}
