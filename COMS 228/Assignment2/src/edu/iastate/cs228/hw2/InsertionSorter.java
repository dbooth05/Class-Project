package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;


/**
 *  
 * @author Dylan Booth
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		// TODO
		super(pts);
		super.algorithm = "insertion sort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		//instance variable to hold the value of a point
		Point temp;
		
		for(int i = 1; i < points.length; i++) {
			
			temp = points[i];
			int index = i;
			int j = i - 1;
			
			while(j > -1 && pointComparator.compare(points[j], points[i]) >= 1) {
				
				swap(i, j);
				i--;
				j--;
			}
			i = index;
		}

	}		
}




















