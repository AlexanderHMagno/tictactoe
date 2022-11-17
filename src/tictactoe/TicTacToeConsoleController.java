package tictactoe;


import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;


/**
 * This Class represents the Tic Tac Toe Controller, it has a Readable and Appendable objects. It implements
 * TicTacToeController Interface.
 */
public class TicTacToeConsoleController implements TicTacToeController{
    final Readable in;
    final Appendable out;
    private static final int placeholder = -9870;

    /**
     * Constructor it has a Readable and Appendable objects.
     * @param in The source of the input.
     * @param out The object to aggregate the information
     */
    public TicTacToeConsoleController(Readable in, Appendable out) throws IllegalArgumentException {
        this.in = in;
        this.out = out;
    }

    @Override
    public void playGame(TicTacToe m) throws IllegalStateException {

        //Check we have a valid model
        testValidModel(m);

        //Use this array to control the row and column inputs
        int[] data = new int[2];
        Scanner scan = new Scanner(this.in);

        try {
            //We need a loop to control the rounds
            while (!m.isGameOver()) {
                resetData(data);
                this.printLine(m.toString());
                this.printLine("Enter a move for " + m.getTurn().toString() + ":");

                //Another loop to control the users input, q|Q or integers
                while (true) {

                    // Check for 2 integers in row and column
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] != placeholder) continue;

                        if (scan.hasNextInt()) {
                            data[i] = scan.nextInt();
                        } else if (scan.hasNext("(?i)q")) {
                            this.quitGame(m);
                            return;
                        } else {
                            this.printLine("Not a valid number: " + scan.next());
                            i = -1;
                        }
                    }

                    //Verify we have a valid input
                    if(Arrays.stream(data).noneMatch(x -> x == placeholder)) {
                        try{
                            m.move(data[0] - 1 , data[1] - 1);
                            break;
                        } catch (IllegalArgumentException e) {
                            this.printLine("Not a valid move: " + data[0] + ", " + data[1]);
                            resetData(data);
                        }catch (IllegalStateException e) {
                            break;
                        }
                    }
                }
            }
            this.printLine(m.toString());
            this.printLine("Game is over! " + (m.getWinner() == null ? "Tie game." :  m.getWinner() + " wins."));

        } catch (IOException | NoSuchElementException n) {
            throw new IllegalStateException();
        }
    }

    /**
     * Helper function to append information to the appendable with new lines
     * @param line Information to append
     * @throws IOException If invalid value is passed
     */
    private void printLine (String line) throws IOException {
        this.out.append(line).append("\n");
    }

    /**
     * Helper function to reset the information of the array that holds each position
     * @param data Position of the next element to play
     */
    private void resetData (int[] data) {
        data[0] = placeholder;
        data[1] = placeholder;
    }

    /**
     * Append information to terminate the game.
     * @param m Tic Tac Toe Model
     * @throws IOException If invalid value is passed
     */
    private void quitGame (TicTacToe m) throws IOException{
        this.printLine("Game quit! Ending game state:");
        this.printLine(m.toString());
    }

    /**
     * Test if Model is valid otherwise throws an error
     * @param m Tic Tac Toe Model
     * @throws IllegalStateException If the Model is invalid
     */
    private void testValidModel (TicTacToe m) throws IllegalStateException {
        try {
            Objects.requireNonNull(m);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
