package test;

import java.awt.*;

public class Test {
	public static void main(String [] args) {
		System.out.println("Hello, world!");
	}
    public Rectangle createRectangle(Point x, Point y) {
	    int width = (int) Math.abs(y.getX() - x.getX());
	    int height = (int) Math.abs(y.getY() - x.getY());
	    return new Rectangle(x, new Dimension(width, height));
    }
}
