package mini3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import snakes.api.Direction;
import snakes.api.GridUtil;
import snakes.api.SnakePiece;
import snakes.hw3.Snake;
import snakes.hw3.SnakeBasket;

/**
 * Class that manages the state information during a recursive
 * search for solutions to the SnakeEscape game.
 */
public class SnakeSolver
{
  /**
   * Maximum number of moves allowed in the search.
   */
  private int maxMoves;
  
  /**
   * Associates a signature with the move count to reach 
   * that grid layout.
   */
  private Map<String, Integer> seen = new HashMap<String, Integer>();
  
  /**
   * All solutions found in this search.
   */
  private ArrayList<String> solutions = new ArrayList<>();
  
  /**
   * Constructs a solver with the given maximum number of moves.
   * @param givenMaxMoves
   *   maximum number of moves
   */
  public SnakeSolver(int givenMaxMoves)
  {
    maxMoves = givenMaxMoves;
  }
  
  /**
   * Returns all solutions found in the search.  Each solution 
   * is a single string in the form returned by SnakeBasket#getMoveList.
   * @return
   *   list of all solutions
   */
  public ArrayList<String> getSolutions()
  {
    return solutions;
  }
  
  /**
   * Recursively search for solutions to the given game instance
   * according to the algorithm described in the miniassignment 3
   * pdf.
   * @param game
   *   any instance of SnakeBasket
   */
  public void solve(SnakeBasket game)
  {
    // TODO
	  if(game.getMoves() > maxMoves) {
		  return;
	  }
	  else if(game.isOver()) {
		  solutions.add(game.getMoveList());
		  return;
	  }
	  else if(seen.size() >= 0 && seen.containsKey(game.getSignature())) {
		  if(seen.get(game.getSignature()) > game.getMoves()) {
			  seen.put(game.getSignature(), game.getMoves());
		  }
		  else {
			  return;
		  }
	  }
	  else {
		  seen.put(game.getSignature(), game.getMoves());
		  for(int i = 0; i < game.getAllSnakes().size(); i++) {
			  ArrayList<Snake> snakes = game.getAllSnakes();
			  game.grabSnake(snakes.get(i).getHead().row(), snakes.get(i).getHead().col());
			  game.move(Direction.LEFT);
			  game.move(Direction.RIGHT);
			  game.move(Direction.UP);
			  game.move(Direction.DOWN);
			  game.grabSnake(snakes.get(i).getTail().row(), snakes.get(i).getTail().col());
			  game.move(Direction.LEFT);
			  game.move(Direction.RIGHT);
			  game.move(Direction.UP);
			  game.move(Direction.DOWN);
			  game.releaseSnake();
			  solve(game);
		  }
	  }
  }
}



















