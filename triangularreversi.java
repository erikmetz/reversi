//MAP: Erik Metz (114752550), Shivam Agrawal (uid#), Matt Pasquino (115336858)

import java.util.*;
import java.lang.Math;

public class triangularreversi {
  public static void main(String[] str) {
    playMove(0);
    //playHuman(2);
    //playAIs(4, 5);
    //System.out.println("\nMoves we can make:");
    //state.findAllMoves();
  }


  /* Reads the input into an array and fills squares that are not part of the game with -1 */
  public static int[][] readBoard() {
    int[][] board = new int[10][20];
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        board[r][c] = -1;
      }
    }
    Scanner input = new Scanner(System.in);

    for (int r = 0; r < 10; r++) {
      for (int c = 9 - r; c < 11 + r; c++) {
        board[r][c] = input.nextInt();
      }
    }
    
    input.close();
    return board;
  }

  public static void playMove(int heuristic) {
    Heuristics.h = heuristic;
    GameState curr = new GameState(readBoard());
    MoveState best;
    int maxVal;
    int numMoves;
    ArrayList<MoveState> moves;
    int d;
    moves = curr.findMoves();
    numMoves = moves.size();
    if (numMoves > 12) {
      d = 3;
    } else if (numMoves > 5) {
      d = 4;
    } else {
      d = 5;
    }
    maxVal = Integer.MIN_VALUE;
    best = null;
    for (MoveState m : moves) {
      int val = m.getState().alphaBeta(d, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
      if (val >= maxVal) {
        best = m;
        maxVal = val;
      }
    }
    if (best != null) {
      System.out.println(best.getRow() + " " + best.getColumn());
    }
  }

  public static void playHuman(int heuristic) {
    Heuristics.h = heuristic;
    GameState curr = new GameState(readBoard());
    MoveState best;
    Scanner input = new Scanner(System.in);
    int maxVal;
    ArrayList<MoveState> moves;
    int[] whitespace = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
    while (true) {
      int row = input.nextInt();
      int column = input.nextInt();
      curr = curr.computeMove(row, column + whitespace[row]);
      System.out.println();
      curr.print(true);
      moves = curr.findMoves();
      maxVal = Integer.MIN_VALUE;
      best = null;
      for (MoveState m : moves) {
        int val = m.getState().alphaBeta(5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        if (val >= maxVal) {
          best = m;
          maxVal = val;
        }
      }
      if (best != null) {
        System.out.println();
        best.getState().print(true);
        curr = best.getState();
      }
      else {
        System.out.println("PASS");
        curr.swap();
        curr.print(true);
        return;
      }
    }
  }

  public static void playAIs(int h1, int h2) {
    int[][] gameStart =
    {
      {-1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1,  0,  0,  0,  0,  1,  2,  0,  0,  0,  0, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1,  0,  0,  0,  0,  0,  2,  1,  0,  0,  0,  0,  0, -1, -1, -1, -1},
      {-1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1, -1},
      {-1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1},
      {-1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1},
      { 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0}
    };
    GameState curr = new GameState(gameStart);
    MoveState best;
    int maxVal;
    ArrayList<MoveState> moves;
    int passes = 0;
    int player = 2;
    while (passes < 2) {
      player = 3 - player;
      
      curr.print(player == 1);
      
      if (player == 1) Heuristics.h = h1;
      else         Heuristics.h = h2;

      moves = curr.findMoves();
      System.out.println("Number of moves: " + moves.size());
      maxVal = Integer.MIN_VALUE;
      best = null;
      for (MoveState m : moves) {
        int val = m.getState().alphaBeta(4, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        if (val >= maxVal) {
          best = m;
          maxVal = val;
        }
      }
      if (best != null) {
        System.out.println("MOVE " + player + "\n");
        curr = best.getState();
        passes = 0;
      }
      else {
        System.out.println("PASS " + player + "\n");
        curr.swap();
        passes++;
      }
    }
    player = 3 - player;
    int winner = curr.winner();
    if (winner > 0) {
      System.out.println("Winner: " + player);
    } else {
      System.out.println("Winner: " + (3 - player));
    }
  }
}

/* 2d int array of game state, -1 is out of bounds */
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
  int boardHeight = 10;
  int boardWidth = 20;

  // necessary to convert our board coordinates to actual board coordinates
  static int[] whitespaces = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
  static int[][] weights =
  {
    {000, 000, 000, 000, 000, 000, 000, 000, 000, 030, 030, 000, 000, 000, 000, 000, 000, 000, 000, 000},
    {000, 000, 000, 000, 000, 000, 000, 000, 001, 001, 001, 001, 000, 000, 000, 000, 000, 000, 000, 000},
    {000, 000, 000, 000, 000, 000, 000, 001, 001, 001, 001, 001, 001, 000, 000, 000, 000, 000, 000, 000},
    {000, 000, 000, 000, 000, 000, 001, 001, 001, 001, 001, 001, 001, 001, 000, 000, 000, 000, 000, 000},
    {000, 000, 000, 000, 000, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 000, 000, 000, 000, 000},
    {000, 000, 000, 000, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 000, 000, 000, 000},
    {000, 000, 000, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 000, 000, 000},
    {000, 000, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 000, 000},
    {000, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 000},
    {030, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 001, 030}
  };

  public GameState(int[][] boardIn) {
    board = new int[10][20];
    for (int r = 0; r < boardIn.length; r++) {
      for (int c = 0; c < boardIn[r].length; c++) {
        board[r][c] = boardIn[r][c];
      }
    }
  }

  public void set(int r, int c, int val) {
    board[r][c] = val;
  }

  /* print with debug flag */
  public void print(boolean debug) {
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] == -1) System.out.print("  ");
        if (board[r][c] == 9) {
          if (debug) System.out.print("* ");
          else System.out.print("0 ");
        }
        else System.out.print(board[r][c] + " ");
      }
      System.out.println();
    }
  }

  /* returns an array list of moves in the form of gamestates from opponents perspective */
  public ArrayList<MoveState> findMoves() {
    ArrayList<MoveState> moves = new ArrayList<MoveState>();
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        GameState result = computeMove(r, c);
        if (result != null) {
          moves.add(new MoveState(r, c, result));
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
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] == 1) {
          board[r][c] = 2;
        }
        else if (board[r][c] == 2) {
          board[r][c] = 1;
        }
      }
    }
  }

  /* computes the result of moving at (i,j) and return null if invalid move */
  public GameState computeMove(int r, int c) {
    if (r < 0 || c < 0 || r >= 10 || c >= 20 || board[r][c] != 0) return null;
    
    int[][] dirs = {{0,1}, {1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}};

    GameState curr = clone();
    Boolean valid = false;
    for (int k = 0; k < dirs.length; k++) {
      valid |= curr.searchMove(r + dirs[k][0], c + dirs[k][1], dirs[k][0], dirs[k][1], 0);
    }
    if (valid) {
      curr.set(r, c, 1);
      curr.swap();
      return curr;
    }
    return null;
  }

  // only use as part of computeMove
  private Boolean searchMove(int r, int c, int dr, int dc, int flips) {
    // check in bounds
    if (r < 0 || c < 0 || r >= 10 || c >= 20) {
      return false;
    }

    // System.out.println("Checking: r = " + r + " c = " + c + " dr = " + dr + " dc = " + dc + " flips = " + flips);

    // only progress if there is an opponent piece in this direction
    // or the spot is ours and at least one piece is flipped
    if (board[r][c] == -1 || board[r][c] == 0 || ((board[r][c] == 1) && (flips == 0))) {
      return false;
    } else if (board[r][c] == 1) {
      // end of chain of opponent pieces
      for (int n = 1; n <= flips; n++) {
        board[r - dr*n][c - dc*n] = 1;
      }
      return true;
    } else {
      // opponent cell, try and flip
      return searchMove(r + dr, c + dc, dr, dc, flips + 1);
    }
  }

  public int winner() {
    int e = 0;
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] == 1)     e++;
        else if (board[r][c] == 2)  e--;
      }
    }
    if (e > 0)    return Integer.MAX_VALUE;
    else if (e < 0) return Integer.MIN_VALUE;
    else      return 0;
  }

  /*
    0: greedyboy
    1: weightedboy
    2: weighted boy with mobility
  */
  public int evaluate() {
    switch(Heuristics.h) {
      case 0: return this.cleverboy();
      case 1: return this.weightedboy();
      case 2: return this.greedyboy() + (10*this.mobility());
      case 3: return this.zeroboy();
      case 4: return this.cleverboy();
      case 5: return this.cleverboy2();
    }
    return 0;
  }

  /* evaluates moves available */
  public int mobility() {
    return this.findMoves().size();
  }

  /* eval function 0: pure tile difference */
  public int greedyboy() {
    int e = 0;
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] == 1)     e++;
        else if (board[r][c] == 2)  e--;
      }
    }
    return e;
  }

  /* eval function 1: weighted tiles */
  public int weightedboy() {
    int e = 0;
    for (int r = 0; r < board.length; r++) {
      for (int c = 0; c < board[r].length; c++) {
        if (board[r][c] == 1) {
          e += weights[r][c];
        }
        else if (board[r][c] == 2) {
          e -= weights[r][c];
        }
      }
    }
    return e;
  }

  public int zeroboy() {
    return 0;
  }

  /* eval function 2: weighted + includes eval fn 0 */
  public int cleverboy() {
    int egreed = 0;
    int eweight = 0;
    int tnum = 0;
    int emob = this.findMoves().size();
    int gweight = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 1) {
          egreed += 1;
          eweight += weights[i][j];
          tnum += 1;
        }
        else if (board[i][j] == 2) {
          egreed -= 1;
          eweight -= weights[i][j];
          tnum += 1;
        }
      }
    }
    if (tnum > 50) {
      gweight = (tnum-50)*(tnum-50) / 30;
    }
    return emob + eweight + egreed*gweight;
  }

  /* eval function 3: similar to 2, different weight based on total moves taken */
  public int cleverboy2() {
    int egreed = 0;
    int eweight = 0;
    int tnum = 0;
    int emob = this.findMoves().size();
    int gweight = 0;
    int wweight = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 1) {
          egreed += 1;
          eweight += weights[i][j];
          tnum += 1;
        }
        else if (board[i][j] == 2) {
          egreed -= 1;
          eweight -= weights[i][j];
          tnum += 1;
        }
      }
    }
    if (tnum > 50) {
      gweight = (tnum - 50)*(tnum - 50) / 2;
    }
    if (tnum < 50) {
      wweight = (50 - tnum) / 2;
    }
    return emob + eweight + egreed*gweight;
  }

  /* alpha beta */
  public int alphaBeta(int depth, int alpha, int beta, Boolean isMax) {
    int mult;
    if (isMax) {
      mult = 1;
    }
    else {
      mult = -1;
    }
    ArrayList<MoveState> moves = this.findMoves();
    if (depth == 0) {
      return this.evaluate()*mult;
    }
    if (moves.size() == 0) {
      GameState opp = this.clone();
      opp.swap();
      if (opp.findMoves().size() == 0) {
        return this.winner()*mult;
      }
    }
    if (isMax) {
      int value = Integer.MIN_VALUE;
      for (MoveState m : moves) {
        value = Math.max(value, m.getState().alphaBeta(depth - 1, alpha, beta, false));
        alpha = Math.max(alpha, value);
        if (alpha >= beta) {
          //System.out.println("beta");
          break; /* beta cutoff */
        }
      }
      return value*mult;
    }
    else {
      int value = Integer.MAX_VALUE;
      for (MoveState m : moves) {
        value = Math.min(value, m.getState().alphaBeta(depth - 1, alpha, beta, true));
        beta = Math.min(beta, value);
        if (alpha >= beta) {
          //System.out.println("alpha");
          break; /* alpha cutoff */
        }
      }
      return value*mult;
    }
  }

  // Move object
  class Move {

    private int r, c, flips;

    public Move(int r, int c, int flips) {
      this.r = r;
      this.c = c;
      this.flips = flips;
    }

    public int getR() { return r; }
    public int getC() { return c; }
    public int getFlips() { return flips; }

    public String toString() {
      return "r = " + r + " c = " + c + " flips = " + flips;
    }

  }

  // Move comparator
  class MoveComparator implements Comparator<Move> {
    public int compare(Move a, Move b) {
      return b.getFlips() - a.getFlips();
    }
  }

  public Move search(int r, int c, int dr, int dc, int flips) {

    // check in bounds
    if (r < 0 || c < 0 || r >= 10 || c >= 20) {
      return null;
    }

//     System.out.println("Checking: i = " + r + " j = " + c + " di = " + dr + " dj = " + dc + " flips = " + flips);

    // only progress if there is an opponent piece in this direction
    // or the spot is empty and at least one piece is flipped
    if (board[r][c] == -1 || board[r][c] == 1 || (board[r][c] == 0 && flips == 0)) {
      return null;
    } else if (board[r][c] == 0) {
      // open cell, can place here
      return new Move(r, c, flips);
    } else {
      // opponent cell, try and flip
      return search(r + dr, c + dc, dr, dc, flips + 1);
    }

  }

  /* Find all possible moves and store them in priority queue in order of how many pieces they flip */
  public void findAllMoves() {

    // find all of our pieces
    Queue<int[]> locations = new LinkedList<int[]>();

    for (int r = 0; r < 10; r++) {
      for (int c = 0; c < 20; c++) {
        if (board[r][c] == 1) {
          locations.add(new int[] { r, c });
        }
      }
    }

    PriorityQueue<Move> moves = new PriorityQueue<Move>(new MoveComparator());

    while (!locations.isEmpty()) {

      int[] loc = locations.poll();
      int r = loc[0], c = loc[1];

      Move north      = search(r - 1, c, -1, 0, 0);
      Move south      = search(r + 1, c,  1, 0, 0);
      Move west       = search(r, c - 1, 0, -1, 0);
      Move east       = search(r, c + 1, 0,  1, 0);
      Move northwest  = search(r - 1, c - 1, -1, -1, 0);
      Move northeast  = search(r - 1, c + 1, -1,  1, 0);
      Move southwest  = search(r + 1, c - 1,  1, -1, 0);
      Move southeast  = search(r + 1, c + 1,  1,  1, 0);

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
      Integer.toString(bestMove.getR() + 1) + " " +
      Integer.toString(bestMove.getC() - whitespaces[bestMove.getR()] + 1)
    );

    // for debugging: print moves
    boolean debug = false;

    if (debug) {

      Iterator<Move> iter = moves.iterator();
      while (iter.hasNext()) {
        Move move = iter.next();
        System.out.println(move.toString());
        // show board to reflect move
        int r = move.getR(), c = move.getC();
        board[r][c] = 9;
      }

      print(true);

      // undo changes to board

      iter = moves.iterator();
      while (iter.hasNext()) {
        // show board to reflect move
        Move move = iter.next();
        int r = move.getR(), c = move.getC();
        board[r][c] = 0;
      }

    }

  }

}

/* contains a move (i,j) and the resulting state (from opponent perspective) */
class MoveState {
  private int row, column;
  private GameState state;

  public MoveState(int r, int c, GameState s) {
    row = r;
    column = c;
    state = s;
  }

  public GameState getState() {
    return state;
  }

  public int getRow() {
    return row + 1;
  }

  public int getColumn() {
    int[] whitespace = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
    return column - whitespace[row] + 1;
  }
}

/* eval function choice */
class Heuristics {
  public static int h = 1;
}