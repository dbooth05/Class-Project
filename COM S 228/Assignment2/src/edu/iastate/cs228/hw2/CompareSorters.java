package edu.iastate.cs228.hw2;

/**
 *  
 * @author Dylan Booth
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner; 
import java.util.Random; 


public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{		
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4]; 
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 
		
		int choice = -1;
		int iter = 1;
		String fileName = "";
		Scanner sc = new Scanner(System.in);
		Point pts[];
		Random random = new Random();
		
		while(choice != 3) {
			int size = 0;
			choice = -1;
				
			while(choice < 1 || choice > 3) {
				System.out.println("keys:  1 (random integers)  2 (file input)  3 (exit)");
				System.out.print("Trial " + iter + ": ");
				choice = sc.nextInt();
				System.out.println();
			}
			
			//random integers
			if(choice == 1) {
				System.out.print("Enter number of random points: ");
				size = sc.nextInt();
				pts = generateRandomPoints(size, random);
				System.out.println();
				scanners[0] = new PointScanner(pts, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(pts, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(pts, Algorithm.MergeSort);
				scanners[3] = new PointScanner(pts, Algorithm.QuickSort);
			} else if(choice == 2) {
				System.out.println("Points from a file");
				System.out.print("File name: ");
				fileName = sc.next();
				System.out.println();
				scanners[0] = new PointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new PointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new PointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new PointScanner(fileName, Algorithm.QuickSort);
			} else if(choice == 3) {
				break;
			}
			System.out.println(String.format("%-17s %-6s %-8s", "algorithm", "size", "time (ns)"));
			System.out.println("----------------------------------");
			
			//TODO call the sorting methods
			for(int i = 0; i < scanners.length; i++) {				
				scanners[i].scan();
				System.out.println(scanners[i].stats());
			}
			
			System.out.println();
			iter++;
		}
		
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if(numPts < 1) {
			throw new IllegalArgumentException();
		}
		Point pts[] = new Point[numPts];
		for(int i = 0; i < numPts; i++) {
			int x = rand.nextInt(101) - 50;
			int y = rand.nextInt(101) - 50;
			pts[i] = new Point(x, y);
		}
		
		return pts; 
		 
	}
	
}
