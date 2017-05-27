package test;

import org.junit.Assert;
import org.junit.Test;

import geography.Point;
import geography.Mbr;
import geography.RTree;

public class MbrTester {
  
  @Test
  public void MbrTester() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
  }
  
}
