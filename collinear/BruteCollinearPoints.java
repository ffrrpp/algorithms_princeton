import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    
    private LineSegment[] segs;
    private int numberOfSegments;
    
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();
        
        int numberOfPoints = points.length;
        segs = new LineSegment[numberOfPoints];
        Point[] copy = Arrays.copyOf(points, numberOfPoints);
        Arrays.sort(copy);
        checkRepeatedPoints(copy);
        
        double slope12;
        double slope13;
        double slope14;
        numberOfSegments = 0;
        for (int i1 = 0; i1 < numberOfPoints-3; i1++) {
            Point p1 = copy[i1];
            if (p1 == null) throw new IllegalArgumentException();
            for (int i2 = i1 + 1; i2 < numberOfPoints-2; i2++) {
                Point p2 = copy[i2];
                if (p2 == null) throw new IllegalArgumentException();
                slope12 = p1.slopeTo(p2);
                for (int i3 = i2 + 1; i3 < numberOfPoints-1; i3++) {
                    Point p3 = copy[i3];
                    if (p3 == null) throw new IllegalArgumentException();
                    slope13 = p1.slopeTo(p3);
                    if (slope12 != slope13) continue;
                    for (int i4 = i3 + 1; i4 < numberOfPoints; i4++) {
                        Point p4 = copy[i4];
                        if (p4 == null) throw new IllegalArgumentException();
                        slope14 = p1.slopeTo(p4);
                        if (slope14 == slope12) {
                            /* StdOut.println(" " + slope12 + " " + slope13 + " " + slope14);
                             StdOut.println(p1);
                             StdOut.println(p2);
                             StdOut.println(p3);
                             StdOut.println(p4); 
                            StdOut.println(numberOfSegments);*/
                            segs[numberOfSegments] = new LineSegment(p1, p4);
                            
                            numberOfSegments++;
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}