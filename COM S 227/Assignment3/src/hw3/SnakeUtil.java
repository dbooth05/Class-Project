package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import api.GridUtil;
import api.SnakeLayoutException;

/**
 * Utility methods for working with Snakes.
 */
public class SnakeUtil
{
  /**
   * Private constructor disables instantiation.
   */
  private SnakeUtil() {}
  
  /**
   * Given a descriptor array, return a list of all the snakes
   * it contains. Snake pieces are represented in the descriptor
   * array by id strings consisting of a single character followed
   * by a number. All other text elements in the descriptor are single 
   * characters only and are ignored by this method.  
   * The first character of the id string
   * becomes the "id" for the Snake. The number following
   * the id becomes the actual list index for the corresponding piece
   * of the snake.  For example, if row 3 and column 5 of the 
   * descriptor is the string "g7", then the snake with id 'g' should
   * have, at index 7 of its pieces list, a SnakePiece with row 3, column 5 .
   * <p>
   * This method first calls GridUtil.validateDescriptor, which may
   * throw a SnakeLayoutException.  In addition, after constructing
   * the list of snakes, this method checks isValid() on each
   * snake and throws SnakeLayoutException if any snake is found
   * to be invalid.
   * <p>
   * The order of snakes within the returned list is unspecified.
   * @param desc
   *   descriptor array
   * @return
   *   list of snakes in the descriptor
   * @throws SnakeLayoutException
   *   if the descriptor fails to pass GridUtil.validateDescriptor
   *   or if any of the resulting snakes is invalid
   */  
  public static ArrayList<Snake> findSnakes(String[] desc)
  {
	  GridUtil.validateDescriptor(desc); //check to validate the descriptor
	  ArrayList<Snake> snakes = new ArrayList<> (); //snake ArrayList to hold snake values
	  Snake tempSnake; //snake variable for instance uses
	  int index; //temp int to hold the index of where the snakepiece is in the snake
	  for(int i = 0; i < desc.length; i++) {
		  String line = desc[i]; //String to work with one line at a time
		  Scanner sc = new Scanner(line); //Scanner object to search line for snakes
		  for(int j = 0; sc.hasNext(); j++) {
			  String temp = sc.next(); //temp value to hold the value returned by scanner
			  if(temp.compareTo("#") > 0 && temp.compareTo("E") > 0 && temp.compareTo(".") > 0 && temp.compareTo("$") > 0 && temp.compareTo("@") > 0) { 
				  if(exists(snakes, temp.charAt(0))) {
					  index = Character.getNumericValue(temp.charAt(1)); 
					  tempSnake = snakes.get(indexOfCurSnake(snakes, temp.charAt(0)));
					  tempSnake.addPiece(index, i, j);
				  }
				  else {
					  tempSnake = new Snake(temp.charAt(0));
					  index = Character.getNumericValue(temp.charAt(1));
					  tempSnake.addPiece(index, i, j);
					  snakes.add(tempSnake);
				  }
			  }
		  }
		  sc.close();
	  }
	  for(int i = 0; i < snakes.size(); i++) { //checking the ArrayList to make sure snakes are valid snakes
		  snakes.get(i).isValid();
	  }

    return snakes;
  }
  private static int indexOfCurSnake(ArrayList<Snake> snakes, char ID) { //helper method to find index of snake currently working with to make sure snakepiece ends up in right spot
	  int res = 0;
	  for(int i = 0; i < snakes.size(); i++) {
		  if(snakes.get(i).getId() == ID) {
			  res = i;
		  }
	  }
	  return res;
  }
  
  private static boolean exists(ArrayList<Snake> snakes, char ID) { // helper method to check is snake with given ID exists
	  for(int i = 0; i < snakes.size(); i++) {		  
		  if(snakes.get(i).getId() == ID) {
			  return true;
		  }
	  }  
	  return false;
  }

  
  /**
   * Reads the given file and creates a string array from
   * each block of text separated by whitespace. The string
   * arrays are returned in an ArrayList.  Despite the name
   * of this method, it does NO checking for whether
   * a given block of text actually represents a descriptor
   * for the game.  The file is assumed to end with a blank line.
   * @param filename
   *   name of the file to read
   * @return
   *   ArrayList of string arrays, one for each block of text
   * @throws FileNotFoundException
   *   if the named file cannot be opened
   */
  public static ArrayList<String[]> createDescriptorsFromFile(String filename) throws FileNotFoundException
  {
	  ArrayList<String[]> res = new ArrayList<String[]> (); //ArrayList to return
	  ArrayList<String> temp = new ArrayList<String> (); //Temp arraylist to add temporary blocks to res
	  File file = new File(filename);
	  Scanner sc = new Scanner(file);
	  while(sc.hasNextLine()) {
		  String line = sc.nextLine();
		  if(line != "") {
			  temp.add(line);
		  }
		  else if(temp.size() > 0 && line == "") {
			  res.add(listToArray(temp)); //call on helper method to translate ArrayList to an array
			  temp.clear(); //clears temp array for next block
		  }
		  
	  }
	  sc.close();
	  return res;
  }
  
  private static String[] listToArray(ArrayList<String> arr) { //helper method to turn an ArrayList<String[]> to an String[];
	  String[] result = new String[arr.size()];
	  for(int i = 0; i < result.length; i++) {
		  result[i] = arr.get(i);
	  }
	  return result;
  }
}
