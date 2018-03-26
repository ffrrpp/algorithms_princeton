import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    
    private String str;
    private Integer[] suffixArray;
    
    private class suffixComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            for (int i = 0; i < str.length() - 1; i++) {
                if (str.charAt(a) < str.charAt(b)) {
                    return -1;
                } else if (str.charAt(a) > str.charAt(b)) {
                    return 1;
                }
                a = (a == str.length() - 1) ? 0 : a + 1;
                b = (b == str.length() - 1) ? 0 : b + 1;
            }
            return 0;            
        }
    }
    
    private Comparator<Integer> suffixComparator() {
        return new suffixComparator();
    }
        
    public CircularSuffixArray(String s) {   // circular suffix array of s
        this.str = s;
        this.suffixArray = new Integer[length()];
        for (int i = 0; i < length(); i++) {
            suffixArray[i] = i;
        }
        Arrays.sort(suffixArray, suffixComparator());
    }
    
    public int length() {                    // length of s
        return str.length();
    }
    
    public int index(int i) {                // returns index of ith sorted suffix
        return suffixArray[i];
    }
    
    public static void main(String[] args) { // unit testing (required)
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++ ) System.out.println(csa.index(i) );
   }
}