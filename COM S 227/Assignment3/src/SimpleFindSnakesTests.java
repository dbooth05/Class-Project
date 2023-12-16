import java.util.ArrayList;

import hw3.Snake;
import hw3.SnakeUtil;

public class SimpleFindSnakesTests
{
  public static void main(String[] args)
  {
    String[] test1 = {
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
    
    ArrayList<Snake> snakes = SnakeUtil.findSnakes(test1);
    System.out.println(snakes.size()); // expected 2
    for (Snake s : snakes)
    {
      System.out.println(s.getId());
      System.out.println(s.getPieces());
    }
    
    // expected one snake with id 'y' and pieces
    // [(1, 5), (1, 4), (1, 3), (1, 2), (1, 1)]
    // expected one snake with id 'g' and pieces
    // [(4, 3), (5, 3), (6, 3), (7, 3)]
    //
    // could occur in either order

  }
}
