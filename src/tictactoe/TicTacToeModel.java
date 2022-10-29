package tictactoe;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represent the Inner functionalities of the TicTacToe game. It represents
 * the model in the General MVC design pattern. It has an Associative array (2D array) that represents
 * the board and a counter.
 * This class implements the TicTacToe Interface.
 */
public class TicTacToeModel implements TicTacToe {

  private final Player[][] board;
  private int round;

  /**
   * Constructor of TicTacToe, it initializes the game round to zero and
   * establish the boundaries of the game.
   */
  public TicTacToeModel() {
    this.board = new Player[3][3];
    this.round = 0;
  }

  @Override
  public void move(int r, int c) throws IllegalArgumentException, IllegalStateException {

    //Conditions

    //Check if the game has ended
    if (this.isGameOver()) throw  new IllegalStateException("Game has been completed");
    //the position is invalid
    this.OutOfBoundary(r,c);
    //Check if the position is occupied
    if(this.getMarkAt(r,c) != null) throw new IllegalArgumentException("Position is taken");

    //Add Player to this position.
    this.board[r][c] =  this.getTurn();
    this.round++;

  }

  @Override
  public Player getTurn() {
    return (this.round+1) % 2 == 0 ? Player.O : Player.X;
  }

  @Override
  public boolean isGameOver() {

    //game has a winner or is full
    return this.getWinner() != null || this.round >= 9;
  }

  @Override
  public Player getWinner() {

    //There can not be a winner with less than 5 movements
    if (this.round < 5) return null;

    //check horizontally
    for (Player[] row: this.board) {
      Set<Player> unique = Arrays.stream(row).collect(Collectors.toSet());
      if (unique.size() == 1) return unique.iterator().next();
    }

    //check Vertically
    for (int col = 0; col <= 2 ; col++) {
      Player[] vertical = new Player[]{this.board[0][col], this.board[1][col], this.board[2][col]};
      Set<Player> unique = Arrays.stream(vertical).collect(Collectors.toSet());
      if (unique.size() == 1) return unique.iterator().next();

    }

    //check Diagonally
    Player[] diagonal = new Player[]{this.board[0][0], this.board[1][1], this.board[2][2]};
    Player[] antiDiagonal = new Player[]{this.board[0][2], this.board[1][1], this.board[2][0]};
    Set<Player> uniqueDiagonal = Arrays.stream(diagonal).collect(Collectors.toSet());
    Set<Player> uniqueAntiDiagonal = Arrays.stream(antiDiagonal).collect(Collectors.toSet());

    if (uniqueDiagonal.size() == 1) return uniqueDiagonal.iterator().next();
    if (uniqueAntiDiagonal.size() == 1) return uniqueAntiDiagonal.iterator().next();

    return null;
  }

  @Override
  public Player[][] getBoard() {
    //Deep copy of Associative array.
    return Arrays.stream(this.board).map(Player[]::clone).toArray(Player[][]::new);
  }

  @Override
  public Player getMarkAt(int r, int c) throws IllegalArgumentException {
    //the position is otherwise invalid
    this.OutOfBoundary(r,c);

    return this.board[r][c];
  }

  @Override
  public String toString() {
    // Using Java stream API to save code:
    return Arrays.stream(getBoard()).map(
      row -> " " + Arrays.stream(row).map(
        p -> p == null ? " " : p.toString()).collect(Collectors.joining(" | ")))
          .collect(Collectors.joining("\n-----------\n"));
  }

  /**
   * Check the coordinates of r and c are within the board
   * @param r the row of the intended move
   * @param c the column of the intended move
   * @throws IllegalArgumentException When the column or row is greater or lower than board size
   */
  private void OutOfBoundary (int r, int c) throws IllegalArgumentException {

    //the position is otherwise invalid
    if (r < 0 || c < 0 || r > 2 || c > 2) {
      throw  new IllegalArgumentException("Out of boundary");
    }
  }
}
