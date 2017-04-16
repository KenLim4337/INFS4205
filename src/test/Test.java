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
		
		//Input Reading test code
		Map<Integer, int[]> testRead = readFile(args[0]);
		
		System.out.println(inputToString(testRead));
		
		//Query Reading test code
		List<List<Integer>> testQueries = readQueries(args[1]);
		
		System.out.println(queriesToString(testQueries));
	}
    
	
	public Rectangle createRectangle(Point x, Point y) {
	    Rectangle r = new Rectangle(x);
	    r.add(y);
	    return r;
    }
    
	/*File reading*/
    public static Map<Integer, int[]> readFile(String filename) {
    	
    	//Variables
    	Integer size = 0;
    	Map<Integer, int[]> result = new HashMap<Integer, int[]>();
    	
    	//Try/catch block for I/O Exception
    	try {
    		File file = new File (filename);
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		System.out.println("\nFile loaded");
    		
    		String line=null;
    	    
    		if((line=reader.readLine()) != null) {
    			//Check if first line is an integer, return exception if not
    			try {
    				size = Integer.parseInt(line);
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    				reader.close();
    	    		return null;
    			}
    	    }
    		
    		while((line=reader.readLine()) != null) {
    	    	List<String> temp = Arrays.asList(line.split(" "));
    	    	int tempID = 0;
    	    	int tempX = 0;
    	    	int tempY = 0;
    	    	
    	    	//Check each row only has 3 columns
    	    	if (temp.size() != 3) {
    	    		System.out.print("File Format Error: Each row must have 3 columns");
    	    		reader.close();
    	    		return null;
    	    	}
    	    	
    	    	
    	    	String[] tempArray = temp.get(0).split("_");
    	    	try {
        	    	tempID = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(1).split("_");
    	    	try {
        	    	tempX = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(2).split("_");
    	    	try {
        	    	tempY = Integer.parseInt(tempArray[1]);
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	int[] tempCoords = {tempX, tempY};
    	    	
    	    	result.put(tempID, tempCoords);
    	    	
    	    }
    	    
    	    reader.close();
    	    
    	} catch (IOException e) {
    		System.out.print("I/O Exception: " + e.getMessage());
    	} finally {
    		//Read fails if the number of points provided does not match the actual number of points in the input file
    		if(result.size() != size) {
	    		System.out.print("File format error: Rows do not match provided number of points.");
	    		return null;
	    	}else {
	    		System.out.println("\nFile reading complete");
	    	}
    	}
    	
    	return result;
    }
    
    
    //Converts an input file into a readable string
    public static String inputToString(Map<Integer, int[]> input) {
    	String result = "";
    	
    	Iterator it = input.entrySet().iterator();
		
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry) it.next();
			int[] tempArray = (int[])pair.getValue();
			String build = "X = " + tempArray[0] + ", Y = " + tempArray[1];
			result += ("ID > " + pair.getKey() + " : " + build + "\n");
			it.remove();
		}	
    	return result;
    }
    
    
    
    public static List<List<Integer>> readQueries(String filename) {
    	List<List<Integer>> result = new ArrayList<List<Integer>>();
    	
    	try {
    		File file = new File (filename);
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		System.out.println("\nFile loaded");
    		
    		String line=null;
    		
    		while((line=reader.readLine()) != null) {
    	    	List<String> temp = Arrays.asList(line.split(" "));

    	    	List<Integer> built = new ArrayList<Integer>();
    	    	
    	    	if (temp.size() != 4) {
    	    		System.out.print("File format error");
    	    		reader.close();
    	    		return null;
    	    	}
    	    	
    	    	String[] tempArray = temp.get(0).split("_");
    	    	try {
        	    	built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(1).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(2).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(3).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("Format Error: " + e.getMessage());
    			}
    	    	
    	    	result.add(built);
    	    	
    	    }
    	    
    	    reader.close();
    	    
    	} catch (IOException e) {
    		System.out.print("I/O Exception: " + e.getMessage());
    	} finally {
    		System.out.println("\nFile reading complete");
    	}
    	
    	return result;
    }
    
    //Converts a queries file into a readable string
    public static String queriesToString(List<List<Integer>> input) {
    	String result = "";
    	
		for(List<Integer> x : input) {
			String builder = "";
			
			builder = "X = " + x.get(0) + ", X' = " + x.get(1) + ", Y = " + x.get(2) + ", Y' = " + x.get(3) + "\n";
			
			result+= builder;
		}
		
    	return result;
    }
    
    
}
