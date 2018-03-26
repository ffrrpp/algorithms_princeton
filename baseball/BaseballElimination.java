import java.util.HashMap;
import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {
    
    private int numTeams;
    private HashMap<String, Integer> nameToIdx;
    private HashMap<Integer, String> idxToName;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    // private FlowNetwork flowNetwork;

    public BaseballElimination(String filename) {                   // create a baseball division from given filename in format specified below
        In in = new In(filename);
        this.nameToIdx = new HashMap<String, Integer>();
        this.idxToName = new HashMap<Integer, String>();
        // first line: number of teams
        String line = in.readLine();
        int teamIdx = 0;
        this.numTeams = Integer.parseInt(line);
        this.w = new int[numTeams];
        this.l = new int[numTeams];
        this.r = new int[numTeams];
        this.g = new int[numTeams][numTeams];
        while((line = in.readLine()) != null) {
            String[] fields = line.trim().split("\\s+");
            String teamName = fields[0];
            this.nameToIdx.put(teamName, teamIdx);
            this.idxToName.put(teamIdx, teamName);
            w[teamIdx] = Integer.parseInt(fields[1]);
            l[teamIdx] = Integer.parseInt(fields[2]);
            r[teamIdx] = Integer.parseInt(fields[3]);
            for (int j = 0; j < numTeams; j++) {
                this.g[teamIdx][j] = Integer.parseInt(fields[4+j]);
            }
            teamIdx++;
        }       
    }
    
    private FlowNetwork buildFlowNetwork(int teamIdx) {

        int numGameVertices = (numTeams-1) * (numTeams-2) / 2;
        int V = 1 + numGameVertices + (numTeams-1) + 1;
        FlowNetwork flowNetwork = new FlowNetwork(V);
        
        // source vertex and sink vertex
        int s = 0;
        int t = V-1;
        
        int gameVertexIdx = 1;
        // link source vertex to game vertices
        for (int i = 0; i < numTeams; i++) {
            for (int j = 0; j < i; j++) {
                
                if (i != teamIdx && j != teamIdx) {
                    int teamVertexIdx_i = i < teamIdx ? 1 + numGameVertices + i : numGameVertices + i;
                    int teamVertexIdx_j = j < teamIdx ? 1 + numGameVertices + j : numGameVertices + j;
                    flowNetwork.addEdge(new FlowEdge(s, gameVertexIdx, this.g[i][j]));
                    
                    flowNetwork.addEdge(new FlowEdge(gameVertexIdx, teamVertexIdx_i, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gameVertexIdx, teamVertexIdx_j, Double.POSITIVE_INFINITY));
                    
                    gameVertexIdx++;
                }
            }
        }
        
        for (int i = 0; i < numTeams; i++) {
            if (i != teamIdx) {
                int teamVertexIdx_i = i < teamIdx ? 1 + numGameVertices + i : numGameVertices + i;
                flowNetwork.addEdge(new FlowEdge(teamVertexIdx_i, t, w[teamIdx] + r[teamIdx] - w[i]));
            }
        }
        
        return flowNetwork;
    }
    
    
    public int numberOfTeams() {                       // number of teams
        return this.numTeams;
    }
    
    public Iterable<String> teams() {                  // all teams
        return nameToIdx.keySet();
    }
    
    public int wins(String team) {                     // number of wins for given team
        if (nameToIdx.get(team) == null) throw new java.lang.IllegalArgumentException();
        return w[nameToIdx.get(team)];
    }
    
    public int losses(String team) {                   // number of losses for given team        
        if (nameToIdx.get(team) == null) throw new java.lang.IllegalArgumentException();
        return l[nameToIdx.get(team)];
    }
    
    public int remaining(String team) {                // number of remaining games for given team
        if (nameToIdx.get(team) == null) throw new java.lang.IllegalArgumentException();
        return r[nameToIdx.get(team)];
    }
    
    public int against(String team1, String team2) {   // number of remaining games between team1 and team2
        if (nameToIdx.get(team1) == null || nameToIdx.get(team2) == null) throw new java.lang.IllegalArgumentException();
        return g[nameToIdx.get(team1)][nameToIdx.get(team2)];
    }
    
    public boolean isEliminated(String team) {             // is given team eliminated?
        if (nameToIdx.get(team) == null) throw new java.lang.IllegalArgumentException();
        int teamIdx = nameToIdx.get(team);
        
        // trivial elimination
        for (int i = 0; i < numTeams; i++) {
            if (w[teamIdx] + r[teamIdx] < w[i]) {
               return true;
            }
        }
        
        // nontrivial elimination
        FlowNetwork flowNetwork = buildFlowNetwork(teamIdx);
        int numGameVertices = (numTeams-1) * (numTeams-2) / 2;
        int V = 1 + numGameVertices + (numTeams-1) + 1;
        FordFulkerson ff = new FordFulkerson(flowNetwork, 0, V-1);
        int gameVertexIdx = 1;
        
        for (int i = 0; i < numTeams; i++) {
            for (int j = 0; j < i; j++) {
                if (i != teamIdx && j != teamIdx) {
                    if (ff.inCut(gameVertexIdx)) {
                        return true;
                    }
                    gameVertexIdx++;
                }
            }
        }
        
        return false;
    }
    
    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
        if (nameToIdx.get(team) == null) throw new java.lang.IllegalArgumentException();
        
        HashSet<String> certificate = new HashSet<String>();
        int teamIdx = nameToIdx.get(team);
        
        // trivial elimination
        for (int i = 0; i < numTeams; i++) {
            if (w[teamIdx] + r[teamIdx] < w[i]) {
                certificate.add(idxToName.get(i));
            }
        }
        
        // nontrivial elimination
        if (certificate.isEmpty()) {
            FlowNetwork flowNetwork = buildFlowNetwork(teamIdx);
            int numGameVertices = (numTeams-1) * (numTeams-2) / 2;
            int V = 1 + numGameVertices + (numTeams-1) + 1;
            FordFulkerson ff = new FordFulkerson(flowNetwork, 0, V-1);
            int gameVertexIdx = 1;
            for (int i = 0; i < numTeams; i++) {
                for (int j = 0; j < i; j++) {                
                    if (i != teamIdx && j != teamIdx) {
                        if (ff.inCut(gameVertexIdx)) {
                            certificate.add(idxToName.get(i));
                            certificate.add(idxToName.get(j));
                        }                            
                        gameVertexIdx++;
                    }
                }
            }
        }
        
        return certificate;
    }
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // FlowNetwork team0 = division.buildFlowNetwork(0);
        // StdOut.println(team0);
        
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}