import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    
    private int width;
    private int height;
    private boolean isTransposed;
    private boolean Horizontal;
    private Color[][] colorMatrix;
    private double[][] energyMatrix;
    // private int[] horizontalSeam;
    // private int[] verticalSeam;
    
    public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
        if (picture == null) throw new java.lang.IllegalArgumentException();
        
        this.width = picture.width();
        this.height = picture.height();
        this.colorMatrix = new Color[width][height];
        this.energyMatrix = new double[width][height];
        this.isTransposed = false;
        this.Horizontal = true;
        for(int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                this.colorMatrix[col][row] = picture.get(col,row);
            }
        }
        
    }
    
    
    public Picture picture() {                         // current picture
        if(isTransposed) transposeColorMatrix();
        
        Picture picture = new Picture(width, height);
        for(int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                picture.set(col, row, this.colorMatrix[col][row]);
            }
        }
        return picture;
    }
    
    public     int width() {                           // width of current picture
        if (isTransposed) transposeColorMatrix();
        return this.width;
    }
    
    public     int height() {                          // height of current picture 
        if (isTransposed) transposeColorMatrix();
        return this.height;
    }
    
    public  double energy(int x, int y) {              // energy of pixel at column x and row y
        // StdOut.printf("%d", width);
        if (x < 0 || x > this.width-1 || y < 0 || y > this.height-1) {
            throw new java.lang.IllegalArgumentException();
        } else if (x == 0 || x == this.width-1 || y == 0 || y == this.height-1) {
            return 1000.0;
        } else {
            double delta2_x = Math.pow(colorMatrix[x+1][y].getRed() - colorMatrix[x-1][y].getRed(),2)
                + Math.pow(colorMatrix[x+1][y].getGreen() - colorMatrix[x-1][y].getGreen(),2)
                + Math.pow(colorMatrix[x+1][y].getBlue() - colorMatrix[x-1][y].getBlue(),2);
            double delta2_y = Math.pow(colorMatrix[x][y+1].getRed() - colorMatrix[x][y-1].getRed(),2)
                + Math.pow(colorMatrix[x][y+1].getGreen() - colorMatrix[x][y-1].getGreen(),2)
                + Math.pow(colorMatrix[x][y+1].getBlue() - colorMatrix[x][y-1].getBlue(),2);
            return Math.sqrt(delta2_x+delta2_y);
        }        
    }
    
    private void updateEnergyMatrix() {
        
        this.energyMatrix = new double[width][height];
        
        for(int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                this.energyMatrix[col][row] = energy(col,row);
            }
        }
    }
    
    private void transposeColorMatrix() {
        Color[][] transposedMatrix = new Color[height][width];
        for(int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                transposedMatrix[row][col] = this.colorMatrix[col][row];
            }
        }
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
        this.colorMatrix = transposedMatrix;
        this.isTransposed = !this.isTransposed;
    }
    
    /*
    public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        
        this.Vertical = false;
        if(!isTransposed) transposeColorMatrix();

        int[] horizontalSeam = findVerticalSeam();
        this.Vertical = true;
        return horizontalSeam;
    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
        
        if (this.Vertical && this.isTransposed) {
            transposeColorMatrix();
        }
        
        // use energyMatrx to store "distTo"
        updateEnergyMatrix();
        
        int[] verticalSeam = new int[height];
        double[] distTo = new double[width];
        int[][] edgeTo = new int[width][height-1];
        
        for(int row = 0; row < height-1; row++) {
            Arrays.fill(distTo, Double.POSITIVE_INFINITY);
            for(int col = 0; col < width; col++) {
                // SW
                if (col > 0 && energyMatrix[col][row] + energyMatrix[col-1][row+1] < distTo[col-1]) {
                    distTo[col-1] = energyMatrix[col][row] + energyMatrix[col-1][row+1];
                    edgeTo[col-1][row] = col;
                }
                
                // S
                if (energyMatrix[col][row] + energyMatrix[col][row+1] < distTo[col]) {
                    distTo[col] = energyMatrix[col][row] + energyMatrix[col][row+1];
                    edgeTo[col][row] = col;
                }
                
                // SE
                if (col < width-1 && energyMatrix[col][row] + energyMatrix[col+1][row+1] < distTo[col+1]) {
                    distTo[col+1] = energyMatrix[col][row] + energyMatrix[col+1][row+1];
                    edgeTo[col+1][row] = col;
                }
            }
            
            // update energyMatrix
            for(int col = 0; col < width; col++) {
                energyMatrix[col][row+1] = distTo[col];
            }
        }
        
        // find vertical seam
        double minDist = Double.POSITIVE_INFINITY;
        int seamCol = 0;
        for(int col = 0; col < width; col++) {
            if (distTo[col] < minDist) {
                minDist = distTo[col];
                seamCol = col;
            }
        }
        verticalSeam[height-1] = seamCol;
        
        for(int row = height-2; row >= 0; row--) {
            verticalSeam[row] = edgeTo[verticalSeam[row+1]][row];
        }
        
        return verticalSeam;
    }
    */
    
    
    
    
    
    
    
    
    
    
    public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        if (this.Horizontal && this.isTransposed) {
            transposeColorMatrix();
        }
        
        // use energyMatrx to store "distTo"
        updateEnergyMatrix();
        
        int[] horizontalSeam = new int[width];
        double[] distTo = new double[height];
        int[][] edgeTo = new int[width-1][height];
        
        for(int col = 0; col < width-1; col++) {
            Arrays.fill(distTo, Double.POSITIVE_INFINITY);
            for(int row = 0; row < height; row++) {
                // SW
                if (row > 0 && energyMatrix[col][row] + energyMatrix[col+1][row-1] < distTo[row-1]) {
                    distTo[row-1] = energyMatrix[col][row] + energyMatrix[col+1][row-1];
                    edgeTo[col][row-1] =row;
                }
                
                // S
                if (energyMatrix[col][row] + energyMatrix[col+1][row] < distTo[row]) {
                    distTo[row] = energyMatrix[col][row] + energyMatrix[col+1][row];
                    edgeTo[col][row] = row;
                }
                
                // SE
                if (row < height-1 && energyMatrix[col][row] + energyMatrix[col+1][row+1] < distTo[row+1]) {
                    distTo[row+1] = energyMatrix[col][row] + energyMatrix[col+1][row+1];
                    edgeTo[col][row+1] = row;
                }
            }
            
            // update energyMatrix
            for(int row = 0; row < height; row++) {
                energyMatrix[col+1][row] = distTo[row];
            }
        }
        
        // find Horizontal seam
        double minDist = Double.POSITIVE_INFINITY;
        int seamRow = 0;
        for(int row = 0; row < height; row++) {
            if (distTo[row] < minDist) {
                minDist = distTo[row];
                seamRow = row;
            }
        }
        horizontalSeam[width-1] = seamRow;
        
        for(int col = width-2; col >= 0; col--) {
            horizontalSeam[col] = edgeTo[col][horizontalSeam[col+1]];
        }
        
        return horizontalSeam;

    }
    
    public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
                     
        this.Horizontal = false;
        if(!isTransposed) transposeColorMatrix();
        int[] horizontalSeam = findHorizontalSeam();
        this.Horizontal = true;
        return horizontalSeam;
    }
    
       
    
    
    
    public    void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
        if (seam == null) throw new java.lang.IllegalArgumentException();
        if (height <= 1) throw new java.lang.IllegalArgumentException();
        if (this.Horizontal && this.isTransposed) {
            transposeColorMatrix();
        }
        
        Color[][] newColorMatrix = new Color[width][height-1];
        
        for(int col = 0; col < width; col++) {
            System.arraycopy(colorMatrix[col],0,newColorMatrix[col],0,height-1);
            System.arraycopy(colorMatrix[col],seam[col]+1,newColorMatrix[col],seam[col],height-1-seam[col]);
        }
        height--;
        
        this.colorMatrix = newColorMatrix;
    }
    
    public    void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
        if (seam == null) throw new java.lang.IllegalArgumentException();
        
        this.Horizontal = false;
        if(!isTransposed) transposeColorMatrix();
        removeHorizontalSeam(seam);
        
        this.Horizontal = true;
    }
    
}