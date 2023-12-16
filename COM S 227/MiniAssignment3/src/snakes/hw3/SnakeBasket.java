package snakes.hw3;
import java.util.ArrayList;
//import static Status;
import java.util.Arrays;

import snakes.api.BasketCell;
import snakes.api.Direction;
import snakes.api.GridUtil;
import snakes.api.SnakePiece;

/**
 * This class represents the basic game state for a "Snake Escape" 
 * game, including a 2d grid of cells and a list of snakes.
 */
public class SnakeBasket
{
  /**
   * History of previous moves, as a list of descriptors
   */
  private ArrayList<String[]> history = new ArrayList<>();
  
  /**
   * String representation of the moves of this game.  Each
   * move is represented by a four-character string consisting of
   * <ul>
   *   <li>the snake's id
   *   <li>'h' or 't', depending on whether the head or tail was moved
   *   <li>'L', 'R', 'U', 'D', depending on the direction
   *   <li>a comma
   * </ul>
   */
  private String moveList = "";
  
  /**
   * The grid cells.
   */
  private BasketCell[][] grid; 
  
  /**
   * The list of snakes.
   */
  private ArrayList<Snake> snakes;
  
  /**
   * The currently grabbed snake, or null if there isn't one.
   */
  private Snake current = null;
  
  /**
   * True if the currently grabbed snake was grabbed at the
   * head.
   */
  private boolean currentIsHead = false;
  
  /**
   * True if the game is over.
   */
  private boolean over;
  
  /**
   * Count of moves.
   */
  private int moves;
  
  /**
   * Constructs an instance of this game from the given string
   * array.
   * @param desc
   *    array of strings representing the initial grid layout,
   *    including ids and placement of snakes
   */
  public SnakeBasket(String[] desc)
  {
    grid = GridUtil.createGridFromDescriptor(desc);
    snakes = SnakeUtil.findSnakes(desc);
    resetAllSnakes();
    history.add(GridUtil.makeDescriptor(this));
  }
  
  /**
   * Constructs an instance of this game from the given string
   * array and list of snakes.  <em>Snake information, if any,
   * in the given string array is ignored</em>.
   * @param desc
   *    array of strings representing the initial grid layout
   * @param givenSnakes
   *    array of snakes
   */
  public SnakeBasket(String[] desc, ArrayList<Snake> givenSnakes)
  {
    grid = GridUtil.createGridFromDescriptor(desc);
    snakes = givenSnakes;
    resetAllSnakes();
    history.add(GridUtil.makeDescriptor(this));
  }
  
  /**
   * Returns the state of the game as a signature (a multi-line 
   * string (similar to the descriptor, but as a single string 
   * with rows separated by newlines).
   * @return
   *   representation of the grid as a single string
   */
  public String getSignature()
  {
    return makeMultiString(GridUtil.makeDescriptor(this));
  }
  
  /**
   * Returns a list of signatures, each representing
   * a previous state of the grid, in order.
   * @return
   *   list of strings representing previous game states
   */
  public ArrayList<String> getHistory()
  {
    ArrayList<String> result = new ArrayList<>();
    for (String[] desc : history)
    {
      result.add(makeMultiString(desc));
    }
    return result;
  }
  
  /**
   * Returns a string representation of the moves of this game.  Each
   * move is represented by a four-character string consisting of
   * <ul>
   *   <li>the snake's id
   *   <li>'h' or 't', depending on whether the head or tail was moved
   *   <li>'L', 'R', 'U', 'D', depending on the direction
   *   <li>a comma
   * </ul>
   */
  public String getMoveList()
  {
    return moveList;
  }
  
  /**
   * Returns the grid cell at the given row and column.
   * @param row
   *    given row
   * @param col
   *    given column
   * @return
   *    grid cell at the given row and column
   */
  public BasketCell getCell(int row, int col)
  {
    return grid[row][col];
  }
  
  /**
   * Returns the number of rows in this game.
   * @return
   *   number of rows
   */
  public int getRows()
  {
    return grid.length;
  }
  
