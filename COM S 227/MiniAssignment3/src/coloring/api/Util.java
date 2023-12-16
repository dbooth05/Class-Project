package coloring.api;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import coloring.ColoringBook;



/**
 * Utility class for an implementation of Minesweeper.  This class
 * contains methods for examining and updating a 2D array of Cell
 * objects.
 */
public class Util
{
  /**
   * Threshold for deciding what's a boundary when reading an
   * image.  If the red, green, and blue components are all below this
   * number, the pixel will be treated as a boundary.
   */
  private static final int THRESHOLD = 100;
  
  public static String[] test0 = 
    {
      "##########",
      "# #  #   #",
      "#    #  ##",
      "## #### ##",
      "#     #  #",
      "##########"   
    };
  
  public static String[] test1 = 
    {
      "##########",
      "# #  #   #",
      "#    #  ##",
      "## #### ##",
      "#        #",
      "##########"   
    };
  
  /**
   * Marker character for initializing a 2D array of cells from
   * an array of Strings.
   */
  public static final char BOUNDARY_CHAR = '#';

  /**
   * Creates a 2D array of Cells from an array of strings, where
   * each string corresponds to one row of the returned array.
   * The jth character of the ith string corresponds to row i, column j
   * of the Cell array.  A BOUNDARY_CHAR character means the corresponding
   * cell will be set as a mine. All strings in the given array
   * must have the same length. 
   * @param descriptor
   *    array of strings from which to construct the Cell array
   * @return
   *    2D array of Cells
   */
  public static Cell[][] createFromStringArray(String[] descriptor)
  {
    int width = descriptor[0].length();
    int height = descriptor.length;
    Cell[][] grid = new Cell[height][width];
    for (int row = 0; row < height; row += 1)
    {
      for (int col = 0; col < width; col += 1)
      {
        grid[row][col] = new Cell();
        if (descriptor[row].charAt(col) == BOUNDARY_CHAR)
        {
          grid[row][col].setBoundary();
        }
      }
    }
    return grid;
  }

  public static Cell[][] createFromImage(String filename)
  {
    Cell[][] grid = null;
    try
    {
      File f = new File(filename);
      System.out.println(f.exists());
      BufferedImage im = ImageIO.read(f);

      int width = im.getWidth();
      int height = im.getHeight();
      grid = new Cell[height][width];
      for (int row = 0; row < height; row += 1)
      {
        for (int col = 0; col < width; col += 1)
        {
          grid[row][col] = new Cell();
          int rgb = im.getRGB(col, row);  // this is x, y
          
          // if rgb are all below threshold, treat it as black
          Color c = new Color(rgb);
          if (c.getRed() < THRESHOLD &&
              c.getGreen() < THRESHOLD &&
              c.getBlue() < THRESHOLD)
          {
            grid[row][col].setBoundary();
          }
                  
//          if (rgb != -1)
//          {
//            // anything but white rendered black for boundary
//            grid[row][col].setBoundary();
//          }
        }
      }
    }
    catch (Exception e)
    {
      System.err.println(e + ": Unable to create image from file " + filename);
    }
    return grid;
  }

  
  /**
   * Converts a grid into an array of strings.  All non-boundary and
   * non-blank cells are rendered as 'o'
   *
   * @param grid
   *   2D array of Cells
   * @param revealAll
   *   true if hidden values should be shown, false otherwise
   * @return
   *   array of strings representing the grid
   */
  public static String[] convertToStringArray(ColoringBook cb)
  {
    String[] ret = new String[cb.getRows()];
    for (int row = 0; row < cb.getRows(); row += 1)
    {
      String current = "";
      for (int col = 0; col < cb.getColumns(); col += 1)
      {
        Cell c = cb.getCell(row, col);
        current += getCharForStatus(c.getStatus());
      }
      ret[row] = current;
    }
    return ret;
  }

  private static char getCharForStatus(Status s)
  {
    switch (s)
    {
      case BOUNDARY: return BOUNDARY_CHAR;
      case UNSEEN: return ' ';
      case SEEN: return 'S';
      case EXPLORE_UP: return '^';
      case EXPLORE_LEFT: return '<';
      case EXPLORE_RIGHT: return '>';
      case EXPLORE_DOWN: return 'v';
      case DONE: return 'o';     
    }
    return ' ';
  }



}
