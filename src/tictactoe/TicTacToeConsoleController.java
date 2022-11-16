package tictactoe;


import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class TicTacToeConsoleController implements TicTacToeController{
    final Readable in;
    final Appendable out;
    private static final int placeholder = -9870;
    public TicTacToeConsoleController(Readable in, Appendable out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void playGame(TicTacToe m) throws IllegalStateException {
        Objects.requireNonNull(m);
        Scanner scan = new Scanner(this.in);
        int[] data = new int[2];

        try {

            //We need a loop to control the rounds
            while (!m.isGameOver()) {
                resetData(data);
                this.printLine(m.toString());
                this.printLine("Enter a move for " + m.getTurn().toString() + ":");

                //Another loop to control the users input
                while (true) {

                    if (scan.hasNext("q")) {
                        this.printLine("Game quit! Ending game state:");
                        this.printLine(m.toString());
                        return;
                    }

                    for (int i = 0; i < data.length; i++) {
                        if (data[i] == placeholder) {
                            if(scan.hasNextInt()) data[i] = scan.nextInt();
                            else this.printLine("Not a valid number: " + scan.next());
                        }
                    }

                    //Verify we have a valid input
                    if(Arrays.stream(data).noneMatch(x -> x == placeholder)) {
                        try{
                            m.move(data[0] - 1 , data[1] - 1);
                            break;
                        } catch (IllegalArgumentException e) {
                            this.printLine("Not a valid movement " + data[0] + ", " + data[1]);
                            resetData(data);
                        }catch (IllegalStateException e) {
                            break;
                        }
                    }
                }
            }
            this.printLine(m.toString());
            this.printLine("Game is over! " + (m.getWinner() == null ? "Tie game." :  m.getWinner() + " wins."));

        } catch (IOException e) {
            throw new IllegalStateException();
        }

    }



    private void printLine (String line) throws IOException {
        this.out.append(line).append("\n");
    }

    private void resetData (int[] data) {
        data[0] = placeholder;
        data[1] = placeholder;
    }
}
