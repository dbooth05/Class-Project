package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner; 
import java.util.Random; 

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain 
{
	private int width; // grid size: width X width 
	
	public Living[][] grid; 
	
	/**
	 *  Default constructor reads from a file 
	 */
	public Plain(String inputFileName) throws FileNotFoundException
	{		
        // TODO 
		// 
		// Assumption: The input file is in correct format. 
		// 
		// You may create the grid plain in the following steps: 
		// 
		// 1) Reads the first line to determine the width of the grid.
		// 
		// 2) Creates a grid object. 
		// 
		// 3) Fills in the grid according to the input file. 
		// 
		// Be sure to close the input file when you are done. 
		
		Scanner sc = new Scanner(new File(inputFileName));
		width = 0;
		
		String line = sc.nextLine();
		sc = new Scanner(line);
		while(sc.hasNext()) {
			width++;
			sc.next();
		}
		
		sc = new Scanner(new File(inputFileName));
		grid = new Living[width][width];
		
		for(int i = 0; i < width; i++) {
			
			for(int j = 0; j < width; j++) {
				
				String temp = sc.next();
				char ch = temp.charAt(0);
				char age = ' ';
				if(temp.length() > 1) {
					age = (char) Character.getNumericValue(temp.charAt(1));
				}
				
				if(ch == 'B') {
						grid[i][j] = new Badger(this, i, j, age);
				}	else if(ch == 'F') {
						grid[i][j] = new Fox(this, i, j, age);
				}	else if(ch == 'R') {
						grid[i][j] = new Rabbit(this, i, j, age);
				}	else if(ch == 'E') {
						grid[i][j] = new Empty(this, i, j);
				}	else { 
						grid[i][j] = new Grass(this, i, j);
				}
				
			}
		}
		sc.close();
		
	}

	
	/**
	 * Constructor that builds a w x w grid without initializing it. 
	 * @param width  the grid 
	 */
	
	
	
	public Plain(int w)
	{
		// TODO
		width = w;
		
		grid = new Living[width][width];
	}
	
	
	public int getWidth()
	{
		// TODO  
		return width;  // to be modified 
	}
	
	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, FOX, RABBIT, GRASS, or EMPTY.  
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit()
	{
		Random generator = new Random(); 
		 
		// TODO
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < width; j++) {
				int num = generator.nextInt(5);
				if(num == 0) {
						grid[i][j] = new Badger(this, i, j, 0);
				}	else if(num == 1) {
						grid[i][j] = new Fox(this, i, j, 0);
				}	else if(num == 2) {
						grid[i][j] = new Rabbit(this, i, j, 0);
				}	else if(num == 3) {
						grid[i][j] = new Empty(this, i, j);
				}	else if(num == 4) {
						grid[i][j] = new Grass(this, i, j);
				}
			}
		}
	}
	
	
	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.  
	 */
	public String toString()
	{
		// TODO
		
		String thing = "";
		
		for(int i = 0; i < width; i++) {
			
			for(int j = 0; j < width; j++) {
				
				if(grid[i][j].who() == State.BADGER) {
						thing += "B" + ((Badger) grid[i][j]).myAge() + " ";
				}	else if(grid[i][j].who() == State.FOX) {
						thing += "F" + ((Fox) grid[i][j]).myAge() + " ";
				}	else if(grid[i][j].who() == State.RABBIT) {
						thing += "R" + ((Rabbit) grid[i][j]).myAge() + " ";
				}	else if(grid[i][j].who() == State.EMPTY) {
						thing += "E" + "  ";
				}	else if(grid[i][j].who() == State.GRASS) {
						thing += "G" + "  ";
				}	
				
			}
			thing += "\n";

		}
		return thing; 
	}
	

	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose. 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws FileNotFoundException
	{
		// TODO 
		// 
		// 1. Open the file. 
		// 
		// 2. Write to the file. The five life forms are represented by characters 
		//    B, E, F, G, R. Leave one blank space in between. Examples are given in
		//    the project description. 
		// 
		// 3. Close the file. 
		
		PrintWriter write = new PrintWriter(new File(outputFileName));
		
		write.append(toString());
		
		write.close();
	}
}















