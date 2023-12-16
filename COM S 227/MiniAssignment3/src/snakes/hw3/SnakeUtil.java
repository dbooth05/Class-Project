package snakes.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import snakes.api.GridUtil;
import snakes.api.SnakeLayoutException;

/**
 * Utility methods for working with Snakes.
 */
public class SnakeUtil
{
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
    // may throw SnakeLayoutException
    GridUtil.validateDescriptor(desc);
    
    int rows = desc.length;
    String firstRow = desc[0];
    String[] tokens = firstRow.split("\\s+");
    int cols = tokens.length;
    
    ArrayList<Snake> list = new ArrayList<>();
    
    for (int row = 0; row < rows; ++row)
    {
      String s = desc[row];
      tokens = s.split("\\s+");
      for (int col = 0; col < cols; ++col)
      {
        String t = tokens[col];
        
        // any string with length greater than 1 has to be a snake id
        // followed by a number
        if (t.length() > 1)
        {
          char id = t.charAt(0);
          int index = Integer.parseInt(t.substring(1));
          
          // see if we have a snake with given id
          boolean found = false;
          for (Snake snake : list)
          {
            if (snake.getId() == id)
            {
              snake.addPiece(index, row, col);
              found = true;
              break;
            }
          }
          if (!found)
          {
            Snake snake = new Snake(id);
            snake.addPiece(index, row, col);
            list.add(snake);
          }             
        }
      }  
    }
    
    for (Snake snake : list)
    {
      if (!snake.isValid())
      {
        throw new SnakeLayoutException("Invalid snake, id '" + snake.getId() + "'.");
      }
    }  
    return list;
  }

  
  /**
   * Reads a the given file and creates a string array from
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
    ArrayList<String[]> games = new ArrayList<>();
    File f = new File(filename);
    Scanner sc = new Scanner(f);
    
    ArrayList<String> current = new ArrayList<>();
    while (sc.hasNextLine())
    {
      String line = sc.nextLine();
      if (line.strip().length() == 0)
      {
        if (current.size() > 0)
        {
          String[] desc = current.toArray(new String[0]);
          games.add(desc);
          current.clear();
        }
        // else do nothing
      }
      else
      {
        current.add(line);
      }
    }
    sc.close();
    // in case there isn't a blank line at end of file, 
    // check current.size
    if (current.size() > 0)
    {
      String[] desc = current.toArray(new String[0]);
      games.add(desc);
      current.clear();
    }    
    
    return games;
  }
}
