package coloring.api;
import java.awt.Color;

/**
 * Cell type for the animated coloring book.
 */
public class Cell
{
  /**
   * Status of this cell.
   */
  private Status status;

  /**
   * Color of this cell (normally displayed only when
   * its status is DONE).
   */
  private Color color;
  
  /**
   * Observer to receive notifications if the cell's status changes.
   */
  private CellObserver observer;
  
  /**
   * Constructs a cell to be located at the given row and column.
   * @param givenRow
   *   row where this cell will be located
   * @param givenCol
   *   column where this cell will be located
   */
  public Cell()
  {
    status = Status.UNSEEN;
    color = Color.WHITE;
  }
  
  public void setColor(Color c)
  {
    color = c;
  }
  
  public Color getColor()
  {
    return color;
  }
  
  /**
   * Sets an observer to be notified if the cell's status changes.
   * @param givenObserver
   *   observer to be notified if the cell's status changes
   */
  public void setObserver(CellObserver givenObserver)
  {
    observer = givenObserver;
  }
  
  /**
   * Sets whether or not this cell is a boundary.
   * @param isBoundary
   *   true if the cell is a boundary, false otherwise
   */
  public void setBoundary()
  {
    status = Status.BOUNDARY;
    color = Color.BLACK;
  }
  
  /**
   * Returns whether this cell is a boundary.
   * @return
   *   true if this cell is a boundary, false otherwise
   */
  public boolean isBoundary()
  {
    return status == Status.BOUNDARY;
  }
  
  /**
   * Returns the status for this cell.
   * @return
   *   status for this cell
   */
  public Status getStatus()
  {
    return status;
  }
  
  /**
   * Sets the status for this cell. This method also notifies the observer
   * if there is one.
   * @param givenStatus
   *   status for this cell
   */
  public void setStatus(Status givenStatus)
  {  
    status = givenStatus;
    if (observer != null)
    {
      observer.update(this);
    }
  }

}
