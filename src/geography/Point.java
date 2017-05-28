package geography;

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
  
  //Mindist for point
  public double mindistPt(Point p) {
      double result = Math.sqrt(Math.pow((this.getX()-p.getX()),2) + Math.pow((this.getY()-p.getY()),2));
      return result;
  }

  //Mindist for Mbr
  public double mindistMbr(Mbr m) {
      Point tl = m.getTl();
      Point br = m.getBr();
      Point tr = new Point (0, br.getX(), tl.getY());
      Point bl = new Point (0, tl.getX(), br.getY());
      
      //Check if bottom is larger or top
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
          //Diagonal, find closest point and mindist to point
          if (this.dominates(tl)) {
              //Top left
              return this.mindistPt(tl);
          } else if (br.dominates(this)) {
              //Bot right 
              return this.mindistPt(br);
          } else if (this.getX() > br.getX()) {
              //Top Right
              return this.mindistPt(tr);
          } else {
              //Bottom Left
              return this.mindistPt(bl);
          }
      }
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
  
  @Override
  public String toString() {
      return "ID: " + this.id + ", X: " + this.x + ", Y: " + this.y;
  }
  
}
