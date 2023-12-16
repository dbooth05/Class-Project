package snakes.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import snakes.hw3.Snake;
import snakes.hw3.SnakeBasket;



/**
 * Main class for a GUI for a Snake Escape game sets up a 
 * GamePanel instance in a frame.
 * 
 * EDIT THE create() METHOD TO CHANGE THE GAME.
 * 
 * @author smkautz
 */
public class GameMain
{
  /**
   * Cell size in pixels.
   */
  public static final int SIZE = 40; 
  
  /**
   * Dot size in pixels, must be less than or equal to SIZE.
   */
  public static final int DOT_SIZE = 20; 

  /**
   * Line width in pixels.
   */
  public static final int LINE_SIZE = 6;
  
  /**
   * Font size for displaying score.
   */
  public static final int SCORE_FONT = 24; 

  /**
   * Colors...
   */
  public static final Color BACKGROUND_COLOR = Color.DARK_GRAY;
  public static final Color WALL_COLOR = Color.ORANGE;
  public static final Color EXIT_COLOR = Color.MAGENTA;
  public static final Color GREEN_SNAKE_COLOR = Color.GREEN;
  public static final Color YELLOW_SNAKE_COLOR = Color.YELLOW;
  public static final Color MUSHROOM_COLOR = Color.CYAN;
  public static final Color APPLE_COLOR = Color.RED;
  public static final Color GRID_COLOR = Color.LIGHT_GRAY;

  
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
  
  // same as test 1 but with some apples and mushrooms to try eating
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

  // slightly harder one :)
  public static final String[] test3 = {
  "#  #  #  #  #  E  #",
  "#  y8 y7 y6 y5 y4 #",
  "#  x0 g0 .  .  y3 #",
  "#  x1 g1 .  .  y2 #",
  "#  x2 g2 g3 g4 y1 #",
  "#  x3 x4 x5 x6 y0 #",
  "#  #  #  #  #  #  #"
  };
  
  public static String[] test4 = {
  "#  #  #  E  #  #  #",
  "#  y0 y1 y2 y3 y4 #",
  "#  .  x0 .  g0 .  #",
  "#  .  x1 .  g1 .  #",
  "#  #  x2 #  g2 #  #",
  "#  x4 x3 #  g3 g4 #",
  "#  #  #  #  #  #  #"};
  
  /**
   * Helper method for instantiating the components.  This
   * method should be executed in the context of the Swing
   * event thread only.
   */
  private static void create()
  {

    // EDIT HERE TO CHANGE THE GAME BEING CREATED
    // ------------------------------------------
    
    // these will work if you don't have SnakeUtil implemented yet...
    //SnakeBasket game = new SnakeBasket(test1, makeSnakesForTest1());
    //SnakeBasket game = new SnakeBasket(test2, makeSnakesForTest1());
    
    // this will only work if you have SnakeUtil.findSnakes implemented
    SnakeBasket game = new SnakeBasket(test4);
    // ------------------------------------------
    
    // create the three UI panels
    ScorePanel scorePanel = new ScorePanel();
    GamePanel panel = new GamePanel(game, scorePanel);
    ChooseButtonPanel choosePanel = new ChooseButtonPanel(panel, scorePanel);

    // arrange the panels vertically
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(choosePanel);
    mainPanel.add(scorePanel);
    mainPanel.add(panel);

    // put main panel in a window
    JFrame frame = new JFrame("Com S 227 Snake Escape");
    frame.getContentPane().add(mainPanel);

    // give panels a nonzero size
    Dimension d = new Dimension(game.getCols() * GameMain.SIZE, game.getRows() * GameMain.SIZE);   
    panel.setPreferredSize(d);
    d = new Dimension(game.getCols() * GameMain.SIZE, 3 * GameMain.SIZE);   
    scorePanel.setPreferredSize(d);
    d = new Dimension(game.getCols() * GameMain.SIZE, GameMain.SIZE);   
    choosePanel.setPreferredSize(d);
    frame.pack();

    // we want to shut down the application if the 
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // rock and roll...
    frame.setVisible(true);
  }
  
  /**
   * Entry point.  Main thread passed control immediately
   * to the Swing event thread.
   * @param args not used
   */
  public static void main(String[] args)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        create();
      }
    };
    SwingUtilities.invokeLater(r);
  }
  
  /**
   * Constructs the snakes corresponding to the test1 descriptor above,
   * since we don't necessarily have SnakeUtil.findSnakes implemented yet...
   */
  private static ArrayList<Snake> makeSnakesForTest1()
  {
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
    
    return snakes;
  }
}
