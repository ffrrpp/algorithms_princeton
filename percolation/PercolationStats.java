import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private static int n, trials;
    private double[] openSitesFraction;
    
    public PercolationStats(int n, int trials) {   // perform trialss independent experiments on an n-by-n grid
        openSitesFraction = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation experiment = new Percolation(n);
            int count = 0;
            while (!experiment.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!experiment.isOpen(row, col)) {
                    experiment.open(row, col);
                    count++;
                }
            }
            openSitesFraction[i] = 1.0 * count/(n*n);
        }
        
    }
        
    public double mean() {                         // sample mean of percolation threshold
        return StdStats.mean(openSitesFraction);
    }
            
    public double stddev() {                       // sample standard deviation of percolation threshold
        if (trials == 1) {
            return Double.NaN;
        }
        else {
            return StdStats.stddev(openSitesFraction);
        }
    }
    
    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return this.mean() - 1.96*this.stddev()/Math.sqrt(trials);
    }
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return this.mean() + 1.96*this.stddev()/Math.sqrt(trials);
    }
    
    public static void main(String[] args) {       // test client (described below)
        n = Integer.parseInt(args[0]);
        trials = Integer.parseInt(args[1]);
        
        if (n > 0 && trials > 0) {
            PercolationStats results = new PercolationStats(n, trials);
            System.out.println("mean =" + results.mean());
            System.out.println("stddev =" + results.stddev());
            System.out.println("95% confidence interval = [" + results.confidenceLo() + ", " + results.confidenceHi() + "]");
        }
        else {
            throw new IllegalArgumentException("n and trials must be positive numbers");
        }
    }

}