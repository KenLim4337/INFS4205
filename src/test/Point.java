package test;

public class Point {
    
    /**
     * Holds an X and Y coordinate to represent points, also used to represent bounds of an Mbr
     * 
     * @author Ken
     */
    
	private int id;
	private int xCoord;
	private int yCoord;
	
	public Point (int idnum, int x, int y) {
		this.id = idnum;
		this.xCoord = x;
		this.yCoord = y;
	}
	
	public int getX() {
		return this.xCoord;
	}
	
	public int getY() {
        return this.yCoord;
    }
	
	public int getID() {
		return this.id;
	}
	
	@Override
	public String toString(){
	    return "Node " + id + "> X: " + xCoord + ", Y: " + yCoord;
	}
}
