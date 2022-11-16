# Implementation MVC with a Tic Tac Toe Game. 


### Implementing the Model (logic)

- On this project I am going to practice the single responsability of the model in the MVC architecture. Following this convenctions given by the instructor:

A Model for Tic Tac Toe

The purpose of this exercise is to give you practice with implementing the Model component of the Model-View-Controller design pattern.

In the starter code, you are given an interface representing a game of Tic Tac Toe; your task is to implement the TicTacToe interface.

- You will need to define an enum Player, representing the players (X and O), with a toString() method that returns "X" and "O" accordingly. 
- You will need to implement the public class named TicTacToeModel, with a single public constructor that takes no arguments. 
- The class definition, with a toString() implementation to help with debugging, are provided to you in the starter code. 
- You will fill in the fields and remaining method definitions as appropriate. 
- You may also define other classes at your option as needed.

Game Grid Rules
- The game grid cells are numbered by row and column starting from 0. 
  - For example, the upper left position is row 0, column 0 (or [0][0] in the 2D array returned by getBoard()), 
  - the upper middle position is row 0, column 1 ([0][1]), 
  - the lower right is [2][2].
  
### Implementing the Controller
  
The controller will output game state and prompts to the Appendable, and read inputs from the Readable corresponding to user moves. 
  - The append() method on Appendable throws a checked exception, IOException. 
  - The playGame() method should not throw this exception. If it occurs, the playGame() should catch it and throw an IllegalStateException.

A single move consists of two numbers, specifying the row and column of the intended move position. Board positions for these moves are numbered from 1. For example, to mark X in the upper left cell, the user would enter "1 1" at the first prompt. To mark O in the upper right cell on the second move, the user would enter "1 3". To quit a game in progress, the user can enter q or Q at any time.

The game state is the output of the model’s toString() method, followed by a carriage return (\n). The move prompt is

  - "Enter a move for " + model.getTurn().toString() + ":\n"
  - (where model is an instance of your Tic Tac Toe Model).

If a non-integer value is entered, it should be rejected with an error message. If an invalid move is entered, namely, two valid integers, but the proposed move was deemed invalid by the model, the controller should give an error message. The content of these error messages should be formatted like the appropriate example followed by a line return:

  - "Not a valid move: 4, 10"
  - "Not a valid number: four"
 

At the end of the game, the controller should output, in order on separate lines:

  - A final game state
  - Game is over! followed by "X wins." or "O wins." or "Tie game." depending on the outcome

If the user quits, the controller should output

 - Game quit! Ending game state:\n" + model.toString() + "\n" and end the playGame() method.

 
