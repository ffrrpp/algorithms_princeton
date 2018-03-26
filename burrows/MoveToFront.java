// import java.util.Arrays;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
// import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    
    private static final int R = 256;
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        int[] alphabet = new int[R];
        
        for (int i = 0; i < R; i++) {
            alphabet[i] = i;
        }
        
        for (int i = 0; i < s.length(); i++) {
            // StdOut.println(s);
            BinaryStdOut.write(alphabet[s.charAt(i)], 8);
            
            int count = 0;
            int j = 0;
            while (count < alphabet[s.charAt(i)]) {
                if (alphabet[j] < alphabet[s.charAt(i)]) {
                    alphabet[j]++;
                    count++;
                }
                j++;                
            }            
            alphabet[s.charAt(i)] = 0;
        }
        
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        
        String s = BinaryStdIn.readString();
        int[] inverse_alphabet = new int[R];
        
        for (int i = 0; i < R; i++) {
            inverse_alphabet[i] = i;
        }
        // StdOut.printf("%02X %02X\n", inverse_alphabet[0] & 0xff, inverse_alphabet[1] & 0xff);
        
        for (int i = 0; i < s.length(); i++) {
            
            char idx = s.charAt(i);
            BinaryStdOut.write(inverse_alphabet[idx], 8);
            int temp = inverse_alphabet[idx];
            System.arraycopy(inverse_alphabet, 0, inverse_alphabet, 1, idx);
            inverse_alphabet[0] = temp;
            // StdOut.printf("%02X %02X\n", inverse_alphabet[0] & 0xff, inverse_alphabet[1] & 0xff);
            
        }
        
        BinaryStdOut.close();        
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}