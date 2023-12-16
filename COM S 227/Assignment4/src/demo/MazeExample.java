package demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import api.Drawable;
import hw4.BasicElement;
import hw4.MovingElement;


/**
 * Using some BasicElements and a MovingElement to create
 * a player in a maze.
 * @author smkautz
 */
public class MazeExample extends GameBase
{
  public static void main(String[] args)
  {
    start(MazeExample.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;

  // Window size, fixed for simplicity
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;
  
  // Dimensions for objects
  private static final int SIZE = 30;
  
  // Player motion constants
  private static final double PLAYER_SPEED = 4;
   
  /**
   * The player.
   */
  private MovingElement player;
  
  /**
   * The walls
   */
  private BasicElement[] walls = new BasicElement[6];

  public MazeExample()
  {
    walls[0] = new BasicElement(100, 100, 400, 20);
    walls[1] = new BasicElement(100, 200, 400, 20);
    walls[2] = new BasicElement(100, 100, 20, 120);
    walls[3] = new BasicElement(480, 100, 20, 120);
    walls[4] = new BasicElement(150, 150, 130, 20);
    walls[5] = new BasicElement(320, 150, 130, 20);

    player = new MovingElement(120, 120, SIZE, SIZE);
    player.setColor(Color.YELLOW);    
  }
  
  @Override
  public int getGameWidth()
  {
    return WIDTH;
  }
  
  @Override
  public int getGameHeight()
  {
    return HEIGHT;
  }
  
  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();

    
    for (BasicElement p : walls)
    {
      arr.add(p);
    }
    arr.add(player);

    return arr;
  }
  
  @Override
  protected void doUpdates()
  {
    player.update();
    for (BasicElement s : walls)
    {
      if (player.collides(s))
      {
        if (player.getDeltaX() > 0)
        {
          System.out.println("collides right");
          player.setPosition(s.getXReal() - player.getWidth(), player.getYReal());            
        }
        else if (player.getDeltaX() < 0)
        {
          System.out.println("collides left");
          player.setPosition(s.getXReal() + s.getWidth(), player.getYReal());            
        }
        else if (player.getDeltaY() < 0)
        {
          System.out.println("collides up");
          player.setPosition(player.getXReal(), s.getYReal() + s.getHeight());            
        }
        else if (player.getDeltaY() > 0)
        {
          System.out.println("collides down");
          player.setPosition(player.getXReal(), s.getYReal() - player.getHeight());            
        }
      }
    }
  }
  
  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      System.out.println("left");
      player.setVelocity(-PLAYER_SPEED, 0);
     }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      System.out.println("right");
      player.setVelocity(PLAYER_SPEED, 0);
    }
    else if (ch == KeyEvent.VK_UP)
    {
      System.out.println("up");
      player.setVelocity(0, -PLAYER_SPEED);
     }
    else if (ch == KeyEvent.VK_DOWN)
    {
      System.out.println("down");
      player.setVelocity(0, PLAYER_SPEED);
    }
  }
  
  @Override
  protected void doKeyReleased(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      System.out.println("left released");
      player.setVelocity(0, 0);
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      System.out.println("right released");
      player.setVelocity(0, 0);       
    }
    if (ch == KeyEvent.VK_UP)
    {
      System.out.println("up released");
      player.setVelocity(0, 0);
    }
    else if (ch == KeyEvent.VK_DOWN)
    {
      System.out.println("down released");
      player.setVelocity(0, 0);       
    }
  }
  
}
