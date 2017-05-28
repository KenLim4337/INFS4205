package geography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mbr {
  
  private RTree tree;
  private int id;
  private Point tl; // top left of rectangle
  private Point br; // bottom right
  private List<Point> points;
  private List<Mbr> children;
  private boolean isLeaf;
  private Mbr parent;
  private static int MAX_CHILDREN = 3;
  private int perimiter;
  
  public Mbr(Mbr parent, boolean isLeaf, RTree tree) {
    this.points = new ArrayList<Point>();
    this.parent = parent;
    this.isLeaf = isLeaf;
    this.tree = tree;
    this.perimiter = 0;
  }
  
  public Mbr(Mbr parent, RTree tree) {
    this(parent, true, tree);
  }

  //Mbr for range query purposes
  public Mbr(List<Point> p) {
      this.points = p;
      rangeInit();
  }
  
  public void insert(Point p) {
    if (isLeaf) {
      points.add(p);
      if (points.size() > MAX_CHILDREN) {
        handleOverflow();
      }
    } else {
      perimiter = getPerimiter();
      Point tl = getTl();
      Point br = getBr();
      int newPerimiter = Integer.MAX_VALUE;
      Mbr bestChild = null;
      for (Mbr child : children) {
        int candidateLeft = child.getTl().getX();
        int candidateTop = child.getTl().getY();
        
        int candidateRight = child.getBr().getX();
        int candidateBottom = child.getBr().getY();
        if (candidateLeft < this.getTl().getX()) {
          tl.setX(candidateLeft);
        }
        if (candidateTop > this.getTl().getY()) {
          tl.setY(candidateTop);
        }
        if (candidateRight > this.getBr().getX()) {
          br.setX(candidateRight);
        }
        if (candidateBottom < this.getBr().getY()) {
          br.setY(candidateBottom);
        }
        int horizontal = br.getX() - tl.getX();
        int vertical = tl.getY() - br.getY();
        int candidatePerimiter = (2 * horizontal) + (2 * vertical);
        if (candidatePerimiter < newPerimiter) {
          newPerimiter = candidatePerimiter;
          bestChild = child;
        }
      }
      bestChild.insert(p);
    }
    calculateCorners();
  }
  
  //Constructs bounds for a range query
  void rangeInit() {
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      
      for(Point x: this.points) {
          if(x.getX() > maxX) {
              maxX = x.getX();   
          }
          
          if (x.getX() < minX) {
              minX = x.getX();
          } 

          if (x.getY() > maxY) {
              maxY = x.getY();
          }

          if (x.getY() < minY) {
              minY = x.getY();
          }
      }
      
      this.br = new Point(maxX, maxY, -1);
      this.tl = new Point(minX, minY, -1);      
  }
  
  
  void calculateCorners() {
    int left = Integer.MAX_VALUE;
    int right = Integer.MIN_VALUE;
    int top = Integer.MIN_VALUE;
    int bottom = Integer.MAX_VALUE;
    if (isLeaf) {
      for (Point p : points) {
        if (p.getX() < left) {
          left = p.getX();
        } else if (p.getX() > right) {
          right = p.getX();
        }
        if (p.getY() > bottom) {
          bottom = p.getY();
        } else if (p.getY() < top) {
          top = p.getY();
        }
      }
    } else {
      // List of value of edges, reading clockwise from the top edge
      for (Mbr c : children) {
        if (c.getTl().getX() < left) {
          left = c.getTl().getX();
        }
        if (c.getBr().getX() > right) {
          right = c.getBr().getX();
        }
        if (c.getTl().getY() > top) {
          top = c.getTl().getY();
        }
        if (c.getBr().getY() < bottom) {
          bottom = c.getBr().getY();
        }
      }
    }
    tl = new Point(left, top, 0);
    br = new Point(right, bottom, 0);
  }
  
  private void handleOverflow() {
    // Create a new root if the current node being split is the root
    if (parent == null) {
      parent = new Mbr(null, false, tree);
    }
    
    // If this is a leaf node, split it into two new leaf nodes and make those
    // children of the parent.
    // If this is an internal node, split into two internal nodes and make those children.
    Mbr mbr1 = new Mbr(parent, isLeaf, tree);
    Mbr mbr2 = new Mbr(parent, isLeaf, tree);
    if (isLeaf) {
      sortLeaf(mbr1, mbr2, points);
      
    } else {
      List<Mbr> children1 = children.subList(0, MAX_CHILDREN / 2);
      List<Mbr> children2 = children.subList(MAX_CHILDREN / 2, children.size());
      mbr1.giveChildren(children1);
      mbr2.giveChildren(children2);
    }
    // If a root node, add the new root and give it the new children
    if (parent == null) {
      tree.setRoot(parent);
      parent.giveChildren(mbr1, mbr2);
    } else {
      parent.giveChildren(mbr1, mbr2);
      parent.calculateCorners();
      if (points.size() > MAX_CHILDREN || children.size() > MAX_CHILDREN) {
        parent.handleOverflow();
      }
    }
  }
  
  private void sortLeaf(Mbr mbr1, Mbr mbr2, List<Point> points) {
    points.sort(Point.compareX);
    List<Integer> xValues = findSplit(points);
    int bestI = xValues.get(0);
    int newPerimiter = xValues.get(1);
    
    points.sort(Point.compareY);
    List<Integer> yValues = findSplit(points);
    if (yValues.get(1) < newPerimiter) {
      newPerimiter = yValues.get(1);
      bestI = yValues.get(0);
    } else {
      points.sort(Point.compareX);
    }
    
    List<Point> points1 = points.subList(0, (int) Math.ceil(0.4 * MAX_CHILDREN));
    List<Point> points2 = points.subList((int) Math.ceil(0.4 * MAX_CHILDREN), points.size());
    mbr1.givePoints(points1);
    mbr2.givePoints(points2);
  }
  /**
   * 
   * @param points
   * @return A list of two integers. First value is the best split point,
   *         second value is the perimiter for the best split.
   */
  private List<Integer> findSplit(List<Point> points) {
    List<Integer> values = new ArrayList<Integer>();
    int newPerimiter = Integer.MAX_VALUE;
    int bestI = Integer.MAX_VALUE;
    int minSize = (int) Math.ceil(0.4 * MAX_CHILDREN);
    int perimiterSum = Integer.MAX_VALUE;
    for (int i = minSize; i < MAX_CHILDREN + 1 - minSize; ++i) {
      List<Point> points1 = points.subList(0, i);
      List<Point> points2 = points.subList(i, points.size());
      newPerimiter = perimiterSum(points1) + perimiterSum(points2);
      if (newPerimiter < perimiterSum) {
        perimiterSum = newPerimiter;
        bestI = i;
      }
    }
    values.add(bestI);
    values.add(newPerimiter);
    return values;
  }
  
  private int perimiterSum(List<Point> points) {
    Point tl = findTl(points);
    Point br = findBr(points);
    
    int horizontal = br.getX() - tl.getX();
    int vertical = tl.getY() - br.getY();
    return (2 * horizontal) + (2 * vertical);
  }
  
  public int getPerimiter() {
    if (isLeaf) {
      return perimiterSum(points);
    }
    tl = getTl();
    br = getBr();
    int horizontal = br.getX() - tl.getX();
    int vertical = tl.getY() - br.getY();
    return (2* horizontal) + (2* vertical);
  }
  
  private static Point findTl(List<Point> points) {
    int left = Integer.MAX_VALUE;
    int top = Integer.MIN_VALUE;
    for (Point p : points) {
      if (p.getX() < left) {
        left = p.getX();
      }
      if (p.getY() > top) {
        top = p.getY();
      }
    }
    return new Point(left, top, 0);
  }
  
  private static Point findBr(List<Point> points) {
    int right = Integer.MIN_VALUE;
    int bottom = Integer.MAX_VALUE;
    for (Point p : points) {
      if (p.getX() > right) {
        right = p.getX();
      }
      if (p.getY() < bottom) {
        bottom = p.getY();
      }
    }
    return new Point(right, bottom, 0);
  }
  
  void giveChildren(List<Mbr> mbrs) {
    if (children == null) {
      children = new ArrayList<Mbr>();
    }
    children.addAll(mbrs);
  }
  
  void giveChildren(Mbr mbr1, Mbr mbr2) {
    List<Mbr> mbrs = new ArrayList<Mbr>();
    mbrs.add(mbr1);
    mbrs.add(mbr2);
    giveChildren(mbrs);
  }
  
  void givePoints(List<Point> ps) {
    points.addAll(ps);
  }
  
  /**
   * 
   * @return true iff point p is contained within, or on the edge of, this Mbr object.
   */
  public boolean contains(Point p) {
    if (p.dominates(tl) && br.dominates(p)) {
      return true;
    }
    return false;
  }
  
  /**
   * 
   * @return true iff mbr is contained completely within this Mbr object.
   */
  public boolean contains(Mbr mbr) {
    if (mbr.getTl().dominates(this.tl) &&
        this.br.dominates(mbr.getBr())) {
      return true;
    }
    return false;
  }

  
  //Intersection code, returns false if no intersect else true
  public boolean intersects(Mbr mbr) {
      Point Destbr = mbr.getBr();
      Point Desttl = mbr.getTl();
      
      //Check Y axis stuff
      if(this.br.getX() < Desttl.getX() || Destbr.getX() < this.tl.getX() || 
              this.br.getY() < Desttl.getY() || Destbr.getY() < this.tl.getY()) {
          return false;
      } else {
          return true;
      }
  }
  
  
  public Point getTl() {
    if (isLeaf) {
      return findTl(this.points);
    }
    int left = Integer.MAX_VALUE;
    int top = Integer.MIN_VALUE;
    for (Mbr child : children) {
      left = Math.min(left, child.getTl().getX());
      top = Math.max(top, child.getTl().getY());
    }
    return new Point(left, top, 0);
  }

  public Point getBr() {
    if (isLeaf) {
      return findBr(this.points);
    }
    int right = Integer.MIN_VALUE;
    int bottom = Integer.MAX_VALUE;
    for (Mbr child : children) {
      right = Math.max(right, child.getBr().getX());
      bottom = Math.min(bottom, child.getBr().getY());
    }
    return new Point(right, bottom, 0);
  }
  
  public void setIsLeaf(boolean isLeaf) {
    this.isLeaf = isLeaf;
  }
  
  void setParent(Mbr parent) {
    this.parent = parent;
  }
  
  void removeChild(Mbr child) {
    children.remove(child);
  }
  
<<<<<<< HEAD
  protected int numChildren() {
    if (isLeaf) {
      return points.size();
    }
    int childrenCount = 0;
    for (Mbr child : children) {
      childrenCount = childrenCount + child.numChildren();
    }
    return childrenCount;
  }
  
  public List<Point> getPoints() {
    if (isLeaf) {
      return points;
    }
    List<Point> allPoints = new ArrayList<Point>();
    for (Mbr child: children) {
      allPoints.addAll(child.getPoints());
    }
    return allPoints;
  }
  
  public boolean isLeaf() {
    return isLeaf;
  }
  public Mbr getParent() {
    return parent;
  }
  public List<Mbr> getChildren() {
    return children;
  }
=======
  public List<Mbr> getChildren() {
      return this.children;
  }
  
  //Is points actual leaves
  public List<Point> getLeaves() {
      return this.points;
  }
  
  @Override
  public String toString() {
      return "Mbr: " + " TL = " + this.tl + ", BR = " + this.br;
  }
  
>>>>>>> 79064838c863a5d95821aac08f0096885447eb8a
}
