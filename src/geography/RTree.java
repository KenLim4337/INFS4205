package geography;

public class RTree {
  private Mbr root;
  
  public RTree() {
    Point tl = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE, 0);
    Point br = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
    
    
  }
  public void insert(Point p) {
    root.insert(p);
  }
  
  void setRoot(Mbr root) {
    this.root = root;
  }
  
}
