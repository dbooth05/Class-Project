package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 *  
 * @author Dylan Booth
 *
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		// TODO  
		super(pts);
		super.algorithm = "merge sort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		// TODO 
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		Point left[];
		Point right[];
		
		//finding lengths of halve
		int midLength = pts.length / 2;
		left = Arrays.copyOfRange(pts, 0, midLength);
		right = Arrays.copyOfRange(pts, midLength, pts.length);
		
		if(pts.length <= 1) {
			return;
		}
		
		mergeSortRec(left);
		mergeSortRec(right);
		
		merge(left, right, pts);
	}

	// Other private methods if needed ...
	
	private void merge(Point[] left, Point[] right, Point[] pts) {
		int leftLength = left.length;
		int rightLength = right.length;
		
		int rCounter = 0; int lCounter = 0;
		int i = 0;
		
		Point result[] = new Point[leftLength + rightLength];
		
		while(rCounter < rightLength && lCounter < leftLength) {
			if(pointComparator.compare(left[lCounter], right[rCounter]) <= 0) {
				result[i] = left[lCounter];
				lCounter++;
			} else {
				result[i] = right[rCounter];
				rCounter++;
			}
			i++;
		}
		
		//test this bigTime, otherwise this is if there are still Points not added
		while(lCounter < leftLength) {
			result[i] = left[lCounter];
			i++;
			lCounter++;
		}
		while(rCounter < rightLength) {
			result[i] = right[rCounter];
			i++;
			rCounter++;
		}
		
		for(int j = 0; j < result.length; j++) {
			pts[j] = result[j];
		}
	}
}
























