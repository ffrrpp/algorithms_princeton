import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    
    private LineSegment[] segs;
    private int numberOfSegments;
    
    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();
        
        int numberOfPoints = points.length;
        int currentLength = numberOfPoints;
        segs = new LineSegment[currentLength];
        Point[] copy = Arrays.copyOf(points, numberOfPoints);
        Arrays.sort(copy);
        checkRepeatedPoints(copy);       
        
        numberOfSegments = 0;
        for (int i = 0; i < numberOfPoints-3; i++) {
            Point[] temp = Arrays.copyOfRange(copy, i, numberOfPoints);
            Arrays.sort(temp, temp[0].slopeOrder());
            // StdOut.println("i = " + i);
            int j = 1;
            while(j < temp.length) {
                int k = 1;
                while (j + k < temp.length && temp[0].slopeTo(temp[j]) == temp[0].slopeTo(temp[j+k])) {
                    // StdOut.println("temp0 = " + temp[0] + " j = " + j + " k = " + k);
                    // StdOut.println("temp j+k = " + temp[j+k]);
                    k++;
                }
                if (k > 2) {
                    // StdOut.println(" " + i + " " + j + " " + k + " " + temp.length);
                    // StdOut.println(temp[0]);
                    // StdOut.println(temp[j]);
                    // StdOut.println(temp[j+1]);
                    // StdOut.println(temp[j+k-1]);
                    segs[numberOfSegments] = new LineSegment(temp[0], temp[j+k-1]);
                    numberOfSegments++;
                    if (numberOfSegments == currentLength) {
                        LineSegment[] t = Arrays.copyOf(segs,currentLength*2);
                        segs = t;
                        currentLength = currentLength * 2;
                    }
                }
                j = j + k;
            }
        }
    }
    
    public int numberOfSegments() {       // the number of line segments
        return numberOfSegments;
    }
    
    public LineSegment[] segments() {               // the line segments
        return Arrays.copyOf(segs, numberOfSegments);
    }
    
    private void checkRepeatedPoints(Point[] points) {
        int numberOfPoints = points.length;
        for (int i = 0; i < numberOfPoints - 1; i++) {
            if (points[i].compareTo(points[i+1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public static void main(String[] args) {
        
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}