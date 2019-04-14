
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Iterator;

public class reversi {
  public static void main(String[] str) {
    GameState state = new GameState(readBoard());
    //state.print();

    //System.out.println("\nMoves we can make:");
    //state.findAllMoves();
    ArrayList<GameState> moves =  state.findMoves();
    for (GameState g : moves) {
      System.out.println("move:");
      g.print();
    }
    //System.out.println("darn");

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

  /*
    Board values:
      -1  => not a possible location
       0  => no piece in the cell
       1  => our piece is in the cell
       2  => our opponents piece is in the cell
       9  => a potential move we can make (only for debugging)
  */
  int[][] board;
  int boardHeight = 8;
  int boardWidth = 14;

  // necessary to convert our board coordinates to actual board coordinates
  static int[] whitespaces = { 3, 2, 1, 0, 0, 1, 2, 3 };

  public GameState(int[][] boardIn) {
    board = new int[8][14];
    for (int i = 0; i < boardIn.length; i++) {
      for (int j = 0; j < boardIn[i].length; j++) {
        board[i][j] = boardIn[i][j];
      }
    }
  }

  public void set(int i, int j, int val) {
    board[i][j] = val;
  }

  public void print() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == -1) {
          System.out.print("  ");
        } else if (board[i][j] == 9) {
          System.out.print("* ");
        } else {
          System.out.print(board[i][j] + " ");
        }
      }
      System.out.println();
    }
  }

  /* returns an array list of moves in the form of gamestates from opponents perspective */
  public ArrayList<GameState> findMoves() {
    ArrayList<GameState> moves = new ArrayList<GameState>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        GameState result = computeMove(i,j);
        if (result != null) {
          System.out.println("hello");
          moves.add(result);
        }
      }
    }
    return moves;
  }


  /* clones current gamestate */
  public GameState clone() {
    return new GameState(board);
  }

  /* switches current player */
  public void swap() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 1) {
          board[i][j] = 2;
        }
        else if (board[i][j] == 2) {
          board[i][j] = 1;
        }
      }
    }
  }

  /* computes the result of moving at (i,j) and return null if invalid move */
  public GameState computeMove(int i, int j) {
    if (i < 0 || j < 0 || i >= 8 || j >= 14 || board[i][j] != 0) {
      return null;
    }
    int[][] dirs = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};

    GameState curr = clone();
    Boolean valid = false;
    for (int k = 0; k < dirs.length; k++) {
      valid |= curr.searchMove(i + dirs[k][0], j + dirs[k][1], dirs[k][0], dirs[k][1], 0);
    }
    if (valid) {
      curr.set(i, j, 1);
      curr.swap();
      return curr;
    }
    else {
      return null;
    }
  }

  // only use as part of computeMove
  private Boolean searchMove(int i, int j, int di, int dj, int flips) {
    // check in bounds
    if (i < 0 || j < 0 || i >= 8 || j >= 14) {
      return false;
    }

    // System.out.println("Checking: i = " + i + " j = " + j + " di = " + di + " dj = " + dj + " flips = " + flips);

    // only progress if there is an opponent piece in this direction
    // or the spot is ours and at least one piece is flipped
    if (board[i][j] == -1 || board[i][j] == 0 || ((board[i][j] == 1) && (flips == 0))) {
      //System.out.println(board[i][j]);
      return false;
    } else if (board[i][j] == 1) {
      // end of chain of opponent pieces
      for (int n = 1; n <= flips; n++) {
        board[i - di*n][j - dj*n] = 1;
      }
      //System.out.println("sometimes I return true");
      return true;
    } else {
      // opponent cell, try and flip
      //System.out.println("sometimes I return here");
      return searchMove(i + di, j + dj, di, dj, flips + 1);
    }
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



  /* Find all possible moves and store them in priority queue in order of how many pieces they flip */

  // Move object
  class Move {

    private int i, j, flips;

    public Move(int i, int j, int flips) {
      this.i = i;
      this.j = j;
      this.flips = flips;
    }

    public int getI() { return i; }
    public int getJ() { return j; }
    public int getFlips() { return flips; }

    public String toString() {
      return "i = " + i + " j = " + j + " flips = " + flips;
    }

  }

  // Move comparator
  class MoveComparator implements Comparator<Move> {
    public int compare(Move a, Move b) {
      return b.getFlips() - a.getFlips();
    }
  }

  public Move search(int i, int j, int di, int dj, int flips) {

    // check in bounds
    if (i < 0 || j < 0 || i >= 8 || j >= 14) {
      return null;
    }

    // System.out.println("Checking: i = " + i + " j = " + j + " di = " + di + " dj = " + dj + " flips = " + flips);

    // only progress if there is an opponent piece in this direction
    // or the spot is empty and at least one piece is flipped
    if (board[i][j] == -1 || board[i][j] == 1 || (board[i][j] == 0 && flips == 0)) {
      return null;
    } else if (board[i][j] == 0) {
      // open cell, can place here
      return new Move(i, j, flips);
    } else {
      // opponent cell, try and flip
      return search(i + di, j + dj, di, dj, flips + 1);
    }

  }

  public void findAllMoves() {

    // find all of our pieces
    Queue<int[]> locations = new LinkedList<int[]>();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 14; j++) {
        if (board[i][j] == 1) {
          locations.add(new int[] { i, j });
        }
      }
    }

    PriorityQueue<Move> moves = new PriorityQueue<Move>(new MoveComparator());

    while (!locations.isEmpty()) {

      int[] loc = locations.poll();
      int i = loc[0], j = loc[1];

      Move north      = search(i - 1, j, -1, 0, 0);
      Move south      = search(i + 1, j,  1, 0, 0);
      Move west       = search(i, j - 1, 0, -1, 0);
      Move east       = search(i, j + 1, 0,  1, 0);
      Move northwest  = search(i - 1, j - 1, -1, -1, 0);
      Move northeast  = search(i - 1, j + 1, -1,  1, 0);
      Move southwest  = search(i + 1, j - 1,  1, -1, 0);
      Move southeast  = search(i + 1, j + 1,  1,  1, 0);

      if (north != null) moves.add(north);
      if (south != null) moves.add(south);
      if (west != null) moves.add(west);
      if (east != null) moves.add(east);
      if (northwest != null) moves.add(northwest);
      if (northeast != null) moves.add(northeast);
      if (southwest != null) moves.add(southwest);
      if (southeast != null) moves.add(southeast);

    }

    // Print best move
    Move bestMove = moves.peek();
    // need to normalize move to account for whitespace
    System.out.println(
      Integer.toString(bestMove.getI() + 1) + " " +
      Integer.toString(bestMove.getJ() - whitespaces[bestMove.getI()] + 1)
    );





    // for debugging: print moves

    boolean debug = true;

    if (debug) {

      Iterator<Move> iter = moves.iterator();
      while (iter.hasNext()) {
        Move move = iter.next();
        System.out.println(move.toString());
        // show board to reflect move
        int i = move.getI(), j = move.getJ();
        board[i][j] = 9;
      }

      print();

      // undo changes to board

      iter = moves.iterator();
      while (iter.hasNext()) {
        // show board to reflect move
        Move move = iter.next();
        int i = move.getI(), j = move.getJ();
        board[i][j] = 0;
      }

    }

  }

}
