import mini3.SnakeSolver;
import snakes.api.GridUtil;
import snakes.hw3.SnakeBasket;

public class SolverTest
{
  
  public static void main(String[] args)
  {
    SnakeBasket game = new SnakeBasket(GridUtil.test0);
    //SnakeBasket game = new SnakeBasket(GridUtil.test1);
    SnakeSolver solver = new SnakeSolver(20);
    solver.solve(game);
    
    // find shortest solution in the list?
    String minSolution = solver.getSolutions().get(0);
    for (String solution : solver.getSolutions())
    {
      if (solution.length() < minSolution.length())
      {
        minSolution = solution;
      }
    }
    System.out.println("Number of moves: " +  minSolution.length() / 4);
    System.out.println(minSolution);
  }
}
