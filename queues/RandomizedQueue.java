import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq;
    private int n;
    
    public RandomizedQueue() {                // construct an empty randomized queue
        rq = (Item[]) new Object[2];
        n = 0;
    }
        
    public boolean isEmpty() {                // is the queue empty?
        return n == 0;
    }
    
    public int size() {                        // return the number of items on the queue
        return n;
    }
    
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = rq[i];
        }
        rq = temp;
    }
    
    public void enqueue(Item item) {           // add the item
        if (item == null) throw new IllegalArgumentException("Null item");
        if (n == rq.length) resize(2*rq.length);
        rq[n++] = item;
    }    
    
    public Item dequeue() {                   // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException("RQ underflow");
        int idx = StdRandom.uniform(n);
        Item item = rq[idx];
        rq[idx] = rq[n-1];
        rq[n-1] = null;
        n--;
        if (n > 0 && n == rq.length/4) resize(rq.length/2);
        return item;
    }        
        
    public Item sample() {                    // return (but do not remove) a random item
        if (isEmpty()) throw new NoSuchElementException("RQ underflow");
        int idx = StdRandom.uniform(n);
        return rq[idx];
    }                
        
    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item> {
        private Item[] temp;
        private int i = 0;
        
        private ArrayIterator() {
            temp = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                temp[i] = rq[i];
            } 
            StdRandom.shuffle(temp);
        }
      
        public boolean hasNext() {
            return i < n;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = temp[i];
            i++;
            return item;
        }
    }
            
    public static void main(String[] args) {  // unit testing (optional)
        RandomizedQueue<String> test = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) test.enqueue(item);
            else if (!test.isEmpty()) StdOut.print(test.dequeue() + " ");
        }
        StdOut.println("(" + test.size() + " left on Randomized Queue)");
    }
}