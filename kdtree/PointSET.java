import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> PointSET;
    public         PointSET() {                              // construct an empty set of points 
        PointSET = new SET<Point2D>();
    };
    
    public           boolean isEmpty() {                     // is the set empty? 
        return PointSET.isEmpty();
    }
    
    public               int size() {                        // number of points in the set 
        return PointSET.size();
    }
    
    public              void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        PointSET.add(p);
    }
    
    public           boolean contains(Point2D p) {           // does the set contain point p? 
        return PointSET.contains(p);
    }
    
    public              void draw() {                        // draw all points to standard draw 
        for (Point2D point: PointSET) {
            point.draw();
        }
    }
        
    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary) 
        SET<Point2D> PointsInside = new SET<Point2D>();
        if (PointSET.isEmpty()) return PointsInside;
        for (Point2D point: PointSET) {
            if (rect.contains(point)) {
                PointsInside.add(point);
            }
        }
        return PointsInside;
    }
        
    public           Point2D nearest(Point2D p) {           // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null) throw new IllegalArgumentException();
        if (PointSET.isEmpty()) return null;
        Point2D nearestPoint = null;
        double nearestDist = 1.0/0.0;
        for (Point2D point: PointSET) {
            double dist = p.distanceTo(point);
            if (dist < nearestDist) {
                nearestPoint = point;
                nearestDist = dist;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {}                 // unit testing of the methods (optional) 
}