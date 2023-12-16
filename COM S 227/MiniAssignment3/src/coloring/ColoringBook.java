package coloring;
import java.awt.Color;

import coloring.api.Cell;
import coloring.api.CellObserver;
import coloring.api.Util;
import mini3.FloodFiller;

/**
 * This class encapsulates the state of a coloring book.
 */
public class ColoringBook
{
  /**
   * Array of cells representing the image.
   */
  private Cell[][] grid;
  
  
  /**
   * Constructs an instance of the coloring book using the given array
   * of strings to initialize the image.
   */
  public ColoringBook(String[] descriptor)
  {
    grid = Util.createFromStringArray(descriptor);   
  }
  
  /**
   * Constructs an instance of the coloring book using the given image
   * file.
   */
  public ColoringBook(String filename)
  {
    grid = Util.createFromImage(filename);   
  }
  
  /**
   * Returns the number of rows in the grid.
   * @return
   *   number of rows in the grid
   */
  public int getRows()
  {
    return grid.length;
  }
  
  /**
   * Returns the number of columns in the grid.
   * @return 
   *   number of columns in the grid
   */
  public int getColumns()
  {
    return grid[0].length;
  }
  
  /**
   * Returns the cell at the specified position.
   * <p>
   * NOTE: The caller of this method should normally not modify
   * the returned cell.
   * 
   * @param row
   *   given position row
   * @param col
   *   given position column
   * @return
   *   cell at the given position
   */
  public Cell getCell(int row, int col)
  {
    return grid[row][col];
  }
  
  /**
   * Fills the region starting with given row and column.
   * @param row
   *   given position row
   * @param col
   *   given position column
   */
  public void fill(int row, int col, Color color)
  {
    FloodFiller.fillRegion(grid, row, col, color);
  }
  
  /**
   * Calls setObserver with the given <code>CellObserver</code> on every cell 
   * of the grid.
   * @param observer
   *   reference to a <code>CellObserver</code>
   */
  public void setObserver(CellObserver observer)
  {
    for (int row = 0; row < grid.length; row += 1)
    {
      for (int col = 0; col < grid[0].length; col += 1)    
      {
        grid[row][col].setObserver(observer);
      }
    }
  }
}