  /**
   * Returns the number of columns in this game.
   * @return
   *   number of columns
   */
  public int getCols()
  {
    return grid[0].length;
  } 
  
  /**
   * Returns the currently grabbed snake, if any.  Returns
   * null if there is no current snake.
   * @return
   *   current snake, if any
   */
  public Snake currentSnake()
  {
    return current;
  }
  
  /**
   * Returns true if there is a current snake and it was 
   * grabbed at the head end, false if it was grabbed by
   * the tail.
   * @return
   *   true if current snake was grabbed by the head
   */
  public boolean currentWasGrabbedByHead()
  {
    return currentIsHead;
  }
  
  /**
   * Returns a list of all snakes.
   * @return
   *   list of all snakes
   */
  public ArrayList<Snake> getAllSnakes()
  {
    return snakes;
  }
  
  /**
   * Returns true if the green snake is in the 
   * exit cell, false otherwise.
   * @return
   *   true if green snake is in the exit
   */
  public boolean isOver()
  {
    return over;
  }
  
  /**
   * Returns the total number of moves that have been made so far
   * in this game.
   * @return
   *   number of moves
   */
  public int getMoves()
  {
    return moves;
  }
  
  
  /**
   * Attempts to grab a snake by the head or tail at the given position.
   * If there is not already a current snake, and if the given position 
   * contains a snake head or tail, that becomes the current snake. 
   * If this game already has a current snake selected, this method
   * does nothing. 
   * @param row
   *   given row at which to grab a snake
   * @param col
   *   given column at which to grab a snake
   */
  public void grabSnake(int row, int col)
  {
    if (current != null) return;
    
    BasketCell cell = grid[row][col];
    Snake s = cell.getSnake();
    if (s != null)
    {
      if (s.isHead(row, col) || s.isTail(row, col))
      {
        current = s;
        currentIsHead = s.isHead(row, col);
      }
    }
  }
  
  /**
   * Sets the current snake to null, if any.
   */
  public void releaseSnake()
  {
    current = null;
  }
  
  /**
   * Attempt to move the current snake (head or tail) to an adjacent
   * cell in the given direction.  If a move is possible, this method
   * updates the current snake, the move count, and the grid cells
   * (via resetAllSnakes).
   * <p>
   * It is only possible to move in the following cases:
   * <ul>
   *   <li>The adjacent cell is empty; then the snake (head or tail) moves 
   *       into the cell
   *   <li>The adjacent cell is the exit and the current snake is the green one;
   *       then the snake (head or tail) moves into the exit and the game ends
   *   <li>The current snake was grabbed by the head, and the adjacent cell
   *       is row/column of the current snake's tail; then the snake (head) moves
   *       into the cell
   *   <li>The current snake was grabbed by the tail, and the adjacent cell
   *       is row/column of the current snake's head; then the snake (tail)
   *       moves into the cell
   *   <li>The current snake was grabbed by the head and the adjacent cell
   *   is an apple; then the apple is removed and the snake (head) moves 
   *   into the cell, increasing its size by one piece
   *   <li>The current snake was grabbed by the head, has at least three pieces,
   *    and the adjacent cell is a mushroom; then the mushroom is removed and
   *    the snake (head) moves into the cell, reducing its size by one piece
   * </ul>
   * If none of the above conditions is met, this method does nothing.
   * If any of the conditions is met and a move occurs, the move count
   * is increased by 1.
   * If there is no currently grabbed snake, this method does nothing.
   * @param dir
   *   Direction in which to attempt to move
   */
  public void move(Direction dir)
  {

    if (current == null || over) return;
    char id = current.getId();
    char direction = dir.toString().charAt(0);
    String headOrTail;
    int row, col;
    if (currentIsHead)
    {
      row = current.getHead().row();
      col = current.getHead().col();
      headOrTail = "h";
    }
    else
    {
      row = current.getTail().row();
      col = current.getTail().col();
      headOrTail = "t";
    }
    String moveString = "" + id + headOrTail + direction;
    
    switch (dir)
    {
      case LEFT:
        col -= 1;
        break;
      case RIGHT:
        col += 1;
        break;
      case UP:
        row -= 1;
        break;
      case DOWN:
        row += 1;
        break;
    }

    if (row >= 0 && row < getRows() && col >= 0 && col < getCols())
    {
      // can we move there? Has to be empty, or our own head or tail, 
      // or it's the exit and we are the green snake
      BasketCell newCell = grid[row][col];
      if (newCell.isEmpty() || 
          (newCell.isExit() && current.isGreen()) ||
          (currentIsHead && current.getTail().row() == row && current.getTail().col() == col) ||
          (!currentIsHead && current.getHead().row() == row && current.getHead().col() == col))
      {
        if (currentIsHead)
        {
          current.moveHead(dir);
        }
        else
        {
          current.moveTail(dir);         
        }
        resetAllSnakes();
        moves += 1;
        recordMove(moveString);
        
        if (newCell.isExit() && current.isGreen())
        {
          over = true;
        }
      }
      else if (currentIsHead && newCell.isApple() || 
               currentIsHead && newCell.isMushroom() && current.getPieces().size() > 2)
      {
        if (newCell.isApple())
        {
          current.moveHeadAndGrow(dir);
        }
        else
        {
          current.moveHeadAndShrink(dir);
        }
        newCell.clearFood();
        resetAllSnakes();
        moves += 1;
        recordMove(moveString);
      }
    }   
  }

  
  
