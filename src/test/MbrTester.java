package test;

import org.junit.Assert;
import org.junit.Test;

import geography.Point;
import geography.Mbr;
import geography.RTree;

public class MbrTester {
  
  @Test
  public void testOnePoint() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
    Assert.assertEquals(tree.numPoints(), 1);
  }
  
  @Test
  public void testThreePoints() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
    tree.insert(new Point(4, 2, 2));
    tree.insert(new Point(3, 5, 3));
    Assert.assertEquals(tree.numPoints(), 3);
  }
  
  @Test
  public void testFourPoints() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
    tree.insert(new Point(4, 2, 2));
    tree.insert(new Point(3, 5, 3));
    tree.insert(new Point(9, -5, 4));
    Assert.assertEquals(tree.numPoints(), 4);
  }
  
  @Test
  public void testSixPoints() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
    tree.insert(new Point(4, 2, 2));
    tree.insert(new Point(3, 5, 3));
    tree.insert(new Point(9, -5, 4));
    tree.insert(new Point(5, 12, 5));
    tree.insert(new Point(0, -8, 6));
    Assert.assertEquals(tree.numPoints(), 6);
  }
  
  @Test
  public void testTenPoints() {
    RTree tree = new RTree();
    tree.insert(new Point(1, 2, 1));
    tree.insert(new Point(4, 2, 2));
    tree.insert(new Point(3, 5, 3));
    tree.insert(new Point(9, -5, 4));
    tree.insert(new Point(5, 12, 5));
    tree.insert(new Point(0, -8, 6));
    tree.insert(new Point(12, 2, 7));
    tree.insert(new Point(-1, 0, 8));
    tree.insert(new Point(0, 0, 9));
    tree.insert(new Point(1, -8, 10));
    Assert.assertEquals(tree.numPoints(), 10);
  }
}
