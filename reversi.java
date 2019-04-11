
import java.util.Scanner;
import java.util.Arrays;

public class reversi {
  public static void main(String[] str) {
    GameState state = new GameState(readBoard());
    state.print();
  }

  /* Reads the input into an array and fills squares that are not part of the game with -1 */
  public static int[][] readBoard() {
    int[][] board = new int[8][14];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = -1;
      }
    }
    Scanner input = new Scanner(System.in);

    for (int j = 3; j < 11; j++) {
      board[0][j] = input.nextInt();
    }
    for (int j = 2; j < 12; j++) {
      board[1][j] = input.nextInt();
    }
    for (int j = 1; j < 13; j++) {
      board[2][j] = input.nextInt();
    }
    for (int j = 0; j < 14; j++) {
      board[3][j] = input.nextInt();
    }
    for (int j = 0; j < 14; j++) {
      board[4][j] = input.nextInt();
    }
    for (int j = 1; j < 13; j++) {
      board[5][j] = input.nextInt();
    }
    for (int j = 2; j < 12; j++) {
      board[6][j] = input.nextInt();
    }
    for (int j = 3; j < 11; j++) {
      board[7][j] = input.nextInt();
    }
    return board;
  }
}

/*
Stores the state of the game in an integer array
Use -1 to represent cells of the array which are not actually part of the game
*/
class GameState {
  int[][] board;

  public GameState(int[][] boardIn) {
    board = new int[8][14];
    for (int i = 0; i < boardIn.length; i++) {
      for (int j = 0; j < boardIn[i].length; j++) {
        board[i][j] = boardIn[i][j];
      }
    }
  }

  public void print() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == -1) {
          System.out.print("  ");
        }
        else {
          System.out.print(board[i][j] + " ");
        }
      }
      System.out.println();
    }
  }
}
