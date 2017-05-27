package geography;

import java.util.Comparator;

public class Point implements Comparable<Point> {
  private int x;
  private int y;
  private int id;
  
  public Point(int x, int y, int id) {
    this.x = x;
    this.y = y;
    this.id = id;
  }
  
  /**
   * If point p is greater than or equal to this point in both the X and Y
   * dimensions, p dominates this point.
   */
  public boolean dominates(Point p) {
    if (p.getX() >= this.getX() && p.getY() >= this.getY()) {
      return true;
    }
    return false;
  }
  
  // Mindist for point
  public double mindistPt(Point p) {
    double result = Math.sqrt(Math.pow((this.getX() - p.getX()), 2)
        + Math.pow((this.getY() - p.getY()), 2));
    return result;
  }
  
  // Mindist for Mbr
  public double mindistMbr(Mbr m) {
    Point tl = m.getTl();
    Point br = m.getBr();
    
    if (m.contains(this)) {
      return 0;
    } else if (this.getX() <= br.getX() && this.getX() >= tl.getX()) {
      if (this.getY() > tl.getY()) {
        return Math.abs((this.getY() - tl.getY()));
      } else {
        return Math.abs((br.getY() - this.getY()));
      }
    } else if (this.getY() <= tl.getY() && this.getY() >= br.getY()) {
      if (this.getX() > br.getX()) {
        return Math.abs((this.getX() - br.getX()));
      } else {
        return Math.abs((tl.getX() - this.getX()));
      }
    } else {
      // Diagonal, find closest point and mindist point
      
    }
    
    return 0.1;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getId() {
    return id;
  }
  
  @Override
  public int compareTo(Point p) {
    if (p == null) {
      throw new NullPointerException();
    }
    return this.getX() - p.getX();
  }
  
  public static Comparator<Point> compareX = (a, b) -> a.getX() < b.getX() ? -1 : a.getX() == b
      .getX() ? 0 : 1;
  
  public static Comparator<Point> compareY = (a, b) -> a.getY() < b.getY() ? -1 : a.getY() == b
      .getY() ? 0 : 1;
}
