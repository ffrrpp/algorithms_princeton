import java.util.Arrays;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    
    private static final int R = 256;
    
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {

        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) BinaryStdOut.write(i);
        }
        
        for (int i = 0; i < csa.length(); i++) {
            int idx = csa.index(i) == 0 ? csa.length() - 1 : csa.index(i) - 1;
            BinaryStdOut.write(s.charAt(idx), 8);
        }
        
        BinaryStdOut.close();
        
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        
        char[] t = s.toCharArray();
        char[] begin = new char[t.length];
        
        for (int i = 0; i < t.length; i++) {
            begin[i] = t[i];
        }
        Arrays.sort(begin);
        
        // construction of next[]
        int[] pos = new int[R];
        int[] next = new int[begin.length];
        
        for (int i = 0; i < R; i++) {
            pos[i] = -1;
        }
        for (int i = 0; i < begin.length; i++) {
            if (pos[begin[i]] == -1) pos[begin[i]] = i;
        }
        for (int i = 0; i < t.length; i++) {
            next[pos[t[i]]] = i;
            pos[t[i]]++;
        }
        
        // construction original char array
        int nextIdx = first;
        for (int i = 0; i < begin.length; i++) {            
            BinaryStdOut.write(begin[nextIdx], 8);
            nextIdx = next[nextIdx];
        }
        
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}