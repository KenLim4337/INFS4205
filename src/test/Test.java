package test;

import java.awt.*;

public class Test {
	public static void main(String [] args) {
		System.out.println("Hello, world!");
		Point p1 = new Point(1, 1);
		Point p2 = new Point(0, 2);
		Rectangle r = new Rectangle(p1);
		r.add(p2);
		System.out.println(r.getBounds());
	}
    public Rectangle createRectangle(Point x, Point y) {
	    Rectangle r = new Rectangle(x);
	    r.add(y);
	    return r;
    }
}
