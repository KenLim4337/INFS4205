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
	private List<Node> leaves;
	
	//Top L and Bottom R of Mbr in order of Top L (Min X, Min Y), Bot R (Max X, Max Y), bound nodes will have an id of -1
	private List<Node> bounds;
	
	public Mbr(int id) {
	    this.id = id;
	    this.children = new ArrayList<Mbr>();
        this.leaves = new ArrayList<Node>();
        this.bounds = new ArrayList<Node>();
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
	
	public List<Node> getLeaves() {
		return this.leaves;
	}
	
	public void setLeaves(List<Node> leaves) {
	    this.leaves = leaves;
	}
	
	public List<Node> getBounds() {
	    return this.bounds;
	}
	
	
	//Generates the bounds for the Mbr
	public void generateBounds() {
	    int maxX = Integer.MIN_VALUE;
	    int maxY = Integer.MIN_VALUE;
	    int minX = Integer.MAX_VALUE;
	    int minY = Integer.MAX_VALUE;
	    
	    for(Node x : leaves) {
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
	    
	    this.bounds.add(new Node(-1, minX, minY));
	    this.bounds.add(new Node(-1, maxX, maxY));
	}
	
	@Override
    public String toString(){
        String temp =  "Mbr " + id + " With Bounds: \n";
        
        for (Node x :bounds) {
            temp +=  ">" + x.toString().substring(9) + "\n";
        }
        
        return temp;
    }
	
}
