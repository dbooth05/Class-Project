package snakes.api;
import static snakes.api.Status.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import snakes.hw3.Snake;
import snakes.hw3.SnakeBasket;

/**
 * Utility class with methods for printing out a Snake Escape grid
 * in text format, and for constructing a grid from a string array
 * descriptor.
 * @author smkautz
 */
public class GridUtil
{
  public static final String test0[] = {
    "#  E  #  #",
    "#  .  .  #",
    "#  g1 g0 #",
    "#  g2 #  #",
    "#  #  #  #"  
  };
  
  public static final String test00[] = {
    "#  E  #  #",
    "#  y0 y1 #",  
    "#  .  .  #",
    "#  g1 g0 #",
    "#  g2 .  #",
    "#  #  #  #"  
  };
  
  public static final String[] test1 = {
  "#  #  #  E  #  #  #",
  "#  y4 y3 y2 y1 y0 #",
  "#  .  #  #  #  .  #",
  "#  .  .  .  .  .  #",
  "#  #  #  g0 #  #  #",
  "#  #  #  g1 #  #  #",
  "#  #  #  g2 #  #  #",
  "#  #  #  g3 #  #  #",
  "#  #  #  #  #  #  #"
  };
  
  public static final String[] test2 = {
  "#  #  #  E  #  #  #",
  "#  y4 y3 y2 y1 y0 #",
  "#  @  #  #  #  @  #",
  "#  .  $  .  $  .  #",
  "#  #  #  g0 #  #  #",
  "#  #  #  g1 #  #  #",
  "#  #  #  g2 #  #  #",
  "#  #  #  g3 #  #  #",
  "#  #  #  #  #  #  #"
  };
  
  public static String[] test3 = {
  "#  #  #  E  #  #  #",
  "#  y0 y1 y2 y3 y4 #",
  "#  .  x0 .  g0 .  #",
  "#  .  x1 .  g1 .  #",
  "#  #  x2 #  g2 #  #",
  "#  x4 x3 #  g3 g4 #",
  "#  #  #  #  #  #  #"
  };
  
  /**
   * Constructs a 2D grid of BasketCell objects from the given
   * string array descriptor.  All snake information in the array 
   * is ignored.  Clients should use the SnakeUtil method 
   * <code>findSnakes</code> to get the initial snake layout
   * from the descriptor.
   * @param desc
   *   given string array
   * @return
   *   2D array of BasketCell
   * @throws SnakeLayoutException
   *   if the given descriptor is invalid
   */
  public static BasketCell[][] createGridFromDescriptor(String[] desc)
  {
    // may throw SnakeLayoutException
    validateDescriptor(desc);   
  
    int rows = desc.length;
    String firstRow = desc[0];
    String[] tokens = firstRow.split("\\s+");
    int cols = tokens.length;
    
    BasketCell[][] grid = new BasketCell[rows][cols];
    for (int row = 0; row < rows; ++row)
    {
      String s = desc[row];
      tokens = s.split("\\s+");
      for (int col = 0; col < cols; ++col)
      {
        String t = tokens[col];
        if (t.equals("#"))
        {
          grid[row][col] = new BasketCell(WALL);
        }
        else if (t.equals("E"))
        {
          grid[row][col] = new BasketCell(EXIT);
        }
        else if (t.equals("@"))
        {
          grid[row][col] = new BasketCell(APPLE);
        }
        else if (t.equals("$"))
        {
          grid[row][col] = new BasketCell(MUSHROOM);
        }
        else
        {
          grid[row][col] = new BasketCell(EMPTY);          
        }
      }
    }
    return grid;
  }
  
