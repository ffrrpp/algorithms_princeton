import java.util.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {   
    
    private boolean isSolvable;
    private int movesToSolve;
    private Stack<Board> solution;
    private Stack<Board> solution_reversed;
    
    private class Node implements Comparable<Node> {
        public Board board;
        public Node prev;
        public int moves;
        
        public Node(Board board) {
            this.board = board;
            this.moves = 0;
            this.prev = null;
        }
        
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }
        
        public int compareTo(Node that) {
            int thisPriority = this.board.manhattan() + this.moves; 
            int thatPriority = that.board.manhattan() + that.moves;
            if (thisPriority < thatPriority) return -1;
            if (thisPriority > thatPriority) return 1;            
            return 0;
        }
    }
    
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
        
        MinPQ<Node> initPQ = new MinPQ<Node>();
        MinPQ<Node> twinPQ = new MinPQ<Node>();
              
        initPQ.insert(new Node(initial));
        twinPQ.insert(new Node(initial.twin()));
        
        isSolvable = false;
        movesToSolve = -1;
        solution = null;
        // StdOut.println("solution is null: " + solution==null);
        while(!initPQ.isEmpty() && !twinPQ.isEmpty()) {
            Node currInit = initPQ.delMin();
            Node currTwin = twinPQ.delMin(); 
            // StdOut.printf("init: %s\n", currInit.board.toString());
            // StdOut.printf("init.manhattan: %d\n", currInit.board.manhattan());
            // StdOut.printf("twin: %s\n", currTwin.board.toString());
            // StdOut.printf("twin.manhattan: %d\n", currTwin.board.manhattan());
            
            if (currTwin.board.manhattan() == 0) {
                isSolvable = false;
                movesToSolve = -1;
                break;
            }
            
            if (currInit.board.manhattan() == 0) {
                isSolvable = true;
                movesToSolve = currInit.moves;
                solution = new Stack<Board>();
                solution.push(currInit.board);
                Node prevInit = currInit.prev;
                while (prevInit != null) {
                    solution.push(prevInit.board);
                    prevInit = prevInit.prev;
                }                

                break;
            }
            
            for (Board iterBoard : currInit.board.neighbors()) {

                if (currInit.prev == null || !iterBoard.equals(currInit.prev.board)) {
                    initPQ.insert(new Node(iterBoard, currInit.moves + 1, currInit));
                }
            }
            for (Board iterBoard : currTwin.board.neighbors()) {
                if (currTwin.prev == null || !iterBoard.equals(currTwin.prev.board)) {
                    twinPQ.insert(new Node(iterBoard, currTwin.moves + 1, currTwin));
                }
            }
        }        
    }
    
    public boolean isSolvable() {         // is the initial board solvable?
        return isSolvable;
    }
    
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return movesToSolve;
    }
    
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        solution_reversed = new Stack<Board>();
        for (int i = 0; i < movesToSolve + 1; i++) {
            solution_reversed.push(solution.pop());
        }
        return solution_reversed;
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());

            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}