package snakes.hw3;
import java.util.ArrayList;

import snakes.api.Direction;
import snakes.api.SnakePiece;

/**
 * A Snake is a sequence of (row, column) pairs in which each 
 * (row, column) in the sequence represents a 2d position that is
 * horizontally or vertically adjacent to the previous one.
 * Each (row, column) is represented by an instance of the
 * class SnakePiece.  The first piece in the sequence
 * is called the "head", and the last one is called the
 * "tail".  In addition to the list of SnakePiece objects,
 * a Snake has an id consisting of a single character.
 * <p>
 * A snake may or may not be <em>valid</em>.  More precisely,
 * a snake is defined to be valid if:
 * <ul>
 *   <li>It has at least two pieces
 *   <li>There are no null elements in its pieces list
 *   <li>There are no duplicates in its pieces list
 *   <li>Each piece in the list has a row and column
 *   position that is horizontally or vertically adjacent
 *   to the previous piece in the list
 * </ul>
 * The methods to add pieces to a snake do 
 * NOT do any error-checking.  Instead, clients can use the method 
 * isValid to check whether the snake is valid.
 */
public class Snake
{
  /**
   * The pieces for this snake.
   */
  private ArrayList<SnakePiece> pieces;
  
  /**
   * The id for this snake.
   */
  private char id;
  
  /**
   * Constructs a Snake with an empty list of SnakePiece objects
   * and with the given character as its ID.
   * @param givenId
   *   ID to use for this Snake
   */
  public Snake(char givenId)
  {
    pieces = new ArrayList<>();
    id = givenId;
  }
  
  /**
   * Adds a new SnakePiece to the end of  this Snake's list of pieces.  This 
   * method does no error-checking to ensure the given position is actually 
   * adjacent to the current tail.
   * @param row
   *   row for the new SnakePiece
   * @param col
   *   column for the new SnakePiece
   */
  public void addPiece(int row, int col)
  {  
    pieces.add(new SnakePiece(row, col));
  }
  
  /**
   * Sets this Snake's list of pieces at the given index to be a new
   * SnakePiece with the given row and column.  If the current list 
   * of pieces is shorter than the given index, it is padded with
   * nulls so the given index can be set. This method does no 
   * error-checking to ensure the given (row, column) is actually 
   * adjacent to its neighbors in the list of pieces.
   * @param index
   *   index in the list of pieces where the new SnakePiece will be set
   * @param row
   *   row for the new SnakePiece
   * @param col
   *   column for the new SnakePiece
   */
  public void addPiece(int index, int row, int col)
  {  
    while(index >= pieces.size())
    {
      pieces.add(null);
    }
    // now index < size
    pieces.set(index, new SnakePiece(row, col));
  }
  
  /**
   * Returns the ID for this Snake.
   * @return
   *   ID for this Snake
   */
  public char getId()
  {
    return id;
  }
  
  /**
   * Returns true if the ID is one of the characters 'g' or 'G'.
   * @return
   *   true if ID is 'g' or 'G'
   */
  public boolean isGreen()
  {
    return id == 'g' || id == 'G';
  }
  
  /**
   * Returns the first piece in this Snake's list of pieces,
   * or null if this snake has no pieces.
   * @return
   *   first piece
   */
  public SnakePiece getHead()
  {
    if (pieces.size() == 0) return null;    
    return pieces.get(0);
  }
  
  /**
   * Returns the last piece in this Snake's list of pieces, or 
   * null if this snake has no pieces.
   * @return
   *   last piece
   */
  public SnakePiece getTail()
  {
    if (pieces.size() == 0) return null;    
    return pieces.get(pieces.size() - 1);
  }
  
  /**
   * Returns true if given row and column match the row and column 
   * of this Snake's first piece.
   * Returns false if this snake has no pieces.
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   true if the head of this Snake has the same row and column
   */
  public boolean isHead(int row, int col)
  {
    if (pieces.size() == 0) return false;
    return row == getHead().row() && col == getHead().col();
  }
  
  /**
   * Returns true if given row and column match the row and column 
   * of this Snake's last piece.  Returns false if this snake has no pieces.
   * 
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   true if the tail of this Snake has the same row and column
   */
  public boolean isTail(int row, int col)
  {
    if (pieces.size() == 0) return false;
    return row == getTail().row() && col == getTail().col();
  }
  
