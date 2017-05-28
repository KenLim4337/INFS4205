package test;

import java.io.*;
import java.util.*;
import geography.*;

public class Test {
    
    public static List<Point> inputPoints = new ArrayList<Point>();
    public static RTree tree;
    public static PriorityQueue<Mbr> pq;
    
    //Result storage
    public static List<Point> rangeResult;
    
    //Overall results
    public static List<List<Point>> rangeOverall;
    public static List<List<Point>> nnOverall;
    
    //Records times for each query
    public static List<Long> times;
    
	public static void main(String [] args) {
	    
	    long startTime;
	    long endTime;
		
        tree = new RTree();
        
		inputPoints = readFile(args[0]);
		
		System.out.println(inputToString(inputPoints));
		
		System.out.println(inputPoints.get(0).mindistPt(inputPoints.get(2)));
		
		List<Point> nnQuery = readNNQueries(args[2]);
		
		System.out.println(nnQueryToString(nnQuery));
		
        List<Mbr> rangeQuery = readRangeQueries(args[1]);
		
        System.out.println(rangeQueryToString(rangeQuery));
		
		
		//Breaking, null pointer exception after more than 3 put in tree
		for(Point x : inputPoints) {
		    tree.insert(x);
		    System.out.println(x + " INSERTED");
		}
		
		System.out.println("END");
		
		Mbr root = tree.getRoot();
	
		treeScanner(root);
		
		
		//Actual code stuff ----------------------------------------------------------
		
		/*
		times = new ArrayList<Long>();
		Scanner scanner = new Scanner(System.in);
		String[] command = new String[2];
		rangeOverall = new LinkedList<List<Point>>();
		nnOverall = new LinkedList<List<Point>>();
		*/

	    /**
	     * Keeps reads user input in command line and responds as appropriate
	     * 
	     * Commands:
	     * exit -> Exits system with error code 0
	     * test -> Runs sequential scan
	     * load <file directory> -> loads specified file
	     * range <file directory> -> loads specified range query file and runs it
	     * nn <file directory> -> loads specified nn query file and runs it
	     * 
	     * @author Ken
	     */
		
		/*
		while(true) {
		    System.out.print("Command: ");
		    
		    command = scanner.nextLine().split(" ");
		    
		    //Add other stuff as stuff goes on
		    //Runs sequential scan
		    if (command[0].equals("test")) {
		        if(inputPoints.isEmpty()) {
		            System.out.println("No points loaded");
		        } else {
	                System.out.println(sequentialScan(inputPoints) + " millis");
		        }
		        
		    //Loads file based on input
		    } else if ((command[0].equals("load") && (!(command[1].isEmpty())))){
		        inputPoints = readFile(command[1]);
		        
		    //Loads and runs range query based on input
		    } else if ((command[0].equals("range") && (!(command[1].isEmpty())))) { 
		        List<Mbr> testRangeQueries = readRangeQueries(command[1]);
		        //Init range result
		        rangeResult = new LinkedList<Point>();
                times.clear();
                rangeOverall.clear();
                
		        //read queries, loop through queries, for each, append result to total results
		        for(Mbr x: testRangeQueries) {
		            //Clear range result
		            rangeResult.clear();
		            //Start timer
		            startTime = System.currentTimeMillis();
		            rangeQuery(tree.getRoot(), x);
		            //End timer
                    endTime = System.currentTimeMillis();
                    System.out.println(endTime - startTime);
                    //Add runtime to list of times
                    times.add(endTime-startTime);
                    //Adds range result to overall results
                    rangeOverall.add(rangeResult);
		        }
		        
		        //Save file
		        
		        
		    //Loads and runs NN query based on input
		    } else if ((command[0].equals("nn") && (!(command[1].isEmpty())))) { 
		        //Read in queries
		        List<Point> testNNQueries = readNNQueries(command[1]);
		        times.clear();
		        nnOverall.clear();
		            //Iterate through all queries and run each query, adding results to the overall result list
		            for(Point x: testNNQueries) {
		                //Start timer
		                startTime = System.currentTimeMillis();
		                List<Point> Result = nnQuery(tree.getRoot(),x);
		                //End timer
		                endTime = System.currentTimeMillis();
		                System.out.println(endTime - startTime);
		                //Add runtime to list of times
		                times.add(endTime-startTime);
		                //Add result of individual query to overall list
		                nnOverall.add(Result);
		            }
		        
		        //Save file
		        
                
            //Exits app
            } else if (command[0].equals("exit")) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("Invalid command");
            }
		}
		*/
	}
    
	
	//Scanner
	public static void treeScanner (Mbr root) {
	    if (root.isLeaf()) {
	        for (Point x : root.getLeaves()) {
	            System.out.println(x);
	        } 
	    } else {
	        for (Mbr y: root.getChildren()) {
	            treeScanner(y);
	        }
	    }
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
    	    	
    	    	result.add(new Point(tempX, tempY, tempId));
    	    	
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
    public static List<Mbr> readRangeQueries(String filename) {
    	//Variables
    	List<Mbr> result = new ArrayList<Mbr>();

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
    	    	
    	    	List<Point> ranges = new ArrayList<Point>();
    	    	
    	    	//x,y
    	    	ranges.add(new Point(built.get(0), built.get(2), -1));
                
    	    	//x1,y1
    	    	ranges.add(new Point(built.get(1), built.get(3), -1));
    	    	
    	    	result.add(new Mbr(ranges));
    	    	
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
    public static String rangeQueryToString(List<Mbr> input) {
        String result = "";
        
        for(Mbr x : input) {
            String builder = "";
            
            builder = x.toString() + "\n";
            
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
     * @return List<Point> A list of queries in the form of points
     * @author Ken
     */
    public static List<Point> readNNQueries(String filename) {
        //Variables
        List<Point> result = new ArrayList<Point>();

        //Try/catch block for I/O Exception
        try {
            File file = new File (filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            System.out.println("\nFile loaded");
            
            String line=null;
            
            while((line=reader.readLine()) != null) {
                List<String> temp = Arrays.asList(line.split(" "));

                List<Integer> built = new ArrayList<Integer>();
                
                //Check each row only has 2 columns
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
                
                result.add(new Point(built.get(0), built.get(1), -1));
                
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
    public static String nnQueryToString(List<Point> input) {
        String result = "";
        
        for(Point x : input) {
            
            String builder = x.toString() + "\n";
            
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
            
        }
        
        long endTime = System.currentTimeMillis();
        
        return endTime-startTime;
    }
    
    /**
     * Returns all points given in a specified range
     * 
     * @param root the root Mbr of the dataset
     * @param query range specified by the query
     * @author Ken
     */
    public static void rangeQuery(Mbr node, Mbr query) {
        
        //If leaf mbr, check if leaf points are in range, if yes add to results
        if (node.isLeaf()) {
            for(Point x:node.getPoints()){
                if(query.contains(x)) {
                    rangeResult.add(x);
                    System.out.println(x.toString());
                }
            }
        } else {
            for(Mbr x: node.getChildren()) {
                //Checks if child mbr intersect s the query range if yes run query on that too
                if(x.intersects(query)) {
                    rangeQuery(x, query);
                }
            }
        }
    }
    
    
    /**
     * Finds the nearest neighbor of the input point
     * 
     * @param root the root Mbr of the dataset
     * @param point the point specified in the query
     * @return List of node nearest neighbor of query point
     * @author Ken
     */
    public static List<Point> nnQuery(Mbr root, Point target) {
        /*
         *  Pseudocode
         * 
         * - Make priority queue
         * - Add root to pqueue
         * - loop
         * - pop pqueue
         * - Add children of current to priority queue
         * - If no child mbr, add points
         * - If point, check if closer than current best, if closer, best = current
         * - repeat until pbest is smaller than the smallest mindist in the list
         */
        
        //Set up priority queue
        pq = new PriorityQueue<Mbr>(10, new Comparator<Mbr>() {
            //Complete implementation after mindist is implemented
            public int compare(Mbr o1, Mbr o2) {
                
                if (target.mindistMbr(o1) < target.mindistMbr(o2)) return -1;
                if (target.mindistMbr(o1) > target.mindistMbr(o2)) return 1;
                
                return 0;
            }});
        
        //List of best points, if lower than the first one, remake list, else append to list
        List<Point> bestPoints = new ArrayList<Point>();
        
        //Init best distance as max at first
        double bestMin = Double.MAX_VALUE;
        
        pq.add(root);
        
        Mbr current = root;
        
        //Loop while the current best 
        while(bestMin > target.mindistMbr(current)) {

            current = pq.poll();
            
            //If no children mbr (leaf mbr), check points and determine best point
            if(current.isLeaf()) {
                for(Point x: current.getPoints()) {
                    if (target.mindistPt(x) < bestMin) {
                        //If a better point, clear list and add x
                        bestPoints.clear();
                        bestPoints.add(x);
                        bestMin = target.mindistPt(x);
                    } else if(target.mindistPt(x) == bestMin) {
                        //If equal to current best, add to list
                        bestPoints.add(x);
                    }
                }
            } else {
                //Have child, put all children into the priority queue
                for(Mbr x: current.getChildren()) {
                    pq.add(x);
                }
            }
        }
        
        return bestPoints;
    }
 
    /**
     * Saves a text file containing range query results and times
     * 
     */
    public static void saveRange(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8"); 

            //Go through each result
            for (List<Point> x : rangeOverall) {
                long time = times.get(rangeOverall.indexOf(x));
                        
                String build = "";
                
                //Build string for each result
                for (Point y : x) {
                   build += "id_" + y.getId() + " x_" + y.getX() + " y_" + y.getY() + ", ";
                }
                
                //Trim end and add time
                build = build.substring(0, build.length()-2) + " - Time: " + time;
                
                //Write to file
                writer.write(build + "\n");
                
            }
            writer.write(" ");
            writer.close();
            
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
    
    /**
     * Saves a text file containing nn query results and times
     * 
     */
    public static void saveNN(String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8"); 

            //Go through each result
            for (List<Point> x : nnOverall) {
                long time = times.get(nnOverall.indexOf(x));
                        
                String build = "";
                
                //Build string for each result
                for (Point y : x) {
                   build += "id_" + y.getId() + " x_" + y.getX() + " y_" + y.getY() + ", ";
                }
                
                //Trim end and add time
                build = build.substring(0, build.length()-2) + " - Time: " + time;
                
                //Write to file
                writer.write(build + "\n");
            }
            writer.write(" ");
            writer.close();
            
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
    
}