  /**
   * Clears all snake information from the grid, if any, and then
   * sets it from the current list of snakes.  After executing 
   * this method, the information in the grid cells and the
   * information in the snake list should be fully consistent.
   */
  public void resetAllSnakes()
  {
    for (int row = 0; row < getRows(); ++row)
    {
      for (int col = 0; col < getCols(); ++col)
      {      
         grid[row][col].clearSnake();
      }
    }
    for (Snake s : snakes)
    {
      setOneSnake(s);
    }
  }
  
  /**
   * Helper method to place a snake in the grid.
   * @param s
   */
  private void setOneSnake(Snake s)
  {
    ArrayList<SnakePiece> pieces = s.getPieces();
    for (int i = 0; i < pieces.size(); ++i)
    {
      SnakePiece p = pieces.get(i);
      int row = p.row();
      int col = p.col();
      if (grid[row][col].isEmpty() || grid[row][col].isExit())
      {
        grid[row][col].setSnake(s);
      }
    }
  }
  
  /**
   * Helper method updates the move list and the history.
   * @param moveString
   *   three-character string describing an individual
   *   move as documented in getMoveList
   */
  private void recordMove(String moveString)
  {
    moveList += moveString + ",";
    history.add(GridUtil.makeDescriptor(this));
  }
  
  /**
   * Undoes the previous move.
   */
  public void undoMove()
  {
    if (history.size() <= 1)
    {
      // nothing to undo
      return;
    }
    history.remove(history.size() - 1);
    moveList = moveList.substring(0, moveList.length() - 4);
    String[] desc = history.get(history.size() - 1);
    grid = GridUtil.createGridFromDescriptor(desc);
    
    // update the snakes
    ArrayList<Snake> temp = SnakeUtil.findSnakes(desc);
    for (int i = 0; i < snakes.size(); ++i)
    {
      Snake s = snakes.get(i);
      char id = s.getId();
      for (Snake t : temp)
      {
        if (t.getId() == id)
        {
          s.getPieces().clear();
          for (SnakePiece sp : t.getPieces())
          {
            s.getPieces().add(sp);
          }
        }
      }
    }
    
    resetAllSnakes();
    moves -= 1;
    current = null;
    over = false; 
  }

  /**
   * Converts a descriptor to a multiline string, where the rows
   * are separated by a newline.
   * @param desc
   *   String array descriptor
   * @return
   *   multiline string 
   */
  private String makeMultiString(String[] desc)
  {
    StringBuilder sb = new StringBuilder();
    for (String s : desc)
    {
      sb.append(s + '\n');
    }
    return sb.toString();   
  }
}
