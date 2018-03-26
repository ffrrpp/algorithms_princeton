import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Stack;

public class KdTree {
    private Node root;
    private int n;
    private boolean keyIsX;
    private RectHV currRect;
    private Stack<Node> NodeStack; // just to use its iterator
    private Stack<Point2D> PointsInside;
    private Point2D nearestPoint;
    private double nearestDist;
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean orientation; // use x coordinate as key if true, y as key if false
        
        public Node (Point2D p, RectHV rect, boolean orientation) {
            this.p = p;
            this.rect = rect;
            this.orientation = orientation;
        }
    }
    
    public KdTree() {
        n = 0;
        NodeStack = new Stack<Node>();
    }
    
    public int size() {
        return n;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException ("empty point");
        // use x coordinate as key if true, y as key if false
        keyIsX = true;
        currRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        root = insert(root, p, keyIsX, currRect);        
    }
    
    private Node insert(Node node, Point2D p, boolean keyIsX, RectHV currRect) {
        if (node == null) {
            n++;
            Node newNode = new Node(p, currRect, keyIsX);
            NodeStack.push(newNode);
            return newNode;
        }
        
        if (p.equals(node.p)) return node;
        
        int cmp = comparePoints(p, node.p, keyIsX);        
        if (cmp < 0) {
            if (node.lb != null) currRect = node.lb.rect;
            else if (keyIsX)     currRect = new RectHV(node.rect.xmin(),node.rect.ymin(),node.p.x(),node.rect.ymax());
            else if (!keyIsX)    currRect = new RectHV(node.rect.xmin(),node.rect.ymin(),node.rect.xmax(),node.p.y());
            keyIsX = !keyIsX;
            node.lb = insert(node.lb, p, keyIsX, currRect);
        }            
        else if (cmp >= 0) {            
            if (node.rt != null) currRect = node.rt.rect;
            else if (keyIsX)     currRect = new RectHV(node.p.x(),node.rect.ymin(),node.rect.xmax(),node.rect.ymax());
            else if (!keyIsX)    currRect = new RectHV(node.rect.xmin(),node.p.y(),node.rect.xmax(),node.rect.ymax());
            keyIsX = !keyIsX;
            node.rt = insert(node.rt, p, keyIsX, currRect);
        }      
        return node;        
    }
    
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (n == 0) return false;
        keyIsX = true;
        return contains(root, p, keyIsX);
    }
    
    private boolean contains(Node node, Point2D p, boolean keyIsX) {
        if (p.equals(node.p)) return true;
        int cmp = comparePoints(p, node.p, keyIsX);
        keyIsX = !keyIsX;
        if(cmp < 0 && node.lb != null) return contains(node.lb, p, keyIsX);
        if(cmp >= 0 && node.rt != null) return contains(node.rt, p, keyIsX);
        return false;
    }
    
    private int comparePoints(Point2D p1, Point2D p2, boolean keyIsX) {
        if (keyIsX) {
            if (p1.x() < p2.x()) return -1;
            if (p1.x() > p2.x()) return 1;
            return 0;
        }
        else { // (key is y)
            if (p1.y() < p2.y()) return -1;
            if (p1.y() > p2.y()) return 1;
            return 0;
        }
    }
    
    public void draw() {                        // draw all points to standard draw 
        for (Node node: NodeStack) {
            Point2D point = node.p;
            RectHV rect = node.rect;
            boolean orientation = node.orientation;
            
            // draw point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            point.draw();
            
            // draw line
            if (orientation) {
                Point2D p1 = new Point2D(point.x(), rect.ymin());
                Point2D p2 = new Point2D(point.x(), rect.ymax());
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(0.005);
                p1.drawTo(p2);
            }
            else {                
                Point2D p1 = new Point2D(rect.xmin(), point.y());
                Point2D p2 = new Point2D(rect.xmax(), point.y());
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(0.005);                
                p1.drawTo(p2);
            }
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {  // all points that are inside the rectangle (or on the boundary) 
        PointsInside = new Stack<Point2D>();
        if (n == 0) return PointsInside;
        range(rect, root);
        return PointsInside;
    }
    
    private void range(RectHV rect, Node root) {
        if (rect.contains(root.p)) PointsInside.push(root.p);
        if (root.lb != null && root.lb.rect.intersects(rect)) range(rect, root.lb);
        if (root.rt != null && root.rt.rect.intersects(rect)) range(rect, root.rt);
    }
    
    public Point2D nearest(Point2D p) {           // a nearest neighbor in the KdTree to point p; null if the set is empty 
        if (p == null) throw new IllegalArgumentException();
        if (n == 0) return null;
        nearestPoint = null;
        nearestDist = 1.0/0.0;

        nearest(p, root);
        return nearestPoint;
    }
        
    private void nearest(Point2D p, Node root) {
        if (root.p.distanceSquaredTo(p) < nearestDist) {
        nearestDist = root.p.distanceSquaredTo(p);
        nearestPoint = root.p;
        }
        if (root.lb != null && root.lb.rect.distanceSquaredTo(p) < nearestDist) {
            nearest(p, root.lb);
        }
        if (root.rt != null && root.rt.rect.distanceSquaredTo(p) < nearestDist) {
            nearest(p, root.rt);
        }
    }
        
        
    public static void main(String[] args) {
/*        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.1,0.3);
        Point2D p2 = new Point2D(0.2,0.2);
        Point2D p3 = new Point2D(0.3,0.6);
        Point2D p4 = new Point2D(0.5,0.4);
        tree.insert(p1);
        tree.insert(p2);
        tree.insert(p3);
        tree.insert(p4);
        StdOut.println("contains p1: " + tree.contains(p1));
        StdOut.println("contains p2: " + tree.contains(p2));
        StdOut.println("contains p3: " + tree.contains(p3));
        StdOut.println("contains p4: " + tree.contains(p4));
        
        tree.draw();*/
    }
}