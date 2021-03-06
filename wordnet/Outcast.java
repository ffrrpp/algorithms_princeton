import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    
    private WordNet wordnet;
    
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    public String outcast(String[] nouns) {
        String result = null;
        int maxDist = 0;
        
        for (String noun : nouns) {
            int dist = 0;
            for (String another : nouns) {
                if (noun != another) {
                    dist += this.wordnet.distance(noun, another);
                }
            }
            
            if (dist > maxDist) {
                maxDist = dist;
                result = noun;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
    
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}