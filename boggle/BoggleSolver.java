import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
// import java.util.Stack;

public class BoggleSolver
{
    private TrieST26<Integer> st;
    private HashSet<String> validWords;
    private boolean[][] visited;
    // private Stack<Integer> toVisit;
    
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        // build symbol table from standard input
        st = new TrieST26<Integer>();
        
        int dictLength = dictionary.length;
        for (int i = 0; i < dictLength; i++) {
            String key = dictionary[i];
            st.put(key, i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows();
        int n = board.cols();
        
        validWords = new HashSet<String>();
        
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                visited = new boolean[m][n];
                // toVisit = new Stack<Integer>();
                // int idx = row * n + col;
                // toVisit.push(idx);
                StringBuilder prefix = new StringBuilder();
                dfs(row, col, board, prefix);

            }
        }
        
        return validWords;
    }
    
    private void dfs(int row, int col, BoggleBoard board, StringBuilder prefix) {
        if (visited[row][col] == true) {
            // StdOut.println("visited: " + board.getLetter(row, col));
            return;
        }
        // mark
        visited[row][col] = true;
        
        prefix.append(board.getLetter(row, col));
        if (!st.containsPrefix(prefix.toString())) {
            visited[row][col] = false;
            prefix.delete(prefix.length() - 1, prefix.length());
            // StdOut.println("been here 1 " + prefix.toString());
            return;
        }
        // StdOut.println(prefix.toString());
        if (st.contains(prefix.toString()) && prefix.length() > 2) {
            validWords.add(prefix.toString());
        }
        
        if (row > 0) {            
            dfs(row - 1, col, board, prefix);            
            if (col > 0) dfs(row - 1, col - 1, board, prefix);
            if (col < board.cols() - 1) dfs(row - 1, col + 1, board, prefix);
        }
        
        if (col > 0) dfs(row, col - 1, board, prefix);
        if (col < board.cols() - 1) dfs(row, col + 1, board, prefix);
        
        if (row < board.rows() - 1) {
            dfs(row + 1, col, board, prefix);
            if (col > 0) dfs(row + 1, col - 1, board, prefix);
            if (col < board.cols() - 1) dfs(row + 1, col + 1, board, prefix);
        }
        // unmark

        prefix.delete(prefix.length() - 1, prefix.length());
        visited[row][col] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!st.contains(word)) return 0;
        int len = word.length();
        if (len <= 2) return 0;
        else if (len <= 4) return 1;
        else if (len == 5) return 2;
        else if (len == 6) return 3;
        else if (len == 7) return 5;
        else return 11;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
