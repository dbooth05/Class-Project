package coloring.ui;


import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import coloring.ColoringBook;
import coloring.api.Util;

/**
 * Entry point for creating and starting graphical interface for
 * a Coloring Book game.
 * @author smkautz
 */
public class GUIMain
{

  
  
  /**
   * Entry point.  Edit here to change the initialization of the game.
   * @param args
   */
  public static void main(String[] args)
  {
//    ColoringBook g = new ColoringBook(Util.test0);
    ColoringBook g = new ColoringBook("doodle1.png");

    start(g);
  }

  /**
   * Start up the game on the UI event thread.
   * @param game
   * @param sleepTime
   */
  private static void start(final ColoringBook game)
  {
    Runnable r = new Runnable()
    {
      public void run()
      {
        createAndShow(game);
      }
    };
    SwingUtilities.invokeLater(r);
  }
  
  /**
   * Initialize GUI components.  This should only be executed on the GUI
   * event thread.
   * @param game
   * @param sleepTime
   */
  private static void createAndShow(final ColoringBook game)
  {
    // create score panel and grid panel

    int rowSize = 1000 / game.getRows();
    int colSize = 1000 / game.getColumns();
    int initialSize = Math.max(1, Math.min(rowSize, colSize));
    initialSize = Math.min(initialSize, 40);
    
    
    JPanel scorePanel = new JPanel();
    JPanel mainPanel = new JPanel();

    ColoringBookPanel panel = new ColoringBookPanel(game, scorePanel, mainPanel, initialSize);

    // set the grid panel to receive notifications for animation
    game.setObserver(panel);
    
    // arrange the two panels vertically
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(scorePanel);
    mainPanel.add(panel);

    // create the frame
    JFrame frame = new JFrame("ColoringBook");
    //System.out.println("original frame " + frame.hashCode());

    frame.setResizable(true);
    frame.getContentPane().add(mainPanel);

    // give it a nonzero size
    //panel.setPreferredSize(new Dimension(game.getColumns() * ColoringBookPanel.CELL_SIZE, game.getRows() * ColoringBookPanel.CELL_SIZE));    
    panel.setPreferredSize(new Dimension(game.getColumns() * initialSize, game.getRows() * initialSize));    
    frame.pack();
    System.out.println(frame.getWidth() + ", " + frame.getHeight());

    // we want to shut down the application if the 
    // "close" button is pressed on the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // make the frame visible and start the UI machinery
    frame.setVisible(true);   

  }
}
