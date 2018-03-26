//import java.util.Arrays;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
    
   private Digraph G;

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
       this.G = new Digraph(G);
   }
   
   // check if iterable v and w are valid
   private boolean isValid(Iterable<Integer> v) {
       if (v == null) return false;
       for (int i : v) {
           if (i < 0 || i > G.V()-1) return false;
       }
       return true;
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
       if (v < 0 || w < 0 || v > G.V()-1 || w > G.V()-1) throw new IllegalArgumentException();
       BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
       BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
       int ancestor = ancestor(v, w);
       if (ancestor == -1) return -1;
       return bfsv.distTo(ancestor) + bfsw.distTo(ancestor);
   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w) {
       if (v < 0 || w < 0 || v > G.V()-1 || w > G.V()-1) throw new IllegalArgumentException();
       BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
       BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
       int shortest = -1;
       int ancestor = -1;
       for (int x = 0; x < G.V(); x++) {
           if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {
               int dist = bfsv.distTo(x) + bfsw.distTo(x);
               if (shortest == -1 || dist < shortest) {
                   shortest = dist;
                   ancestor = x;
               }
           }
       }
       return ancestor;       
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
       if (!(isValid(v) && isValid(w))) throw new IllegalArgumentException();
       BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
       BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
       int ancestor = ancestor(v, w);
       if (ancestor == -1) return -1;
       return bfsv.distTo(ancestor) + bfsw.distTo(ancestor);
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
       if (!(isValid(v) && isValid(w))) throw new IllegalArgumentException();
       BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
       BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
       int shortest = -1;
       int ancestor = -1;
       for (int x = 0; x < G.V(); x++) {
           if (bfsv.hasPathTo(x) && bfsw.hasPathTo(x)) {
               int dist = bfsv.distTo(x) + bfsw.distTo(x);
               if (shortest == -1 || dist < shortest) {
                   shortest = dist;
                   ancestor = x;
               }
           }
       }
       return ancestor;    
   }

   // do unit testing of this class
   public static void main(String[] args) {
        In in = new In("C:\\Users\\ruopei\\algs4\\wordnet\\digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int length, ancestor;

        length = sap.length(3, 11);
        ancestor = sap.ancestor(3, 11);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        length = sap.length(9, 12);
        ancestor = sap.ancestor(9, 12);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        length = sap.length(7, 2);
        ancestor = sap.ancestor(7, 2);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        length = sap.length(1, 6);
        ancestor = sap.ancestor(1, 6);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
   }
}
