package hw3;
import java.util.ArrayList;
//import static Status;
import api.BasketCell;
import api.Direction;
import api.SnakeLayoutException;
import api.SnakePiece;
import api.GridUtil;

/**
 * This class represents the basic game state for a "Snake Escape" 
 * game, including a 2d grid of cells and a list of snakes.
 */
public class SnakeBasket
{
  /**
   * The 2D array of cells.
   */
  private BasketCell[][] grid; 
  
  /**
   * The list of snakes.
   */
  private ArrayList<Snake> snakes;
  
  // TODO: any other instance variables you need
  
  private Snake curSnake; //Snake variable to hold the current snake
  private int moves; //int var to hold moves made
  private boolean isHead; //boolean variable to hold if current snake is being held by head
  
  
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
    
    // TODO:
    //   implement resetAllSnakes
    //   any other initialization you need
    curSnake = null;
    moves = 0;
  }
  
  /**
   * Constructs an instance of this game from the given string
   * array.
   * @param desc
   *    array of strings representing the initial grid layout,
   *    including ids and placement of snakes
   */
  public SnakeBasket(String[] desc)
  {
    // TODO:
    //   implement SnakeUtil.findSnakes
    //   implement resetAllSnakes
    //   any other initialization you need
	grid = GridUtil.createGridFromDescriptor(desc);
	snakes = SnakeUtil.findSnakes(desc);
	resetAllSnakes();
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
	  if(curSnake == null) {
		  return null;
	  }
	  else {	  
		  return curSnake;
	  }
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
	  if(curSnake != null && isHead) {
		  return true;
	  }
    return false;
  }
  
  /**
   * Returns this SnakeBasket's list of all snakes. Normally this
   * method is used to easily render or display the game;
   * clients should not modify the list or the snakes.
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
//	  if(curSnake != null && curSnake.getId() == 'g' && grid[curSnake.getHead().row()][curSnake.getHead().col()].isExit()) {
//		  return true;
//	  }
//	  else if(curSnake != null && curSnake.getId() == 'g' && grid[curSnake.getTail().row()][curSnake.getTail().col()].isExit()) {
//		  return true;
//	  }
	  for(int i = 0; i < grid.length; i++) {
		  for(int j = 0; j < grid[i].length; j++) {
			  if(grid[i][j].isExit() && grid[i][j].getId() == 'g') {
				  return true;
			  }
		  }
	  }
    return false;
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
	  if(curSnake == null) {
		  for(int i = 0; i < snakes.size(); i++) {
			  Snake temp = snakes.get(i);
			  if(temp.isHead(row, col)) {
				  curSnake = temp;
				  isHead = true;
			  }
			  else if(temp.isTail(row, col)) {
				  curSnake = temp;
				  isHead = false;
			  }
		  }
	  }
	  else {
		  return;
	  }
  }
  
  /**
   * Sets the current snake to null, if any.
   */
  public void releaseSnake()
  {
	  curSnake = null;
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
	  if(isOver()) {
		  return;
	  }
	  if(dir == Direction.UP) {
		  if(isHead) {
			  if(checkAdjCell(curSnake.getHead().row() - 1, curSnake.getHead().col()) == false) {
				  return;
			  }
			  if(grid[curSnake.getHead().row() - 1][curSnake.getHead().col()].isMushroom()) {
				  grid[curSnake.getHead().row() - 1][curSnake.getHead().col()].clearFood();
				  curSnake.moveHeadAndShrink(dir);
			  }
			  else if(grid[curSnake.getHead().row() - 1][curSnake.getHead().col()].isApple()) {
				  grid[curSnake.getHead().row() - 1][curSnake.getHead().col()].clearFood();
				  curSnake.moveHeadAndGrow(dir);
			  }
			  else {
				  curSnake.moveHead(dir);
			  }
		  }
		  else {
			  if(checkAdjCell(curSnake.getTail().row() - 1, curSnake.getTail().col()) == false ||  
					  grid[curSnake.getTail().row() - 1][curSnake.getTail().col()].isMushroom() ||
					  grid[curSnake.getTail().row() - 1][curSnake.getTail().col()].isApple()) {
				  return;
			  }
			  else {
				  curSnake.moveTail(dir);
			  }
		  }
	  }
	  else if(dir == Direction.DOWN) {
		  if(isHead) {
			  if(checkAdjCell(curSnake.getHead().row() + 1, curSnake.getHead().col()) == false) {
				  return;
			  }
			  if(grid[curSnake.getHead().row() + 1][curSnake.getHead().col()].isMushroom()) {
				  grid[curSnake.getHead().row() + 1][curSnake.getHead().col()].clearFood();
				  curSnake.moveHeadAndShrink(dir);
			  }
			  else if(grid[curSnake.getHead().row() + 1][curSnake.getHead().col()].isApple()) {
				  grid[curSnake.getHead().row() + 1][curSnake.getHead().col()].clearFood();
				  curSnake.moveHeadAndGrow(dir);
			  }
			  else {
				  curSnake.moveHead(dir);
			  } 
		  }
		  else { 
			  if(checkAdjCell(curSnake.getTail().row() + 1, curSnake.getTail().col()) == false || 
					  grid[curSnake.getTail().row() + 1][curSnake.getTail().col()].isMushroom() ||
					  grid[curSnake.getTail().row() + 1][curSnake.getTail().col()].isApple()) {
				  return;
			  }
			  else {
				  curSnake.moveTail(dir);
			  }  
		  }
	  }
	  else if(dir == Direction.LEFT) {
		  if(isHead) {
			  if(checkAdjCell(curSnake.getHead().row(), curSnake.getHead().col() - 1) == false) {
				  return;
			  }
			  if(grid[curSnake.getHead().row()][curSnake.getHead().col() - 1].isMushroom()) {
				  grid[curSnake.getHead().row()][curSnake.getHead().col() - 1].clearFood();
				  curSnake.moveHeadAndShrink(dir);
			  }
			  else if(grid[curSnake.getHead().row()][curSnake.getHead().col() - 1].isApple()) {
				  grid[curSnake.getHead().row()][curSnake.getHead().col() - 1].clearFood();
				  curSnake.moveHeadAndGrow(dir);
			  }
			  else {
				  curSnake.moveHead(dir);
			  }
		  }
		  else {
			  if(checkAdjCell(curSnake.getTail().row(), curSnake.getTail().col() - 1) == false ||
					  grid[curSnake.getTail().row()][curSnake.getTail().col() - 1].isMushroom() ||
					  grid[curSnake.getTail().row()][curSnake.getTail().col() - 1].isApple()) {
				  return;
			  }
			  else {
				  curSnake.moveTail(dir);
			  }
		  }
	  }
	  else if(dir == Direction.RIGHT) {
		  if(isHead) {
			  if(checkAdjCell(curSnake.getHead().row(), curSnake.getHead().col() + 1) == false) {
				  return;
			  }
			  if(grid[curSnake.getHead().row()][curSnake.getHead().col() + 1].isMushroom()) {
				  grid[curSnake.getHead().row()][curSnake.getHead().col() + 1].clearFood();
				  
			  }
			  else if(grid[curSnake.getHead().row()][curSnake.getHead().col() + 1].isApple()) {
				  grid[curSnake.getHead().row()][curSnake.getHead().col() + 1].clearFood();
				  curSnake.moveHeadAndGrow(dir);
			  }
			  else {
				  curSnake.moveHead(dir);
			  }
		  }
		  else {
			  if(checkAdjCell(curSnake.getTail().row(), curSnake.getTail().col() + 1) == false ||
					  grid[curSnake.getTail().row()][curSnake.getTail().col() + 1].isMushroom() ||
					  grid[curSnake.getTail().row()][curSnake.getTail().col() + 1].isApple()) {
				  return;
			  }
			  else {
				  curSnake.moveTail(dir);
			  }
		  }
	  }
	  resetAllSnakes(); //call to reset snakes to new position
	  moves++; //increment moves
  }
  
  private boolean checkAdjCell(int row, int col) { //helper method to check the adjacent cell to make sure move can be made
	  if(grid[row][col].isEmpty()) {
		  return true;
	  }
	  else if(grid[row][col].isWall()) {
		  return false;
	  }
	  else if(grid[row][col].hasSnake()) {
		  SnakePiece temp = new SnakePiece(row, col);
		  if(temp.equals(curSnake.getHead()) || temp.equals(curSnake.getTail())) {
			  return true;
		  }
		  return false;
	  }
	  else {
		  return true;
	  }
  }
  
  /**
   * Clears all snake information from the grid, if any, and then
   * sets it from the current list of snakes.  After executing 
   * this method, the information in the grid cells and the
   * information in the snake list should be fully consistent.
   */
  public void resetAllSnakes() throws SnakeLayoutException
  {
	  for(int i = 0; i < grid.length; i++) { //loop to just clear snake pieces from grid
		  for(int j = 0; j < grid[i].length; j++) {
			  if(grid[i][j].hasSnake()) {
				  grid[i][j].clearSnake();
			  }
		  }
	  }
	  
	  for(int i = 0; i < snakes.size(); i++) { //loop to add snake pieces to grid in new location
		  ArrayList<SnakePiece> pieces = snakes.get(i).getPieces();
		  for(int j = 0; j < pieces.size(); j++) {
			  SnakePiece piece = pieces.get(j);
			  grid[piece.row()][piece.col()].setSnake(snakes.get(i));
		  }
	  }
  }
}