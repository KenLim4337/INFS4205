package test;

import java.util.ArrayList;
import java.util.List;

public class Mbr {
    
    /**
     * Mbrs. Contains an Mbrs parents, children Mbrs/Nodes and generates its bounds.
     * 
     * @author Ken
     */
    
    //Incremented during creation, range queries will have an id of -2
    private int id;
    
    //Null if root
	private Mbr parent;
	
	//Child Mbrs (Size = 0 for leaf Mbrs)
	private List<Mbr> children;
	
	//Nodes in Mbr
	private List<Point> leaves;
	
	//Top right and bottom left bounds
	private Point trBound;
	
	private Point blBound;
	
	
	
	public Mbr(int id) {
	    this.id = id;
	    this.children = new ArrayList<Mbr>();
        this.leaves = new ArrayList<Point>();
        initBounds();
        
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
        updateBounds(1);
	}
	
	public void addLeaf(Point leaf) {
	    this.leaves.add(leaf);
	    updateBounds(0);
	}
	
	public Point getTR() {
	    return this.trBound;
	}
	
	public Point getBL() {
	    return this.blBound;
	}
	
	//Initializes initial bounds
	public void initBounds() {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
	    
	    this.trBound = new Point(-1, maxX, minY);
	    this.blBound = new Point(-1, minX, maxY);
	}
	
	//Updates the bounds for an Mbr, parameter to check if called from add or set leaves
	public void updateBounds(int z) {
	    
	    //Resets bounds first if a new set of points is used
	    if(z == 1) {
	        initBounds();
	    }
	    
	    for(Point x : leaves) {
	        if(x.getX() > trBound.getX()) {
	            trBound.setX(x.getX());   
	        }
	        
	        if (x.getX() < blBound.getX()) {
	            blBound.setX(x.getX());
	        } 

	        if (x.getY() > blBound.getY()) {
                blBound.setY(x.getY());
	        }

	        if (x.getY() < trBound.getY()) {
	            trBound.setY(x.getY());
	        }
	    }
	}
	
	//Checks if an Mbr intersects another Mbr
	public boolean checkIntersect() {
	    
	    
	    
	    
	    return false;
	}
	
	
	@Override
    public String toString(){
        String temp =  "Mbr " + id + " With Bounds: \n";
        
        List<Point> bounds = new ArrayList<Point>();
        
        bounds.add(trBound);
        bounds.add(blBound);
        
        for (Point x :bounds) {
            temp +=  ">" + x.toString().substring(9) + "\n";
        }
        
        return temp;
    }
	
}
