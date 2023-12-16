package snakes.api;
import static snakes.api.Status.*;

import snakes.hw3.Snake;

/**
 * Class representing one cell in a 2D grid for a Snake Escape game.
 * A cell may represent an empty position, a wall, the exit, a mushroom,
 * or an apple, as represented by the Status attribute. 
 * After construction, the status cannot be directly examined or 
 * manipulated; instead, clients use the accessor methods such as 
 * <code>isEmpty</code> or <code>isWall</code>.  The only way the 
 * status can change is that an apple or mushroom may be changed 
 * to empty via the method <code>clearFood</code>.
 * <p>
 * In addition to the status, a cell may contain a snake.  This is
 * only possible for cells that are empty (or the exit). See methods
 * <code>setSnake</code>, <code>getSnake</code>, and 
 * <code>clearSnake</code>.
 * @author smkautz
 */
public class BasketCell
{
  /**
   * Status of this cell.
   */
  private Status status;
  
  /**
   * Reference to snake occupying this cell, if any.
   */
  private Snake snake;
  
  /**
   * Constructs an empty BasketCell.
   */
  public BasketCell()
  {
    status = EMPTY;
    snake = null;
  }
  
  /**
   * Constructs a BasketCell with the given Status.
   * @param givenStatus
   *   status for this cell
   */
  public BasketCell(Status givenStatus)
  {
    status = givenStatus;
    snake = null;
  }
  
  /**
   * Determines whether this cell is a wall.
   * @return
   *   true if this cell is a wall, false otherwise
   */
  public boolean isWall()
  {
    return status == WALL;
  }
  
  /**
   * Determines whether this cell is an exit.
   * @return
   *   true if this cell is an exit, false otherwise
   */
  public boolean isExit()
  {
    return status == EXIT;
  }
  
  /**
   * Determines whether this cell is empty; that is, 
   * its status is EMPTY and it does not contain a snake.
   * @return
   *   true if this cell is empty, false otherwise
   */
  public boolean isEmpty()
  {
    return status == EMPTY && snake == null;
  }
  
  /**
   * Determines whether this cell is an apple.
   * @return
   *   true if this cell is an apple, false otherwise
   */
  public boolean isApple()
  {
    return status == APPLE;
  }

  /**
   * Determines whether this cell is a mushroom.
   * @return
   *   true if this cell is a mushroom, false otherwise
   */
  public boolean isMushroom()
  {
    return status == MUSHROOM;
  }
  
  /**
   * Determines whether this cell currently contains
   * a snake.
   * @return
   *   true if this cell has a snake, false otherwise
   */
  public boolean hasSnake()
  {
    return snake != null;
  }
  
  /**
   * Returns a reference to the snake currently in this 
   * cell, or null if it does not contain a snake.
   * @return
   *   reference to the snake in this cell, or null
   */
  public Snake getSnake()
  {
    return snake;
  }
  
  /**
   * Returns the single-character id of the snake currently
   * in this cell, or '-' if it does not contain a snake.
   * @return
   *   id of the snake in this cell, or '-'
   */
  public char getId()
  {
    return snake != null ? snake.getId() : '-';
  }
  
  /**
   * Clears the snake from this cell, if any.
   */
  public void clearSnake()
  {
    snake = null;
  }
  
  /**
   * Changes the status from apple or mushroom to empty.  Does 
   * nothing if the status isn't apple or mushroom.
   */
  public void clearFood()
  {
    if (status == APPLE || status == MUSHROOM)
    {
      status = EMPTY;
    }
  }
  
  /**
   * Sets the given snake in this cell, if possible.  If 
   * the cell is neither empty nor an exit, or if it already
   * contains a snake, this method throws a SnakeLayoutException.
   * @param s
   *   snake to set in this cell
   * @throws SnakeLayoutException
   *   if snake cannot be set
   */
  public void setSnake(Snake s)
  {
    if (isEmpty() || isExit())
    {
      snake = s;
    }
    else
    {
      String msg;
      if (status == EMPTY)
      {
        msg = "Attempt to set snake in cell already containing a snake. ";
      }
      else
      {
        msg = "Attempt to set snake in " + status + " cell. ";
      }
      throw new SnakeLayoutException(msg);
    }
  }
  
}
