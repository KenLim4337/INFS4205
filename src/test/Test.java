package test;

import java.io.*;
import java.util.*;

public class Test {
    
    public static List<Mbr> mbrList;
    public static List<Point> inputPoints;
    
	public static void main(String [] args) {
		System.out.println("Hello, world!");
		
		//Input Reading test code
		List<Point> inputPoints = readFile(args[0]);
		
		//System.out.println(inputToString(inputPoints));
		
        System.out.println(sequentialScan(inputPoints) + " millis");
		
		//Query Reading test code
		List<List<Integer>> testQueries = readRangeQueries(args[1]);
		System.out.println(rangeQueryToString(testQueries));
        List<List<Integer>> testNNQueries = readNNQueries(args[2]);
		System.out.println(nnQueryToString(testNNQueries));
		
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
    public static List<Point> readFile(String filename) {
    	
    	//Variables
    	Integer size = 0;
    	List<Point> result = new ArrayList<Point>();
    	
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
    	    	
    	    	result.add(new Point(tempId, tempX, tempY));
    	    	
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
	    		System.out.println("\nInput file reading complete");
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
    public static String inputToString(List<Point> input) {
    	String result = "Input set: " + "\n";

    	for(Point x : input) {
    	    result += x.toString() + "\n";
    	}
    	
    	return result;
    }
    
    
    /**
     * Reads in an appropriately formatted text file and outputs a list of range queries.
     * Also checks that the file follows the following format:
     * 
     * x_1 x'_1 y_1 y'_1
     * 
     * @param filename Directory of the input query file
     * @return List<List<Integer>> A list of queries in the form of Lists of integers
     * @author Ken
     */
    public static List<List<Integer>> readRangeQueries(String filename) {
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
    		System.out.println("\nRange query file reading complete");
    	}
    	
    	return result;
    }
   
    
    /**
     * Converts data from a range queries file into a readable string
     * 
     * @param input A List of Lists of integers (output from readRangeQueries)
     * @return List from readRangeQueries in string form.
     * @author Ken
     */
    public static String rangeQueryToString(List<List<Integer>> input) {
        String result = "";
        
        for(List<Integer> x : input) {
            String builder = "";
            
            builder = "X = " + x.get(0) + ", X' = " + x.get(1) + ", Y = " + x.get(2) + ", Y' = " + x.get(3) + "\n";
            
            result+= builder;
        }
        
        return result;
    }
    
    
    /**
     * Reads in an appropriately formatted text file and outputs a list of NN queries.
     * Also checks that the file follows the following format:
     * 
     * x_1 y_1
     * ...
     * 
     * @param filename Directory of the input query file
     * @return List<List<Integer>> A list of queries in the form of Lists of integers
     * @author Ken
     */
    public static List<List<Integer>> readNNQueries(String filename) {
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
                if (temp.size() != 2) {
                    System.out.print("File Format Error: Each row must have 2 columns");
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
                
                result.add(built);
                
            }
            
            reader.close();
            
        } catch (IOException e) {
            System.out.print("I/O Exception: " + e.getMessage());
        } finally {
            System.out.println("\nNearest Neighbour query file reading complete");
        }
        
        return result;
    }
    
    
    /**
     * Converts data from a NN queries file into a readable string
     * 
     * @param input A List of Lists of integers (output from readNNQueries)
     * @return List from readNNQueries in string form.
     * @author Ken
     */
    public static String nnQueryToString(List<List<Integer>> input) {
        String result = "";
        
        for(List<Integer> x : input) {
            String builder = "";
            
            builder = "X = " + x.get(0) + ", Y = " + x.get(1) + "\n";
            
            result+= builder;
        }
        
        return result;
    }
    
    
    /**
     * Reads the entire dataset once to establish the sequential scan benchmark
     * 
     * @param inputNodes the dataset read in from file
     * @return long time to read the entire dataset in milliseconds
     * @author Ken
     */
    public static long sequentialScan(List<Point> inputNodes) {
        long startTime = System.currentTimeMillis();
        
        for(Point x:inputNodes) {
            System.out.println(x.toString());
        }
        
        long endTime = System.currentTimeMillis();
        
        return endTime-startTime;
    }
    
    /**
     * Returns all points given in a specified range
     * 
     * @param root the root Mbr of the dataset
     * @param query range specified by the query
     * @return List<Node> a list of all points within query range
     * @author Ken
     */
    public static List<Point> rangeQuery(Mbr root, Mbr query) {
        return new ArrayList<Point>();
    }
    
    
    /**
     * Finds the nearest neighbor of the input point
     * 
     * @param root the root Mbr of the dataset
     * @param point the point specified in the query
     * @return Node nearest neighbor of query point
     * @author Ken
     */
    public static Point nnQuery(Mbr root, Point point) {
        return new Point(0, 0, 0);
    }
}
