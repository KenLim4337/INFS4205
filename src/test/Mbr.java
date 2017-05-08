package test;

import java.util.ArrayList;
import java.util.List;

public class Mbr {
    
    /**
     * Mbrs. Contains an Mbrs parents, children Mbrs/Nodes and generates its bounds.
     * 
     * @author Ken
     */
    
    //Incremented during creation
    private int id;
    
    //Null if root
	private Mbr parent;
	
	//Child Mbrs (Null for leaf Mbrs)
	private List<Mbr> children;
	
	//Nodes in Mbr
	private List<Point> leaves;
	
	//Top L and Bottom R of Mbr in order of Top L (Min X, Min Y), Bot R (Max X, Max Y), bound nodes will have an id of -1
	private List<Point> bounds;
	
	public Mbr(int id) {
	    this.id = id;
	    this.children = new ArrayList<Mbr>();
        this.leaves = new ArrayList<Point>();
        this.bounds = new ArrayList<Point>();
	}
	
	public Mbr getParent() {
		return this.parent;
	}
	
	public void setParent(Mbr newParent) {
		this.parent = newParent;
	}
	
	public List<Mbr> getChildren() {
		return this.children;
	}
	
	public void setChildren(List<Mbr> children) {
	    this.children = children;
	}
	
	public List<Point> getLeaves() {
		return this.leaves;
	}
	
	public void setLeaves(List<Point> leaves) {
	    this.leaves = leaves;
	}
	
	public List<Point> getBounds() {
	    return this.bounds;
	}
	
	
	//Generates the bounds for the Mbr
	public void generateBounds() {
	    int maxX = Integer.MIN_VALUE;
	    int maxY = Integer.MIN_VALUE;
	    int minX = Integer.MAX_VALUE;
	    int minY = Integer.MAX_VALUE;
	    
	    for(Point x : leaves) {
	        if(x.getX() > maxX) {
	            maxX = x.getX();
	        } 
	        
	        if (x.getX() < minX) {
	            minX = x.getX();
	        } 

	        if (x.getY() > maxY) {
                maxY = x.getY();
	        } 

	        if (x.getY() < minY) {
	            minY = x.getY();
	        }
	    }
	    
	    this.bounds.add(new Point(-1, minX, minY));
	    this.bounds.add(new Point(-1, maxX, maxY));
	}
	
	@Override
    public String toString(){
        String temp =  "Mbr " + id + " With Bounds: \n";
        
        for (Point x :bounds) {
            temp +=  ">" + x.toString().substring(9) + "\n";
        }
        
        return temp;
    }
	
}
