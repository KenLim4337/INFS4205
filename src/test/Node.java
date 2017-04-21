package test;

import java.awt.Point;

public class Node {

	private Point coords;
	private int id;
	
	public Node (Point coordinates, int idnum) {
		this.coords = coordinates;
		this.id = idnum;
	}
	
	public Point getCoords() {
		return this.coords;
	}
	
	public int getID() {
		return this.id;
	}
	
}
