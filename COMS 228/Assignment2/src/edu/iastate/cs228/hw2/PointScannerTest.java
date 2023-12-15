package edu.iastate.cs228.hw2;

/*
 * @author Dylan Booth
 */

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Random;

import org.junit.Before;
import org.junit.jupiter.api.Test;

public class PointScannerTest {
	
//	@Before
//	public void prep() throws InputMismatchException, FileNotFoundException {
//		p = new PointScanner("points.txt", Algorithm.SelectionSort);
//		
//	}
	
	public static void main(String[] args) throws InputMismatchException, FileNotFoundException { 
		
		PointScanner p = new PointScanner("points-4.txt", Algorithm.QuickSort);
		//PointScanner p = new PointScanner("points-4.txt", Algorithm.MergeSort);
		//PointScanner p = new PointScanner("points-4.txt", Algorithm.SelectionSort);
		//PointScanner p = new PointScanner("points-4.txt", Algorithm.InsertionSort);
		p.scan();
		
	}
	

	
}
