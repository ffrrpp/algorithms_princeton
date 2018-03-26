import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int n;
    
    private static class Node<Item> {
        private Item item;
        private Node<Item> prev;
        private Node<Item> next;
    }
    
    public Deque() {                          // construct an empty deque
        first = null;
        last = null;
        n = 0;
    }
        
    public boolean isEmpty() {                  // is the deque empty?
        return first == null;
    }   
        
    public int size() {                       // return the number of items on the deque
        return n;
    }
    
    public void addFirst(Item item) {         // add the item to the front
        if (item == null) throw new IllegalArgumentException("Null item");
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.prev = null;
        first.next = oldfirst;
        if (last == null) last = first;
        else oldfirst.prev = first;
        n++;
    }
        
    public void addLast(Item item) {          // add the item to the end
        if (item == null) throw new IllegalArgumentException("Null item");
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (first == null) first = last;
        else oldlast.next = last;
        n++;
    }
        
    public Item removeFirst() {               // remove and return the item from the front
        if (isEmpty()) throw new NoSuchElementException("Empty deque");
        Item item = first.item;
        first = first.next;
        n--;
        if (first == null) last = null;
        else first.prev = null;
        return item;
    }        
        
    public Item removeLast() {                // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException("Empty deque");
        Item item = last.item;
        last = last.prev;
        n--;
        if (last == null) first = null;
        else last.next = null;
        return item;
    }        
                
    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new ListIterator<Item>(first);  
    }       
    
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        
        public ListIterator(Node<Item> first) {
            current = first;
        }
        
        public boolean hasNext() {
            return current != null;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
        
    public static void main(String[] args) { // unit testing (optional)
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) deque.addLast(item);
            else if (!deque.isEmpty()) StdOut.print(deque.removeLast() + " ");
        }
        StdOut.println("(" + deque.size() + " left on deque)");
    }
        
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    