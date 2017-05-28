package geography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
  //512b disc block size (hard drives) /25b per mbr = 20
  private static int MAX_CHILDREN = 19;
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
    if (this.isLeaf) {
      this.points.add(p);
      if (this.points.size() > MAX_CHILDREN) {
        handleOverflow();
      }
    } else {
      
      Mbr bestChild = null;
      int bestPerim = Integer.MAX_VALUE;
      
      for(Mbr child: this.children) {
          //Get perimiter after adding p
          if(child.calcPerim(p) < bestPerim) {
              bestPerim = child.calcPerim(p);
              bestChild = child;
          }
          
      }
      
      bestChild.insert(p);
      
      /*
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
      */
    }
    calculateCorners();
  }
  
  //Calculates new perimiter given a new point to insert
  public Integer calcPerim(Point x) {
      int minX = this.tl.getX();
      int maxX = this.br.getX();
      int minY = this.tl.getY();
      int maxY = this.br.getY();
      
      
      if (x.getX() > maxX) {
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
      
      return (((maxY-minY)*2) + ((maxX-minX)*2));
      
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
    
    List<Mbr> split = new ArrayList<Mbr>();  
      
    //Split this in 2
    if(this.isLeaf) {
        split = this.leafSplit();
    } else {
        //innersplit
        split = this.innerSplit();
    }
      
    if(this.parent == null) {
        //Create new parent for this
        Mbr newParent = new Mbr(null, false, tree);
        //Make new parent the parent
        newParent.giveChildren(split);
        //Make the parent of splits this
        for (Mbr x: split) {
            x.setParent(newParent);
        }
        //Update root
        tree.setRoot(newParent);
    } else {
        Mbr parent = this.getParent();
        parent.getChildren().remove(this);
        for (Mbr x: split) {
            parent.addChild(x);
        }
        if (parent.getChildren().size() > MAX_CHILDREN) {
            parent.handleOverflow();
        }
    }
    
    /*  
    
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
    */
  }
  
  //Splits this leaf Mbr in 2
  private List<Mbr> leafSplit() {
      //Check X axis split and Y axis split, then choose best split
      int n = this.getLeaves().size();
      int i = 8;
      int i1 = 12;
      int bestPerim = Integer.MAX_VALUE;
      List<Comparator<Point>> comps = getPtComp();
      
      //Iterate through list of comparators?
      List<Point> leaf = this.getLeaves();
      
      List<Point> result1 = new ArrayList<Point>();
      List<Point> result2 = new ArrayList<Point>();
      
      for (Comparator<Point> comp : comps) {
          
          //Sort leaves based on comparator
          leaf.sort(comp);
          
          List<Point> temp1 = leaf.subList(0, i);
          
          List<Point> temp2 = leaf.subList(i1, n);
          
          List<Integer> sides1 = findSides(temp1);
                  
          List<Integer> sides2 = findSides(temp2);      
          
          int tempPerim = ((sides1.get(0)*2)+(sides1.get(1)*2)) + ((sides2.get(0)*2)+(sides2.get(1)*2));
          
          
          //Check numbers
          if (tempPerim < bestPerim) {
              result1 = temp1;
              result2 = temp2;
          } else if (tempPerim == bestPerim) {
              //if equal compare area
              
              List<Integer> lSides1 = findSides(result1);
              
              List<Integer> lSides2 = findSides(result2);     
              
              int lastArea = (lSides1.get(0) * lSides1.get(1)) + (lSides2.get(0) * lSides2.get(1));
              
              int thisArea = (sides1.get(0) * sides1.get(1)) + (sides2.get(0) * sides2.get(1));
              
              if (thisArea < lastArea) {
                  result1 = temp1;
                  result2 = temp2;
              } else {
                  //else do nothing, already have best split
              }
              
          } else {
              //else do nothing, already have the best split
          }
      }
      
      Mbr m1 = new Mbr (this, true, this.tree);
      m1.givePoints(result1);
      
      Mbr m2 = new Mbr (this, true, this.tree);
      m2.givePoints(result2);
      
      
      List<Mbr> res = new ArrayList<Mbr>();
      res.add(m1);
      res.add(m2);
      
      return res;
      
      /*
      leaf.sort(Point.compareX);
      
      List<Point> sx1 = leaf.subList(0, i);
      
      List<Point> sx2 = leaf.subList(i1, n);
      
      List<Integer> xSides1 = findSides(sx1);
              
      List<Integer> xSides2 = findSides(sx2);      
      
      bestPerim = ((xSides1.get(0)*2)+(xSides1.get(1)*2)) + ((xSides2.get(0)*2)+(xSides2.get(1)*2));
      
      leaf.sort(Point.compareY);
      
      List<Point> sy1 = leaf.subList(0, i);
      
      List<Point> sy2 = leaf.subList(i1, n);
      
      List<Integer> ySides1 = findSides(sy1);
      
      List<Integer> ySides2 = findSides(sy2);
      
      int yPerim = ((ySides1.get(0)*2)+(ySides1.get(1)*2)) + ((ySides2.get(0)*2)+(ySides2.get(1)*2));
      
      if (yPerim < bestPerim) {
          bestPerim = yPerim;
          //return Y mbrs
          result1 = sy1;
          result2 = sy2;
      } else if (yPerim == bestPerim) {
          //if equal, compare area
          int xArea = (xSides1.get(0) * xSides1.get(1)) + (xSides2.get(0) * xSides2.get(1));
          int yArea = (ySides1.get(0) * ySides1.get(1)) + (ySides2.get(0) * ySides2.get(1));
          
          if (xArea < yArea) {
              result1 = sx1;
              result2 = sx2;
          } else {
              result1 = sy1;
              result2 = sy2;
          }
      } else {
          //return X mbrs
          result1 = sx1;
          result2 = sx2;
      }
      
      Mbr m1 = new Mbr (this, true, this.tree);
      m1.givePoints(result1);
      
      Mbr m2 = new Mbr (this, true, this.tree);
      m2.givePoints(result2);
      
      
      List<Mbr> res = new ArrayList<Mbr>();
      res.add(m1);
      res.add(m2);
      
      return res;
      */
  }
  
  private List<Comparator<Point>> getPtComp() {
      List<Comparator<Point>> result = new ArrayList<Comparator<Point>>();
      result.add(Point.compareX);
      result.add(Point.compareY);
      
      return result;
  }
  
  
  //Splits inner node in 2
  private List<Mbr> innerSplit() {
      int n = this.getChildren().size();
      int i = 8;
      int i1 = 12;
      int bestPerim = Integer.MAX_VALUE;
      List<Comparator<Mbr>> comps = getMbrComp();
      
      List<Mbr> children = this.getChildren();
      
      List<Mbr> result1 = new ArrayList<Mbr>();
      List<Mbr> result2 = new ArrayList<Mbr>();
      
      for (Comparator<Mbr> compy: comps) {
          children.sort(compy);
          
          List<Mbr> temp1 = children.subList(0, i);

          List<Mbr> temp2 = children.subList(i1, n);

          int p1 = findPerim(temp1);
          
          int p2 = findPerim(temp2); 
          
          int tempPerim = p1 + p2;
          
          if (tempPerim < bestPerim) {
              result1 = temp1;
              result2 = temp2;
          } else {
              //Already has the best
          }
          
      }
      
      Mbr m1 = new Mbr (this, true, this.tree);
      m1.giveChildren(result1);
      
      Mbr m2 = new Mbr (this, true, this.tree);
      m2.giveChildren(result2);
      
      
      List<Mbr> res = new ArrayList<Mbr>();
      res.add(m1);
      res.add(m2);
      
      return res;
  }
  
  private int findPerim(List<Mbr> x) {
      int minX = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxY = Integer.MIN_VALUE;
      
      for(Mbr y: x) {
          if(y.getBr().getX() > maxX) {
              maxX = y.getBr().getX();   
          }
          
          if (y.getTl().getX() < minX) {
              minX = y.getTl().getX();
          } 

          if (y.getBr().getY() > maxY) {
              maxY = y.getBr().getY();
          }

          if (y.getTl().getY() < minY) {
              minY = y.getTl().getY();
          }
      }
      
      return (Math.abs(maxX - minX) * 2) + (Math.abs(maxY - minY) * 2);
  }
  
  private List<Comparator<Mbr>> getMbrComp() {
      
      List<Comparator<Mbr>> comps = new ArrayList<Comparator<Mbr>>();
      comps.add(Mbr.compareX1);
      comps.add(Mbr.compareX2);
      comps.add(Mbr.compareY1);
      comps.add(Mbr.compareY2);
      
      return comps;
  }
  
  
  
  
  
  //Compare lowest X
  public static Comparator<Mbr> compareX1 = (a, b) -> a.getTl().getX() < b.getTl().getX() ? -1 : 
      a.getTl().getX() == b.getTl().getX() ? 0 : 1;
  
  //Compare highest X
  public static Comparator<Mbr> compareX2 = (a, b) -> a.getBr().getX() < b.getBr().getX() ? -1 : 
      a.getBr().getX() == b.getBr().getX() ? 0 : 1;
  
  //Compare lowest Y
  public static Comparator<Mbr> compareY1 = (a, b) -> a.getTl().getY() < b.getTl().getY() ? -1 : 
      a.getTl().getY() == b.getTl().getY() ? 0 : 1;
  
  //Compare highest Y
  public static Comparator<Mbr> compareY2 = (a, b) -> a.getBr().getY() < b.getBr().getY() ? -1 : 
      a.getBr().getY() == b.getBr().getY() ? 0 : 1;
  
  
  private List<Integer> findSides(List<Point> x) {
      int minX = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxY = Integer.MIN_VALUE;

      for (Point y: x) {
          if(y.getX() > maxX) {
              maxX = y.getX();   
          }
          
          if (y.getX() < minX) {
              minX = y.getX();
          } 

          if (y.getY() > maxY) {
              maxY = y.getY();
          }

          if (y.getY() < minY) {
              minY = y.getY();
          }
      }
      List<Integer> result = new ArrayList<Integer>();
      
      //Width
      result.add(Math.abs(maxX-minX));
      //Height
      result.add(Math.abs(maxY-minY));
      
      return result;
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
  
  public void setParent(Mbr parent) {
    this.parent = parent;
  }
  
  public void removeChild(Mbr child) {
    children.remove(child);
  }
  
  public void addChild(Mbr child) {
      this.children.add(child);
  }
  
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
  
  //Is points actual leaves
  public List<Point> getLeaves() {
      return this.points;
  }
  
  @Override
  public String toString() {
      return "Mbr: " + " TL = " + this.tl + ", BR = " + this.br;
  }
}
