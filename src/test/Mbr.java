package test;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class Mbr {
	private Mbr parent;
	private List<Mbr> children;
	private List<Point> leaves;
	private Rectangle rect;
	
	public Mbr() {
		
	}
	
	public Mbr getParent() {
		return parent;
	}
	
	public void setParent(Mbr newParent) {
		parent = newParent;
	}
	
	public List<Mbr> getChildren() {
		return children;
	}
	
	public List<Point> getLeaves() {
		return leaves;
	}
	
	public Rectangle getRectangle() {
		return rect;
	}
}
