package geography;

public class Point {
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
  
}
