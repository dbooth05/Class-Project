package api;

/**
 * Immutable representation of a row and column position
 * for one piece of a Snake.
 * @author smkautz
 */
public class SnakePiece
{
  /**
   * Row of this piece.
   */
  private final int row;
  
  /**
   * Column of this piece.
   */
  private final int col;
  
  /**
   * Constructs a piece with the given row and column.
   * @param givenRow
   *   for for this piece
   * @param givenColumn
   *   column for this piece
   */
  public SnakePiece(int givenRow, int givenColumn)
  {
    row = givenRow;
    col = givenColumn;
  }
  
  /**
   * Returns the row.
   * @return
   *   row for this piece
   */
  public int row()
  {
    return row;
  }
  
  /**
   * Returns the column.
   * @return
   *   column for this piece
   */
  public int col()
  {
    return col;
  }
  
  /**
   * Returns a string representation of this piece
   * in the form "(row, col)".
   * @return
   *   string representation of this piece
   */
  @Override
  public String toString()
  {
    return "(" + row + ", " + col + ")";
  }
  
  /**
   * Returns true if the given object is a SnakePiece and
   * has the same row and column as this piece.
   * @param other
   *   any object
   * @return
   *   true if the given object is a SnakePiece and
   * has the same row and column
   */
  @Override
  public boolean equals(Object other)
  {
    if (other == null || other.getClass() != this.getClass())
    {
      return false;
    }
    if (this == other)
    {
      return true;
    }
    SnakePiece p = (SnakePiece) other;
    return p.row == row && p.col == col;
  }
}
