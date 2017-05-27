package geography;

public class RTree {
  private Mbr root;
  
  public RTree() {
    root = new Mbr(null, true, this);
  }
  public void insert(Point p) {
    root.insert(p);
  }
  
  protected void setRoot(Mbr root) {
    this.root = root;
  }
  
}
