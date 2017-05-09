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
	
	public void setX(int x) {
	    this.xCoord = x;
	}
	
	public int getY() {
        return this.yCoord;
    }
	
	public void setY(int y) {
        this.yCoord = y;
    }
	
	public int getID() {
		return this.id;
	}
	
	
	//Checks if a rectangle contains this point
	public boolean isIn(Mbr rect) {
	    Point tr = rect.getTR();
	    Point bl = rect.getBL();
	    
	    if ((this.xCoord <= tr.getX() && this.xCoord >= bl.getX())
	            && this.yCoord <= bl.getY() && this.yCoord >= tr.getY()) {
	        return true;
	    }
	    //else
	    return false;
	}
	
	@Override
	public String toString(){
	    return "Node " + id + "> X: " + xCoord + ", Y: " + yCoord;
	}
}
