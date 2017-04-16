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
		Map<Integer, int[]> testRead = readFile(args[0]);
		
		Iterator it = testRead.entrySet().iterator();
		
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next();
			int[] tempArray = (int[])pair.getValue();
			String build = "X = " + tempArray[0] + ", Y = " + tempArray[1];
			System.out.println("ID > " + pair.getKey() + " : " + build);
			it.remove();
		}
		
	}
    
	
	public Rectangle createRectangle(Point x, Point y) {
	    Rectangle r = new Rectangle(x);
	    r.add(y);
	    return r;
    }
    
	/*File reading*/
    public static Map<Integer, int[]> readFile(String filename) {
    	
    	Integer size = 0;
    	Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    	
    	try {
    		File file = new File (filename);
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		System.out.println("\nFile loaded");
    		
    		String line=null;
    	    
    		if((line=reader.readLine()) != null) {
    			try {
    				size = Integer.parseInt(line);
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    }
    		
    		while((line=reader.readLine()) != null) {
    	    	List<String> temp = Arrays.asList(line.split(" "));
    	    	int tempID = 0;
    	    	int tempX = 0;
    	    	int tempY = 0;
    	    	
    	    	System.out.println(line);
    	    	
    	    	if (temp.size() != 3) {
    	    		System.out.print("File format error");
    	    		reader.close();
    	    		return null;
    	    	}
    	    	
    	    	
    	    	String[] tempArray = temp.get(0).split("_");
    	    	try {
        	    	tempID = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(1).split("_");
    	    	try {
        	    	tempX = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(2).split("_");
    	    	try {
        	    	tempY = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	int[] tempCoords = {tempX, tempY};
    	    	
    	    	result.put(tempID, tempCoords);
    	    	
    	    }
    	    
    	    reader.close();
    	    
    	} catch (IOException e) {
    		System.out.print("I/O Exception: " + e.getMessage());
    	} finally {
    		if(result.size() != size) {
	    		System.out.print("File format error");
	    		return null;
	    	}
    		System.out.println("\nFile reading complete");
    	}
    	
    	return result;
    }
}
