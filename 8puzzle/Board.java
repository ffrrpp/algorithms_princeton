import java.util.Stack;
import java.util.Arrays;


public class Board {
    
    private short[] board;
    private int dim;
    
    public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
        this.dim = blocks.length;
        this.board = new short[dim * dim];
        for (int idx = 0; idx < dim * dim; idx++) {
            int i = idx / dim;
            int j = idx % dim;
            this.board[idx] = (short)(blocks[i][j]);
        }
    }
    
    private Board(short[] board, int dim) {
        this.dim = dim;
        this.board = new short[dim * dim];
        this.board = Arrays.copyOf(board, dim * dim);
    }
    
    public int dimension() {                // board dimension n
        return dim;
    }
    
    public int hamming() {                  // number of blocks out of place
        int hamming = 0;
        for (int idx = 0; idx < dim * dim; idx++) {
            if (board[idx] != 0 && board[idx] != idx + 1) {
                hamming++;
            }
        }        
        return hamming;
    }
    
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
        int manhattan = 0;
        int row;
        int col;
        for (int idx = 0; idx < dim * dim; idx++) {
            if (board[idx] != 0) {
                int i = idx / dim;
                int j = idx % dim;
                row = (board[idx] - 1) / dim;
                col = (board[idx] - 1) % dim;
                manhattan += Math.abs(row - i) + Math.abs(col - j);
            }
        }        
        return manhattan;
    }
    
    public boolean isGoal() {               // is this board the goal board?
        return (hamming() == 0);
    }
    
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        Board twin = new Board(board, dim);
        if (dim > 1) {
            int[] swapIdx = new int[2];
            int count = 0;
            for (int idx = 0; count < 2; idx++) {
                if (board[idx] != 0) {
                    swapIdx[count] = idx;
                    count ++;
                }    
            }
            //swap
            twin.swap(swapIdx[0], swapIdx[1]);
        }      
        return twin;
    }
    
    public boolean equals(Object y) {       // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dim != dim) return false;
        for (int idx = 0; idx < dim * dim; idx++) {
            if (board[idx] != that.board[idx]) return false;            
        }
        return true;
    }
        
    public Iterable<Board> neighbors() {    // all neighboring boards
        
        int idx0 = 0;
        int i0 = 0;
        int j0 = 0;
        for (int idx = 0; idx < dim * dim; idx++) {
            if (board[idx] == 0) {
                idx0 = idx;
                i0 = idx / dim;
                j0 = idx % dim;
                break;
            }
        }
        
        // StdOut.println("(i0,j0) =  " + i0 + " " + j0);
        
        Stack<Board> neighbors = new Stack<Board>();
        
        // neighbor above
        if (i0 != 0) {
            int idx1 = idx0 - dim;
            Board nb = new Board(board, dim);
            nb.swap(idx0, idx1);
            neighbors.push(nb);
        }
        
        // neighbor below
        if (i0 != dim - 1) {
            int idx1 = idx0 + dim;
            Board nb = new Board(board, dim);
            nb.swap(idx0, idx1);
            neighbors.push(nb);
        }
        
        // neighbor left
        if (j0 != 0) {
            int idx1 = idx0 - 1;
            Board nb = new Board(board, dim);
            nb.swap(idx0, idx1);
            neighbors.push(nb);
        }
        
        //neighbor right
        if (j0 != dim - 1) {
            int idx1 = idx0 + 1;
            Board nb = new Board(board, dim);
            nb.swap(idx0, idx1);
            neighbors.push(nb);
        }
        
        return neighbors;
        
    }
    
    public String toString() {            // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        
        for (int idx = 0; idx < dim * dim; idx++) {
            s.append(String.format("%3d", board[idx]));
            if ((idx + 1) % dim == 0) {
                s.append("\n");
            }
        }
        return s.toString();        
    }    
    
    private void swap(int idx1, int idx2) {
        short temp = this.board[idx1];
        this.board[idx1] = this.board[idx2];
        this.board[idx2] = temp;
    }
    
    public static void main(String[] args) {// unit tests (not graded)
    }
}