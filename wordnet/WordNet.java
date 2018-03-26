import java.util.HashMap;
import java.util.HashSet;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.StdIn;
//import edu.princeton.cs.algs4.StdOut;

public class WordNet { 

    private Digraph G;
    private SAP sap;
    private int numSynsets;
    private HashMap<Integer, String> idToSynset;
    private HashMap<String, HashSet<Integer>> nounToIds;
    
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        this.idToSynset = new HashMap<Integer, String>();
        this.nounToIds = new HashMap<String, HashSet<Integer>>();
        readSynsets(synsets);
        readHypernyms(hypernyms);
    }

    private void readSynsets(String synsets) {
        
        In in = new In(synsets);
        numSynsets = 0;
        String line;
        while((line = in.readLine()) != null) {
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String synset = fields[1];
            String[] nouns = synset.split(" ");
            HashSet<Integer> currentIds = null;
            
            
            // implement nounToIds
            for (String noun : nouns) {
                if (this.nounToIds.containsKey(noun)) {
                    currentIds = this.nounToIds.get(noun);
                    currentIds.add(id);
                    this.nounToIds.put(noun, currentIds);
                } else {
                    currentIds = new HashSet<Integer>();
                    currentIds.add(id);
                    this.nounToIds.put(noun, currentIds);
                }
            }
            
            // implement idToSynset
            this.idToSynset.put(id, synset);
            numSynsets++;
        }            

    }
        
    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        this.G = new Digraph(numSynsets);
        String line;
        int numRoots = 0;
        
        while((line = in.readLine()) != null) {
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            
            if (fields.length == 1) numRoots++;
            
            for (int i = 1; i < fields.length; i++) {
                G.addEdge(id, Integer.parseInt(fields[i]));
            }
        }
        
        if (numRoots > 1) throw new java.lang.IllegalArgumentException();
        
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.IllegalArgumentException();
        return this.nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException();
        if (!this.nounToIds.containsKey(nounA) || !this.nounToIds.containsKey(nounA))
            throw new java.lang.IllegalArgumentException();
        return this.sap.length(this.nounToIds.get(nounA), this.nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new java.lang.IllegalArgumentException();
        if (!this.nounToIds.containsKey(nounA) || !this.nounToIds.containsKey(nounA))
            throw new java.lang.IllegalArgumentException();
        int ancestor = this.sap.ancestor(this.nounToIds.get(nounA), this.nounToIds.get(nounB));
        return this.idToSynset.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {}
}