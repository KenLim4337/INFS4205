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

  public void insert(Point p) {
    if (isLeaf) {
      points.add(p);
      if (points.size() > MAX_CHILDREN) {
        handleOverflow();
      }
    } else {
      // TODO: choose subtree, in intelligent way, not randomly
      Random random = new Random(); // TODO: pick a better method
      Mbr nextLevel = children.get(random.nextInt(children.size()));
      nextLevel.insert(p);
    }
    calculateCorners();
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
    // TODO split this mbr into u and u' in a proper way
    // current method is exactly in half, along x axis
    
    // If this is a leaf node, split it into two new leaf nodes and make those
    // children of the parent.
    // If this is an internal node, split into two internal nodes and make those children.
    Mbr mbr1 = new Mbr(parent, isLeaf, tree);
    Mbr mbr2 = new Mbr(parent, isLeaf, tree);
    if (isLeaf) {
      Collections.sort(points);
      List<Point> points1 = points.subList(0, MAX_CHILDREN / 2);
      List<Point> points2 = points.subList(MAX_CHILDREN / 2, points.size());
      mbr1.givePoints(points1);
      mbr2.givePoints(points2);
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
  
  void giveChildren(List<Mbr> mbrs) {
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
    return tl;
  }

  public Point getBr() {
    return br;
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
  
  public List<Mbr> getChildren() {
      return this.children;
  }
  
  //Is points actual leaves
  public List<Point> getLeaves() {
      return this.points;
  }
  
}
