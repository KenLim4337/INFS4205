package test;

import java.awt.Rectangle;
import java.awt.Point;
import java.io.*;
import java.util.*;

public class Test {
	public static void main(String [] args) {
		System.out.println("Hello, world!");
		Point p1 = new Point(1, 1);
		Point p2 = new Point(0, 2);
		Rectangle r = new Rectangle(p1);
		r.add(p2);
		System.out.println(r.getBounds());
		
		//Reading test code
		readFile("");
	}
    
	
	public Rectangle createRectangle(Point x, Point y) {
	    Rectangle r = new Rectangle(x);
	    r.add(y);
	    return r;
    }
    
	/*File reading*/
    public static Map<Integer, int[]> readFile(String filename) {
    	
    	Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    	
    	try {
    		File file = new File (filename);
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		System.out.println("\nFile loaded!");
    		
    		String line=null;
    	    while((line=reader.readLine()) != null) {
    	    	System.out.println(line);
    	    }
    	    
    	    
    	} catch (IOException e) {
    		System.out.print("I/O Exception: " + e.getMessage());
    	} 
    	
    	
    	
    	
    	return result;
    }
}
