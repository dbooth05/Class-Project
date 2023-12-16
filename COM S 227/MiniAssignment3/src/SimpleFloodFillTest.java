


import java.awt.Color;
import java.util.Scanner;

import coloring.ColoringBook;
import coloring.api.Cell;
import coloring.api.CellObserver;
import coloring.api.Util;

public class SimpleFloodFillTest
{ 
  
  public static void main(String[] args)
  {
    ColoringBook cb = new ColoringBook(Util.test0);
    runFloodFill(cb, 2, 2);
   // runFloodFillVerbose(cb, 2, 2);
  }
  
  /**
   * Runs the flood fill algorithm on the given ColoringBook starting
   * at the given row and column, and displays the grid before and
   * after.
   */
  public static void runFloodFill(ColoringBook cb, int row, int col)
  {
    display(cb);
    System.out.println();
    cb.fill(row, col, Color.RED);
    display(cb);    
  }
  
  
  /**
   * Runs the flood fill algorithm on the given ColoringBook starting
   * at the given row and column, and pauses to display the grid after
   * every step.
   */
  public static void runFloodFillVerbose(ColoringBook cb, int row, int col)
  {
    // the listener's update method will be called after every cell update
    cb.setObserver(new ConsoleListener(cb));
    display(cb);
    System.out.println();
    cb.fill(row, col, Color.RED);
  }
  
  /**
   * Displays the coloring book's grid in text form.  See Util.getCharForStatus
   * for the meaning of the characters.
   * @param cb
   */
  public static void display(ColoringBook cb)
  {
    String[] grid = Util.convertToStringArray(cb);
    for (String row : grid)
    {
      System.out.println(row);
    }   
  }
  
  /**
   * Implementation of the CellObserver interface that causes
   * the implementation to pause until user presses the 
   * ENTER key.
   */
  private static class ConsoleListener implements CellObserver
  {
    private Scanner in = new Scanner(System.in);
    private ColoringBook cb;
    
    public ConsoleListener(ColoringBook cb)
    {
      this.cb = cb;
    }
    
    @Override
    public void update(Cell c)
    {
      display(cb);
      System.out.println();
      System.out.println("Press ENTER to continue...");
      in.nextLine();
    }
    
  }
}
