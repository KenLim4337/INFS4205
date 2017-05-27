package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import geography.Point;

public class PointsTester {
  
  @Test
  public void test() {
    Point p1 = new Point(1, 2, 1);
    Point p2 = new Point(2, 2, 2);
    Point p3 = new Point(-2, 4, 3);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3);
    Collections.sort(pointsTest);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p3); pointsCorrect.add(p1); pointsCorrect.add(p2);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
  @Test
  public void testHorizontal() {
    Point p1 = new Point(1, 2, 1);
    Point p2 = new Point(2, 2, 2);
    Point p3 = new Point(-2, 4, 3);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3);
    pointsTest.sort(Point.compareX);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p3); pointsCorrect.add(p1); pointsCorrect.add(p2);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
  @Test
  public void secondTest() {
    Point p1 = new Point(20, 15, 1);
    Point p2 = new Point(10, 0, 2);
    Point p3 = new Point(0, 0, 3);
    Point p4 = new Point(21, -2, 4);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3); pointsTest.add(p4);
    Collections.sort(pointsTest);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p3); pointsCorrect.add(p2); pointsCorrect.add(p1); pointsCorrect.add(p4);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
  @Test
  public void secondHorizontalTest() {
    Point p1 = new Point(20, 15, 1);
    Point p2 = new Point(10, 0, 2);
    Point p3 = new Point(0, 0, 3);
    Point p4 = new Point(21, -2, 4);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3); pointsTest.add(p4);
    pointsTest.sort(Point.compareX);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p3); pointsCorrect.add(p2); pointsCorrect.add(p1); pointsCorrect.add(p4);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
  @Test
  public void verticalTest() {
    Point p1 = new Point(1, 2, 1);
    Point p2 = new Point(2, 1, 2);
    Point p3 = new Point(-2, 4, 3);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3);
    pointsTest.sort(Point.compareY);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p2); pointsCorrect.add(p1); pointsCorrect.add(p3);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
  @Test
  public void secondVerticalTest() {
    Point p1 = new Point(20, 15, 1);
    Point p2 = new Point(10, 1, 2);
    Point p3 = new Point(0, 0, 3);
    Point p4 = new Point(21, -2, 4);
    List<Point> pointsTest = new ArrayList<Point>();
    pointsTest.add(p1); pointsTest.add(p2); pointsTest.add(p3); pointsTest.add(p4);
    pointsTest.sort(Point.compareY);
    List<Point> pointsCorrect = new ArrayList<Point>();
    pointsCorrect.add(p4); pointsCorrect.add(p3); pointsCorrect.add(p2); pointsCorrect.add(p1);
    Assert.assertEquals(pointsTest, pointsCorrect);
  }
  
}
