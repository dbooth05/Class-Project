package hw3;
import java.util.ArrayList;
import static api.Direction.*;
import api.Direction;
import api.SnakePiece;

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
   * Constructs a Snake with an empty list of SnakePiece objects
   * and with the given character as its ID.
   * @param givenId
   *   ID to use for this Snake
   */
	
	private char id; //char to hold ID of snake
	private ArrayList<SnakePiece> arr; //array of snakepieces for the snake
	private SnakePiece temp; //temp snakepiece for instance uses
	
  public Snake(char givenId)
  {
	  id = givenId;
	  arr = new ArrayList<SnakePiece> ();
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
	  temp = new SnakePiece(row, col);
	  arr.add(temp);
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
	  while(arr.size() <= index) {
		  arr.add(null);
	  }
	  temp = new SnakePiece(row, col);
	  arr.set(index, temp);
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
    return arr.get(0);
  }
  
  /**
   * Returns the last piece in this Snake's list of pieces, or 
   * null if this snake has no pieces.
   * @return
   *   last piece
   */
  public SnakePiece getTail()
  {
    return arr.get(arr.size() - 1);
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
	  temp = new SnakePiece(row, col);
	  return temp.equals(arr.get(0));
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
	 temp = new SnakePiece(row, col); 
	 return temp.equals(arr.get(arr.size() - 1));
  }
   
  /**
   * Returns this Snake's list of pieces. Normally this
   * method is used to render or display the game; clients
   * should not directly modify the snakes through this method.
   * @return
   *   list of SnakePiece objects for this Snake
   */
  public ArrayList<SnakePiece> getPieces()
  {
    return arr;
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
	  temp = arr.get(0);
	  if(arr.size() < 2) {
		  return;
	  }
	  else if(dir == UP) {
		  temp = new SnakePiece(temp.row() - 1, temp.col()); 
	  }
	  else if(dir == DOWN) {
		  temp = new SnakePiece(temp.row() + 1, temp.col());
	  }
	  else if(dir == LEFT) {
		  temp = new SnakePiece(temp.row(), temp.col() - 1);
	  }
	  else if(dir == RIGHT) {
		  temp = new SnakePiece(temp.row(), temp.col() + 1);
	  }
	  arr.add(0, temp);
	  arr.remove(arr.size() - 1);
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
	  temp = arr.get(arr.size() - 1);
	  if(arr.size() < 2) {
		  return;
	  }
	  else if(dir == UP) {
		  temp = new SnakePiece(temp.row() - 1, temp.col());
		  arr.add(temp);
		  arr.remove(0);
	  }
	  else if(dir == DOWN) {
		  temp = new SnakePiece(temp.row() + 1, temp.col());
		  arr.add(temp);
		  arr.remove(0);
	  }
	  else if(dir == LEFT) {
		  temp = new SnakePiece(temp.row(), temp.col() - 1);
		  arr.add(temp);
		  arr.remove(0);
	  }
	  else if(dir == RIGHT) {
		  temp = new SnakePiece(temp.row(), temp.col() + 1);
		  arr.add(temp);
		  arr.remove(0);
	  }
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
	  if(arr.size() < 2) {
		  return;
	  }
	  else {
		  temp = arr.get(0);
		  if(dir == UP) {
			  temp = new SnakePiece(temp.row() - 1, temp.col());
			  arr.add(0, temp);
		  }
		  else if(dir == DOWN) {
			  temp = new SnakePiece(temp.row() + 1, temp.col());
			  arr.add(0, temp);
		  }
		  else if(dir == LEFT) {
			  temp = new SnakePiece(temp.row(), temp.col() - 1);
			  arr.add(0, temp);
		  }
		  else if(dir == RIGHT) {
			  temp = new SnakePiece(temp.row(), temp.col() + 1);
			  arr.add(0, temp);
		  }
	  }
  }
  
  /**
   * Moves the head of this Snake in the given direction, decreasing
   * its length by 1.  Does nothing if this Snake fewer than three pieces.
   * @param dir
   *   which direction
   */
  public void moveHeadAndShrink(Direction dir)
  {
	  if(arr.size() < 3) {
		  return;
	  }
	  else {
		  temp = arr.get(0);
		  if(dir == UP) {
			  temp = new SnakePiece(temp.row() - 1, temp.col());
			  arr.add(0, temp);
			  arr.remove(arr.size() - 1);
			  arr.remove(arr.size() - 1);
		  }
		  else if(dir == DOWN) {
			  temp = new SnakePiece(temp.row() + 1, temp.col());
			  arr.add(0, temp);
			  arr.remove(arr.size() - 1);
			  arr.remove(arr.size() - 1);
		  }
		  else if(dir == LEFT) {
			  temp = new SnakePiece(temp.row(), temp.col() - 1);
			  arr.add(0, temp);
			  arr.remove(arr.size() - 1);
			  arr.remove(arr.size() - 1);
		  }
		  else if(dir == RIGHT) {
			  temp = new SnakePiece(temp.row(), temp.col() + 1);
			  arr.add(0, temp);
			  arr.remove(arr.size() - 1);
			  arr.remove(arr.size() - 1);
		  }
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
	  if(arr.size() > 2 && checkNull(arr) && checkDup(arr) && checkAdj(arr)) {
		  return true;
	  }
    return false;
  }
  
  private static boolean checkAdj(ArrayList<SnakePiece> arr) { //helper method to check adj spots
	  for(int i = 1; i < arr.size(); i++) {
		  SnakePiece temp = arr.get(i);
		  SnakePiece temp1 = arr.get(i - 1);
		  int tempRow = temp.row();
		  int tempCol = temp.col();
		  int tempRow1 = temp1.row();
		  int tempCol1 = temp1.col();
		  
		  if((tempRow == tempRow1 && (tempCol == tempCol1 - 1 || tempCol == tempCol1 + 1))) {
			  continue;
		  }
		  else if((tempCol == tempCol1 && (tempRow == tempRow1 - 1 || tempRow == tempRow1 + 1))) {
			  continue;
		  }
		  else {
			  return false;
		  }
	  }
	  return true;  
  }
  
  private static boolean checkDup(ArrayList<SnakePiece> arr) { //helper method to check for duplicate coords in array
	  for(int i = 1; i < arr.size(); i++) {
		  if(arr.get(i) == arr.get(i - 1)) {
			  return false;
		  }
	  }
	  return true;
  }
  
  private static boolean checkNull(ArrayList<SnakePiece> arr) { //helper method to check for null in ArrayList
	  for(int i = 0; i < arr.size(); i++) {
		  if(arr.get(i) == null) {
			  return false;
		  }
	  }
	  return true;
  }
  
}
