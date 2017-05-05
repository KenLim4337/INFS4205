package test;

import java.io.*;
import java.util.*;

public class Test {
    
    public static List<Mbr> mbrList;
    public static List<Node> inputPoints;
    
	public static void main(String [] args) {
		System.out.println("Hello, world!");
		
		//Input Reading test code
		List<Node> inputPoints = readFile(args[0]);
		
		System.out.println(inputToString(inputPoints));
		
		//Query Reading test code
		List<List<Integer>> testQueries = readQueries(args[1]);
		
		System.out.println(queriesToString(testQueries));
		
		
		//Basic MBR test code
		Mbr test = new Mbr(1);
		
		test.setLeaves(inputPoints);
		test.generateBounds();
		
		System.out.println(test.toString());
		
	}
    
	
    /**
     * Reads in an appropriately formatted text file and outputs a map of points with their respective IDs.
     * Also checks that the input file is formatted appropriately in the form:
     * 
     * n
     * id_1 x_1 y_1
     * ...
     * id_n x_n y_n
     * 
     * @param filename Directory of the input file
     * @return List of nodes
     * @author Ken
     */
    public static List<Node> readFile(String filename) {
    	
    	//Variables
    	Integer size = 0;
    	List<Node> result = new ArrayList<Node>();
    	
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
    	    	int tempId = 0;
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
        	    	tempId = Integer.parseInt(tempArray[1]);
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
    	    	
    	    	result.add(new Node(tempId, tempX, tempY));
    	    	
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
    
    
    /**
     * Converts data from an input file into a readable string
     * 
     * @param input Map with an integer as key and an array of integers as values (output from readFile)
     * @return Map from readFile in string form.
     * @author Ken
     */
    public static String inputToString(List<Node> input) {
    	String result = "Input set: " + "\n";

    	for(Node x : input) {
    	    result += x.toString() + "\n";
    	}
    	
    	return result;
    }
    
    
    
    /**
     * Reads in an appropriately formatted text file and outputs a list of queries.
     * Also checks that the file follows the following format:
     * 
     * x_1 x'_1 y_1 y'_1
     * 
     * @param filename Directory of the input query file
     * @return A list of queries in the form of Lists of integers
     * @author Ken
     */
    public static List<List<Integer>> readQueries(String filename) {
    	//Variables
    	List<List<Integer>> result = new ArrayList<List<Integer>>();

    	//Try/catch block for I/O Exception
    	try {
    		File file = new File (filename);
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		System.out.println("\nFile loaded");
    		
    		String line=null;
    		
    		while((line=reader.readLine()) != null) {
    	    	List<String> temp = Arrays.asList(line.split(" "));

    	    	List<Integer> built = new ArrayList<Integer>();
    	    	
    	    	//Check each row only has 4 columns
    	    	if (temp.size() != 4) {
    	    		System.out.print("File Format Error: Each row must have 4 columns");
    	    		reader.close();
    	    		return null;
    	    	}
    	    	
    	    	String[] tempArray = temp.get(0).split("_");
    	    	try {
        	    	built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(1).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(2).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
    			}
    	    	
    	    	tempArray = temp.get(3).split("_");
    	    	try {
    	    		built.add(Integer.parseInt(tempArray[1]));
    			} catch (NumberFormatException e) {
    				System.out.print("File Format Error: " + e.getMessage());
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
    
    
    /**
     * Converts data from an queries file into a readable string
     * 
     * @param input A List of Lists of integers (output from readQueries)
     * @return List from readQueries in string form.
     * @author Ken
     */
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