  /**
   * Checks the given descriptor for validity and throws
   * SnakeLayoutException if it isn't.  A descriptor 
   * is considered to be valid if:
   * <ul>
   *   <li>The array is nonempty
   *   <li>Each string contains the same positive number of whitespace-
   *   separated tokens
   *   <li>Every token that is a single character is one of the
   *   characters followng characters:
   *     <ul>
   *       <li>'.' (an empty cell)
   *       <li>'#' (a wall)
   *       <li>'E' (an exit)
   *       <li>'@' (an apple)
   *       <li>'$' (a mushroom)
   *     </ul>.#E@$
   *   <li>Every token that has multiple characters is a string
   *   with a single character followed by an integer
   * </ul>
   * In particular note that this method does NOT check whether
   * the snake information represents a valid set of snakes, only
   * that the format of the text is correct. Clients should use
   * the SnakeUtil method <code>findSnakes</code> to process the
   * snake information and the Snake method <code>isValid</code>
   * to check validity of each snake. 
   * @param desc
   *   array of strings
   * @throws SnakeLayoutException
   *   if the given descriptor is not valid
   */
  public static void validateDescriptor(String[] desc)
  {
    int rows = desc.length;
    if (rows == 0)
    {
      throw new SnakeLayoutException("Descriptor is empty. ");
    }
    String firstRow = desc[0];
    String[] tokens = firstRow.split("\\s+");
    int cols = tokens.length;
    if (cols == 0)
    {
      throw new SnakeLayoutException("First row is blank. ");      
    }
    
    for (int row = 0; row < rows; ++row)
    {
      String s = desc[row];
      tokens = s.split("\\s+");
      if (tokens.length != cols)
      {
        throw new SnakeLayoutException("Row " + row + " length is inconsistent.");
      }
      for (int col = 0; col < cols; ++col)
      {
        String t = tokens[col];
        if (t.length() == 1)
        {
          char c = t.charAt(0);
          if (".#E@$".indexOf(c) < 0)
          {
            throw new SnakeLayoutException("Invalid single character " + c + "in row " + row + ". ");
          }
        }
        else
        {
          // t.length() can't be 0 since our delimiter was whitespace, so it's > 1
          // make sure everything after the first character is numeric
          for (int i = 1; i < t.length(); ++i)
          {
            if (!Character.isDigit(t.charAt(i)))
            {
              throw new SnakeLayoutException("Invalid number in string " + t + " in row " + row + ". ");
            }
          }
        }
      }
    }
  }

  /**
   * Converts the game grid into a descriptor.
   * @param game
   *   The game instance
   * @return
   *   String array having the form of a game descriptor
   */
  public static String[] makeDescriptor(SnakeBasket game)
  {   
    String[] desc = new String[game.getRows()];    
    for (int row = 0; row < game.getRows(); ++row)
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      
      for (int col = 0; col < game.getCols(); ++col)
      {
        String s = makeString(game, row, col);
        pw.printf("%-3s", s);
      }
      desc[row] = sw.toString();
    }
    return desc;
  }
  
  /**
   * Prints a text representation of the game grid.
   * @param game
   *   instance of a Snake Escape game
   */
  public static void display(String[] desc)
  {
    String firstRow = desc[0];
    String[] tokens = firstRow.split("\\s+");
    int cols = tokens.length;
    
    // print column numbers across the top
    System.out.print("   ");
    for (int col = 0; col < cols; ++col)
    {
      System.out.printf("%-3d", col);
    }
    System.out.println();
    
    for (int row = 0; row < desc.length; ++row)
    {
      // print row numbers at left
      System.out.printf("%-3d", row);    
      System.out.println(desc[row]);
    }
  }
  
  /**
   * Prints a text representation of the game state.
   * @param game
   */
  public static void display(SnakeBasket game)
  {
    display(makeDescriptor(game));
  }
  
  /**
   * Helper method converts the cell at given row and column in 
   * the game into a string representation according to the 
   * conventions of a valid descriptor.
   * @param game
   *   instance of a Snake Escape game
   * @param row
   *   row of cell to be printed
   * @param col
   *   column of cell to be printed
   * @return
   *   string representation of the cell at given row and column
   */
  private static String makeString(SnakeBasket game, int row, int col)
  {    
    BasketCell cell = game.getCell(row, col);
    if (cell.isEmpty())
    {
      return ".";
    }
    else if (cell.isWall())
    {
      return "#";
    }
    else if (cell.isExit() && cell.getSnake() == null)
    {
      return "E";
    }
    else if (cell.isApple())
    {
      return "@";
    }
    else if (cell.isMushroom())
    {
      return "$";
    }
    else
    {
      // gotta be a snake...
      if (cell.getSnake() != null)
      {
        char id = cell.getId();
        int index = findSnakePieceIndex(cell, row, col);
        return "" + id + index;
      }
      else
      {
        // should never happen...
        return "?";
      }
    }
  }
  
  /**
   * Helper method that, given a cell occupied by a snake, finds
   * the index within the snake of the snake piece that has
   * the cell's row and column.
   * @param cell
   *   a cell in the grid for which <code>hasSnake</code> is true
   * @param row
   *   row of the cell
   * @param col
   *   column of the cell
   * @return
   *   index in the snake of the snake piece with the given row and column
   */
  private static int findSnakePieceIndex(BasketCell cell, int row, int col)
  {
    Snake s = cell.getSnake();
    ArrayList<SnakePiece> pieces = s.getPieces();
    for (int i = 0; i < pieces.size(); ++i)
    {
      SnakePiece p = pieces.get(i);
      if (row == p.row() && col == p.col())
      {
        return i;
      }       
    }
    return -1;
  }
  
}