  /**
   * Returns a list of all this Snake's pieces.
   * @return
   *   list of SnakePiece objects for this Snake
   */
  public ArrayList<SnakePiece> getPieces()
  {
    return pieces;
  }
  
  /**
   * Moves the head of this Snake in the given direction without
   * changing its length. Does nothing if the snake has fewer than
   * two pieces.
   * @param dir
   *   which direction
   */
  public void moveHead(Direction dir)
  {
    move(dir, true, false, false);
  }
  
  /**
   * Moves the tail of this Snake in the given direction without
   * changing its length. Does nothing if the snake has fewer than
   * two pieces.
   * @param dir
   *   which direction
   */
  public void moveTail(Direction dir)
  {
    move(dir, false, false, false);
  }
  
  /**
   * Moves the head of this Snake in the given direction, increasing
   * its length by 1. Does nothing if the snake has fewer than
   * two pieces.
   * @param dir
   *   which direction
   */
  public void moveHeadAndGrow(Direction dir)
  {
    move(dir, true, true, false);
  }
  
  /**
   * Moves the head of this Snake in the given direction, decreasing
   * its length by 1.  Does nothing if this Snake fewer than three pieces.
   * @param dir
   *   which direction
   */
  public void moveHeadAndShrink(Direction dir)
  {
    if (pieces.size() <= 2) return;
    move(dir, true, false, true);
  }
  
  /**
   * Helper method for the move methods.
   * @param dir
   *    which direction
   * @param isHead
   *    true if we are moving the head end
   * @param grow
   *    true if the snake should grow when moved
   * @param shrink
   *    true if the snake should shrink when moved
   */
  private void move(Direction dir, boolean isHead, boolean grow, boolean shrink)
  {   
    SnakePiece currentHeadOrTail;
    if (isHead)
    {
      currentHeadOrTail = pieces.get(0);
    }
    else
    {
      currentHeadOrTail = pieces.get(pieces.size() - 1);
    }
    
    int row = currentHeadOrTail.row();
    int col = currentHeadOrTail.col();
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
    SnakePiece newHeadHeadOrTail = new SnakePiece(row, col);
    if (isHead)
    {
      pieces.add(0, newHeadHeadOrTail);
      if (!grow)
      {
        pieces.remove(pieces.size() - 1);
      }
      if (shrink)
      {
        pieces.remove(pieces.size() - 1);
      }
    }
    else
    {
      pieces.add(newHeadHeadOrTail);
      pieces.remove(0);
    }
  }
  
  
  /**
   * Determines whether this snake is valid.  A snake is
   * <em>valid</em> if 
   * <ul>
   *   <li>It has at least two pieces
   *   <li>There are no null elements in its pieces list
   *   <li>There are no duplicates in its pieces list
   *   <li>Each piece in the list has a row and column
   *   position that is horizontally or vertically adjacent
   *   to the previous piece in the list
   * </ul>
   * @return
   *   true if this snake is valid, false otherwise
   */
  public boolean isValid()
  {
    // make sure there are no nulls
    for (SnakePiece p : pieces)
    {
      if (p == null)
      {
        return false;
      }
    }
    
    // make sure there are no duplicates
    ArrayList<SnakePiece> temp = new ArrayList<>();
    for (SnakePiece p : pieces)
    {
      if (!temp.contains(p))
      {
        temp.add(p);
      }
      else
      {
        return false;
      }
    }
    
    // length at least 2
    if (pieces.size() < 2)
    {
      return false;
    }
    
    // consecutive pieces are adjacent
    int curRow = getHead().row();
    int curCol = getHead().col();
    for (int i = 1; i < pieces.size(); ++i)
    {
      int nextRow = pieces.get(i).row();
      int nextCol = pieces.get(i).col();
      if (curRow == nextRow)
      {
        if (nextCol != curCol + 1 && nextCol != curCol - 1)
        {
           return false;
        }
      }
      else if (curCol == nextCol)
      {
        if (nextRow != curRow + 1 && nextRow != curRow - 1)
        {          
          return false;
        }        
      }
      else
      {
        return false;
      }
      curCol = nextCol;
      curRow = nextRow;
    }
    return true;
  }
  
}
