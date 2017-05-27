package geography;

import java.util.List;
import java.util.ArrayList;

public class Node {
  
  private Node parent; // if root node, null
  private List<Node> children;
  private List<Mbr> mbrs;
  private static int MAX_CHILDREN = 3;
  
  public Node(Node parent) {
    this.parent = parent;
    children = new ArrayList<Node>();
  }
  
  public Node() {
    this.parent = null;
    children = new ArrayList<Node>();
  }
  
  public void insert(Point p) {
    if (children.size() == 0) {
      for (Mbr mbr : mbrs) {
        if (mbr.contains(p)) {
          
          break;
        }
      }
    }
  }
  
  public Node getParent() {
    return parent;
  }
  
  public List<Node> getChildren() {
    return children;
  }
}
