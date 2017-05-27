package test;

import java.util.ArrayList;
import java.util.List;

public class MbrTest {
    
    /**
     * Mbrs. Contains an Mbrs parents, children Mbrs/Nodes and generates its bounds.
     * 
     * @author Ken
     */
    
    //Incremented during creation, range queries will have an id of -2
    private int id;
    
    //Null if root
	private MbrTest parent;
	
	//Child Mbrs (Size = 0 for leaf Mbrs)
	private List<MbrTest> children;
	
	//Nodes in Mbr
	private List<PointTest> leaves;
	
	//Top right and bottom left bounds
	private PointTest trBound;
	
	private PointTest blBound;
	
	
	
	public MbrTest(int id) {
	    this.id = id;
	    this.children = new ArrayList<MbrTest>();
        this.leaves = new ArrayList<PointTest>();
        initBounds();
        
	}
	
	public MbrTest getParent() {
		return this.parent;
	}
	
	public void setParent(MbrTest newParent) {
		this.parent = newParent;
	}
	
	public List<MbrTest> getChildren() {
		return this.children;
	}
	
	public void setChildren(List<MbrTest> children) {
	    this.children = children;
	}
	
	public List<PointTest> getLeaves() {
		return this.leaves;
	}
	
	public void setLeaves(List<PointTest> leaves) {
	    this.leaves = leaves;
        updateBounds(1);
	}
	
	public void addLeaf(PointTest leaf) {
	    this.leaves.add(leaf);
	    updateBounds(0);
	}
	
	public PointTest getTR() {
	    return this.trBound;
	}
	
	public PointTest getBL() {
	    return this.blBound;
	}
	
	//Initializes initial bounds
	public void initBounds() {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
	    
	    this.trBound = new PointTest(-1, maxX, minY);
	    this.blBound = new PointTest(-1, minX, maxY);
	}
	
	//Updates the bounds for an Mbr, parameter to check if called from add or set leaves
	public void updateBounds(int z) {
	    
	    //Resets bounds first if a new set of points is used
	    if(z == 1) {
	        initBounds();
	    }
	    
	    for(PointTest x : leaves) {
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
        
            temp +=  ">" + trBound.toString().substring(9) + "\n";
            temp +=  ">" + blBound.toString().substring(9) + "\n";
            
        return temp;
    }
	
}
