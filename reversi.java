
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

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

  /* returns an array list of moves [row, column] */
  public ArrayList<int[]> findMoves() {
    ArrayList<int[]> moves = new ArrayList<int[]>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (isMove(i,j)) {
          int[] move = new int[]{i,j};
          moves.add(move);
        }
      }
    }
    return moves;
  }

  /* check if (rIn,cIn) is a legal move. incomplete, only checks horizontal and vertical directions */
  public Boolean isMove(int rIn, int cIn) {
    if (board[rIn][cIn] != 0) {
      return false;
    }
    else {
      int r = rIn - 1;
      int c = cIn;
      while ((r > 0) && (board[r][c]) == 2) {
        r--;
      }
      if (board[r][c] == 1) {
        return true;
      }

      r = rIn;
      c = cIn - 1;
      while ((c > 0) && (board[r][c]) == 2) {
        c--;
      }
      if (board[r][c] == 1) {
        return true;
      }

      r = rIn + 1;
      c = cIn;
      while ((r < board.length) && (board[r][c]) == 2) {
        r++;
      }
      if (board[r][c] == 1) {
        return true;
      }

      r = rIn;
      c = cIn + 1;
      while ((r < board[0].length) && (board[r][c]) == 2) {
        c++;
      }
      if (board[r][c] == 1) {
        return true;
      }

      return false;
    }
  }
}
