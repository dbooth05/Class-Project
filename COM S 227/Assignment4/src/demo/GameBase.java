package demo;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import api.Drawable;
import hw4.BasicElement;

/**
 * Basic example of a Projectile that can be moved and can "jump"
 * using key controls.  Jumping just means that we give the sprite
 * a negative y-velocity and positive gravity so it will eventually
 * come down.  (Remember that in this world, the positive y-direction
 * is "down".)
 * 
 * Use left/right arrow to move, 'a' key to jump.
 */
public abstract class GameBase extends JPanel
{ 
  /**
   * Timer for animation
   */
  private Timer timer;
  
  /**
   * Interval in milliseconds between timer callbacks.
   */
  private int interval = 40;
  
  /**
   * Constructor creates and starts the timer.
   */
  public GameBase()
  {
    KeyListener listener = new BaseKeyListener();
    this.addKeyListener(listener);
    MouseListener mouseListener = new BaseMouseListener();
    this.addMouseListener(mouseListener);
    MouseMotionListener mouseMotionListener = new BaseMouseMotionListener();
    this.addMouseMotionListener(mouseMotionListener);
    interval = 1000 / getFrameRate();
    System.out.println("Interval: " + interval);
    timer = new Timer(interval, new TimerAction());   
    timer.start();
  }

  // Subclasses MUST override these methods:
  
  /**
   * Return a list of all drawables to be rendered, including
   * children of platforms and elevators.
   * @return
   *   list of all drawables
   */
  protected abstract ArrayList<Drawable> getAllDrawables();
  
  /**
   * Perform all desired updates on game elements.
   */
  protected abstract void doUpdates();
  
  /**
   * Returns the width of the game window, in pixels.
   * @return
   *   game width
   */
  protected abstract int getGameWidth();
  
  /**
   * Returns the height of the game window, in pixels.
   * @return
   *   game height
   */
  protected abstract int getGameHeight();
  
  // Subclasses MAY override these methods...
  
  /**
   * Performs updates based on a key press.
   * @param ch
   *   key code
   */
  protected void doKeyPressed(int ch) {}
  
  /**
   * Performs updates based on a key release.
   * @param ch
   *   key code
   */
  protected void doKeyReleased(int ch) {}
  
  /**
   * Performs updates based on a mouse press.
   * @param x
   *   x-coordinate of mouse
   * @param y
   *   y-coordinate of mouse
   */
  protected void doMousePressed(int x, int y) {}
  
  /**
   * Performs updates based on a mouse release.
   * @param x
   *   x-coordinate of mouse
   * @param y
   *   y-coordinate of mouse
   */
  protected void doMouseReleased(int x, int y) {}
  
  /**
   * Performs updates based on a mouse motion.
   * @param x
   *   x-coordinate of mouse
   * @param y
   *   y-coordinate of mouse
   */
  protected void doMouseMoved(int x, int y) {}
  
  /**
   * Performs updates based on a mouse drag (motion with button down).
   * @param x
   *   x-coordinate of mouse
   * @param y
   *   y-coordinate of mouse
   */
  protected void doMouseDragged(int x, int y) {} 
  
  /**
   * Returns true if the game is over (i.e. animation should stop).
   * Default is false.
   * @return
   *   true if the game is over
   */
  protected boolean isOver() 
  {
    return false;
  }
  
  /**
   * Returns the desired frame rate. Default is 25 frames per second.
   * @return
   *   desired frame rate
   */
  protected int getFrameRate() 
  {
    return 25;
  }
  
  /**
   * Returns the score. If the value is >= 0, score will be displayed
   * in upper left corner.  Default is -1 (not displayed).
   * @return
   *   current score
   */
  protected int getScore() 
  {
    return -1;
  }
  
  /**
   * Returns the color for displaying the score.  Default is YELLOW.
   * @return
   *   color to use for displaying the score
   */
  protected Color getScoreColor() 
  {
    return Color.YELLOW;
  }
  
  /**
   * Returns the background color.  Default is DARK_GRAY.
   * @return
   *   color to use for the background
   */
  protected Color getBackgroundColor() 
  {
    return Color.DARK_GRAY;
  }
  
  /**
   * Constructs an instance of the given game class and starts up
   * the UI machinery.  The given class must be a subclass of GameBase
   * and must have a no-arg constructor.
   * @param gameClass
   *   class for game to be constructed
   */
  public static void start(final Class<? extends GameBase> gameClass)
  {
    // Create the game instance and set up the UI machinery
    Runnable r = new Runnable()
    {
      public void run()
      {
        GameBase game = null;
        try
        {
          game = gameClass.getConstructor().newInstance();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        if (game != null)
        {
          createAndShow(game);
        }
      }
    };
    SwingUtilities.invokeLater(r);
  } 
  
  /**
   * Static helper method creates the frame and
   * makes it visible.
   */
  protected static void createAndShow(GameBase game)
  {
    JFrame frame = new JFrame();
      
    frame.getContentPane().add(game);
    game.setFocusable(true);
    frame.setSize(game.getGameWidth(), game.getGameHeight());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
   
  
  
  /**
   * Overridden paintComponent method is called by the Swing
   * framework when this component needs to be redrawn. NEVER
   * call this method yourself.
   */
  @Override    
  public void paintComponent(Graphics g)
  {    
    Graphics2D g2 = (Graphics2D) g;
    Color savedColor = g2.getColor();
    
    // paint background
    g2.setBackground(getBackgroundColor());
    g2.clearRect(0, 0, getWidth(), getHeight());

    // draw everybody
    for (Drawable s : getAllDrawables())
    {
      s.draw(g2);
    }
    
    // draw the score maybe
    if (getScore() >= 0)
    {
      Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
      g2.setFont(f);
      g2.setColor(getScoreColor());
      FontMetrics fm = g2.getFontMetrics(f);
      int h = fm.getHeight();   
      int textX = 10;
      int textY = 10 + h;
      g2.drawString("Score " + getScore(), textX, textY);
      textY += h;
    }
    
    // restore
    g.setColor(savedColor);
  }
  
  
  /**
   * Action listener for timer callbacks. 
   */
  class TimerAction implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if (isOver())
      {
        timer.stop();
      }
      else
      {
        // update state and then call repaint()
        doUpdates();    
      }
      repaint();
    }
  }
  
  /**
   * Callback for key presses.
   */
  private class BaseKeyListener implements KeyListener
  {
    @Override
    public void keyPressed(KeyEvent event)
    {
      int ch = event.getKeyCode();
      doKeyPressed(ch);
    }

    @Override
    public void keyReleased(KeyEvent event)
    {
      int ch = event.getKeyCode();
      doKeyReleased(ch);
    }

    @Override
    public void keyTyped(KeyEvent event)
    {
    }   
  }
  
  /**
   * Callback for mouse clicks.
   */
  private class BaseMouseListener implements MouseListener
  {

    @Override
    public void mouseClicked(MouseEvent event)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      doMousePressed(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      doMouseReleased(e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }
  }

  
  /**
   * Callback for mouse motion events.
   */
  private class BaseMouseMotionListener implements MouseMotionListener
  {

    @Override
    public void mouseDragged(MouseEvent e)
    {
      doMouseDragged(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
      doMouseMoved(e.getX(), e.getY());
    }
    
  }

}
