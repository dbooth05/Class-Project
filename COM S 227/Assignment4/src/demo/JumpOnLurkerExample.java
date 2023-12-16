package demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import api.CircleRenderer;
import api.Drawable;
import api.ExplosionRenderer;
import api.GameElement;
import api.Renderer;
import hw4.Charm;
import hw4.Flash;
import hw4.Lurker;
import hw4.Missile;
import hw4.Platform;

/**
 * Uses a missile to jump on a Lurker and a Charm associated
 * with a platform.  If we collide, make them "explode" using
 * a Flash. 
 */
public class JumpOnLurkerExample extends GameBase
{
  public static void main(String[] args)
  {
    start(JumpOnLurkerExample.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;

  // Window size, fixed for simplicity
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;
  
  // Dimensions for objects
  private static final int SIZE = 30;
  
  // Player motion constants
  private static final double PLAYER_JUMP_VELOCITY = -12;
  private static final double GRAVITY = 0.8;
  private static final double PLAYER_SPEED = 4;

  
  /**
   * The player.
   */
  private Missile player;
  
  /**
   * A platform for player to land on.
   */
  private Platform platform;
  
  /**
   * Possible explosion, if we hit the enemy.
   */
  private ArrayList<Flash> explosions = new ArrayList<>();
  
  /**
   * And let's keep score!
   */
  private int score = 0;
  
  public JumpOnLurkerExample()
  {
    platform = new Platform(100, 300, 400, 20);
    platform.setColor(Color.GREEN);
    
    Lurker e = new Lurker(SIZE, SIZE, platform.getWidth() - SIZE);
    e.setColor(Color.RED);
    e.setVelocity(2, 0);
    platform.addChild(e);
    
    e = new Lurker(SIZE, SIZE, 2 * SIZE);
    e.setColor(Color.MAGENTA);
    e.setVelocity(1, 0);
    platform.addChild(e);
    
    Charm c = new Charm(SIZE, SIZE, (platform.getWidth() - SIZE) / 2, 100);
    c.setColor(Color.PINK);
    CircleRenderer r = new CircleRenderer(Color.PINK);
    r.setMargin(5);
    c.setRenderer(r);
    platform.addChild(c);
    
    player = new Missile(100, 300 - SIZE, SIZE, SIZE + SIZE/2);
    player.setColor(Color.YELLOW);
    player.setGravity(GRAVITY);
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
  public int getScore()
  {
    return score;
  }
  
  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();
    arr.add(player);
    arr.add(platform);
    for (Drawable c : platform.getChildren())
    {
      arr.add(c);
    }
    arr.addAll(explosions);
    
    return arr;
  }
  
  @Override
  protected void doUpdates()
  {
    player.update();
    platform.update();
    for (GameElement e : explosions)
    {
      e.update();
    }
    
    if (player.collides(platform))
    {
      // we landed! no longer flying...
      player.setBallistic(false);
      
      // align bottom of player with top of platform
      player.setPosition(player.getXReal(), platform.getYReal() - player.getHeight());
      
      // restore horizontal velocity, set vertical velocity to zero
      player.setVelocity(player.getDeltaX(), 0);      
    }
    
    for (GameElement e : platform.getChildren())
    {
      if (player.collides(e))
      {
        e.markForDeletion();
        Renderer r = new ExplosionRenderer(40, Color.ORANGE, Color.BLACK);
        Flash f = new Flash(e.getXInt(), e.getYInt(), SIZE, SIZE, 40);
        f.setRenderer(r);
        explosions.add(f);
        score += 10000;
      }
    }
    
    platform.deleteMarkedChildren();
    
    // delete marked explosions too
    ArrayList<Flash> temp = new ArrayList<>();
    for (Flash f : explosions)
    {
      if (!f.isMarked())
      {
        temp.add(f);
      }
    }
    explosions = temp;
  }
  
  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
     }
    else if (ch == KeyEvent.VK_RIGHT)
    {
       player.setVelocity(PLAYER_SPEED, player.getDeltaY());
    }
    else if (ch == KeyEvent.VK_A)
    {
      // jump
      player.setVelocity(player.getDeltaX(), PLAYER_JUMP_VELOCITY);
      player.setGravity(GRAVITY);
      player.setBallistic(true);
    }
  }
  
  @Override
  protected void doKeyReleased(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      player.setVelocity(0, player.getDeltaY());
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      player.setVelocity(0, player.getDeltaY());       
    }
  }
}
