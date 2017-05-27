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
   * If point p is greater than or equal to this
   * point in both the X and Y dimensions,
   * p dominates this point.
   */
  public boolean dominates(Point p) {
    if (p.getX() >= this.getX()&&
        p.getY() >= this.getY()) {
      return true;
    }
    return false;
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
  
  public static Comparator<Point> compareX =
      (a, b) -> a.getX() < b.getX() ? -1 : a.getX() == b.getX() ? 0 : 1;
      
  public static Comparator<Point> compareY = 
      (a, b) -> a.getY() < b.getY() ? -1 : a.getY() == b.getY() ? 0 : 1;
}
