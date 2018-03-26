import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private WeightedQuickUnionUF sites;
    private int width;
    private boolean[] siteStatus;
    private int openSitesCount;

    
    public Percolation(int n) {               // create n-by-n grid, with all sites blocked
        if (n > 0) {
            width = n;
            siteStatus = new boolean[width * width];
            sites = new WeightedQuickUnionUF((width + 2) * width);
            openSitesCount = 0;
            // add two virtual rows at the top and bottom of the grid, respectively
            int idx1DFirstRowFirstCol = 0;
            int idx1DLastRowFirstCol = xyTo1D(width + 1, 1);
            for (int i = 1; i < width; i++) {
                sites.union(idx1DFirstRowFirstCol, idx1DFirstRowFirstCol + i);
                sites.union(idx1DLastRowFirstCol, idx1DLastRowFirstCol + i);                
            }
            // System.out.println(sites.count());

        } else {
            throw new IllegalArgumentException("n must be a positive number");
        }
    }
       
    public    void open(int row, int col) {   // open site (row, col) if it is not open already
        if (!isOpen(row, col)) {
            int idx1D = xyTo1D(row, col);
            siteStatus[idx1D-width] = true;
            openSitesCount++;                
            // System.out.println("row is "+row + " col is " + col + " idx1D is " + idx1D);
            
            // neighbour above

            if (isOpen(row-1, col)) {
                sites.union(idx1D, idx1D-width);
                // System.out.println("idx1D is " + idx1D);
                // System.out.println("idx1D-width is "+(idx1D-width));
            }

            // neighbour below

            if (isOpen(row+1, col)) {
                sites.union(idx1D, idx1D+width);
                // System.out.println("idx1D is " + idx1D);
                // System.out.println("idx1D+width is "+(idx1D+width));
            }
                
            // neighbour to the left
            if (col != 1) {            
                if (isOpen(row, col-1)) {
                    sites.union(idx1D, idx1D-1);
                }
            }
            // neighbour to the right
            if (col != width) {            
                if (isOpen(row, col+1)) {
                    sites.union(idx1D, idx1D+1);
                }
            }
        }
    }
        
    public boolean isOpen(int row, int col) { // is site (row, col) open?
        if (row >= 0 && row <= width + 1 && col >= 1 && col <= width) {
            if (row == 0 || row == width + 1) {
                return true;
            }
            else {
                int idx1D = xyTo1D(row, col); 
                return siteStatus[idx1D-width];
            }
        }
        else {
            throw new IllegalArgumentException("illegal row or col");
        }
    }
            
    public boolean isFull(int row, int col) { // is site (row, col) full?
        if (row >= 0 && row <= width + 1 && col >= 1 && col <= width) {
            if (isOpen(row, col)){
                int idx1D = xyTo1D(row, col);
                // 0 is the index of the first row first col
                if (sites.connected(idx1D, 0)) {
                    return true;
                }
            }
            return false;
        }
        else {
            throw new IllegalArgumentException("illegal row or col");
        }
    }
        
                
    public     int numberOfOpenSites() {      // number of open sites
        return openSitesCount;
    }
        
    public boolean percolates() {             // does the system percolate?
        int idx1DFirstRowFirstCol = 0;
        int idx1DLastRowFirstCol = xyTo1D(width + 1, 1);
        // System.out.println(" " +sites.find(0)+" " +sites.find(1)+" " +sites.find(2)
        //                       +" " +sites.find(3)+" " +sites.find(4)+" " +sites.find(5)
         //                      +" " +sites.find(6)+" " +sites.find(7)+" " +sites.find(8)
        //                       +" " +sites.find(9)+" " +sites.find(10)+" " +sites.find(11)
         //                 +" " +sites.find(12)+" " +sites.find(13)+" " +sites.find(14));
        return sites.connected(idx1DFirstRowFirstCol, idx1DLastRowFirstCol);
    }
        
    private int xyTo1D(int row, int col) {
        return row * width + (col-1);
    }

       
   // public static void main(String[] args)   // test client (optional)
}