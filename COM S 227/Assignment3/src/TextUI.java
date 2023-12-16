import java.util.ArrayList;
import java.util.Scanner;

import api.Direction;
import api.GridUtil;
import hw3.SnakeBasket;
import hw3.Snake;

/**
 * Text based user interface for the Snake Escape game.
 * @author smkautz
 */
public class TextUI
{
  
  public static final String[] test1 = {
  "#  #  #  E  #  #  #",
  "#  y4 y3 y2 y1 y0 #",
  "#  .  #  #  #  .  #",
  "#  .  .  .  .  .  #",
  "#  #  #  g0 #  #  #",
  "#  #  #  g1 #  #  #",
  "#  #  #  g2 #  #  #",
  "#  #  #  g3 #  #  #",
  "#  #  #  #  #  #  #"
  };
  
  // same as above, but has a few apples and mushrooms to eat
  public static final String[] test2 = {
  "#  #  #  E  #  #  #",
  "#  y4 y3 y2 y1 y0 #",
  "#  @  #  #  #  @  #",
  "#  .  $  .  $  .  #",
  "#  #  #  g0 #  #  #",
  "#  #  #  g1 #  #  #",
  "#  #  #  g2 #  #  #",
  "#  #  #  g3 #  #  #",
  "#  #  #  #  #  #  #"
  };

  public static void main(String[] args)
  {
    // construct the snakes corresponding to the test1 descriptor above,
    // since we don't necessarily have SnakeUtil.findSnakes implemented yet...
    ArrayList<Snake> snakes = new ArrayList<>();
    Snake s = new Snake('g');
    s.addPiece(4, 3); // head
    s.addPiece(5, 3);
    s.addPiece(6, 3);
    s.addPiece(7, 3); // tail
    snakes.add(s);
    s = new Snake('y');
    s.addPiece(1, 5); // head
    s.addPiece(1, 4);
    s.addPiece(1, 3);
    s.addPiece(1, 2);
    s.addPiece(1, 1); // tail
    snakes.add(s);
    
    // the two-argument SnakeBasket constructor will ignore
    // the snake information in the string array, and instead use the snake list
    // we made above
    SnakeBasket b = new SnakeBasket(test1, snakes);
    
    // print out the game...
    GridUtil.display(b);
    
    // loop until the game is over...
    Scanner sc = new Scanner(System.in);
    while (!b.isOver())
    {
      // select a snake head or tail
      doSelect(sc, b);
      
      // move it left/right/up/down until released
      while(b.currentSnake() != null && !b.isOver())
      {
        doMove(sc, b);
        GridUtil.display(b);
      }
    }
    System.out.println("Done in " + b.getMoves() + " moves");
  }
  
  
  private static void doSelect(Scanner sc, SnakeBasket game)
  {
    System.out.print("Select snake head or tail (row col):");
    int row = sc.nextInt();
    int col = sc.nextInt();
    game.grabSnake(row, col);
  }


  private static void doMove(Scanner sc, SnakeBasket game)
  {
    System.out.print("Enter direction {l, r, u, d} or any other key to release: ");
    String d = sc.next();
    char c = d.charAt(0);
    Direction dir = null;
    switch (c)
    {
      case 'l': dir = Direction.LEFT; break;
      case 'r': dir = Direction.RIGHT; break;
      case 'u': dir = Direction.UP; break;
      case 'd': dir = Direction.DOWN; break;
    }
    if (dir == null)
    {
      game.releaseSnake();
    }
    else
    {
      game.move(dir);
    }
  }
}
