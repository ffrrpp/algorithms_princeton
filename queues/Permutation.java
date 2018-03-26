import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }
        if (k < 0 || k > rq.size()) throw new IllegalArgumentException();
        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}