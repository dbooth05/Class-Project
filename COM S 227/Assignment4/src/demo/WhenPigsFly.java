package demo;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import api.Drawable;
import api.ExplosionRenderer;
import api.GameElement;
import api.ImageRenderer;
import api.Renderer;
import hw4.BasicElement;
import hw4.Flash;
import hw4.Missile;
import hw4.MovingElement;


/**
 * An attempt to experiment with the mechanics of the lovely game
 * "Winter Bells"
 * http://www.freewebarcade.com/game/winterbells/
 * 
 * Side-to-side motion of player is controlled by mouse.  Player can 
 * jump using the 'a' key but only from the ground. Landing on an apple 
 * acts like a jump.
 * You get +10 points for each apple but -100 when you land on the ground
 * (provided you have at least 100 points).
 * 
 * The two images need to be present in the src/demo directory, since
 * they are loaded as resources on the classpath, not as files in
 * the working directory.
 * 
 * @author smkautz
 *
 */
public class WhenPigsFly extends GameBase
{
  
  public static void main(String[] args)
  {
    start(WhenPigsFly.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;
  
  // Window size, fixed for simplicity
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  // Dimensions for the images
  private static final int SIZE = 30;
  private static final int PLAYER_SIZE = 48;

  // constants for the playeer
  private static final double PLAYER_JUMP_VELOCITY = -12;
  private static final double GRAVITY = 0.7;
  
  // names of image files
  private static final String PLAYER_IMAGE = "pig_small_alpha.png";
  private static final String APPLE_IMAGE = "apple_small_alpha.png";
  
  
  // hopefully all this is self-explanatory...
  /**
   * The player.
   */
  private Missile player;
  
  /**
   * Platform at bottom of the screen.
   */
  private BasicElement platform;
  
  /**
   * List of "enemies" that will fall from the sky.
   */
  private ArrayList<MovingElement> apples;
  
  /**
   * List of active explosions for when we land on an apple.
   */
  private ArrayList<Flash> explosions;
  
  /**
   * Renderer for the player.
   */
  private ImageRenderer renderer;
  
  /**
   * Renderer for the apples.
   */
  private Renderer appleRenderer;
  
  /**
   * Keep track of player's orientation to match direction of motion.
   */
  private boolean playerFlipped;
  
  /**
   * Generator for random positins.
   */
  private Random rand;
  
  /**
   * Vertical velocity for apples.
   */
  private double appleSpeed = 1.0;
  
  /**
   * Initial number of apples.
   */
  private int minApples = 8;
  
  /**
   * If number of apples goes below this value, make some more.
   */
  private int maxApples = 24; 
  
  /**
   * Track mouse position so player can follow it.
   */
  private int mouseX;
  
  /**
   * Keep score.
   */
  private int score;
  
  /**
   * Constructor sets up components.
   */
  public WhenPigsFly()
  {
    rand = new Random();
    
    // load images
    Image playerImage = null;
    java.net.URL url = WhenPigsFly.class.getResource(PLAYER_IMAGE);
    if (url != null)
    {
      playerImage = new ImageIcon(url).getImage();
    }
    renderer = new ImageRenderer(playerImage, Color.PINK);
    player = new Missile(WIDTH / 2, HEIGHT - SIZE * 2, PLAYER_SIZE, PLAYER_SIZE);   
    player.setRenderer(renderer);
    
    Image enemyImage = null;
    url = WhenPigsFly.class.getResource(APPLE_IMAGE);
    if (url != null)
    {
      enemyImage = new ImageIcon(url).getImage();
    }
    appleRenderer = new ImageRenderer(enemyImage, Color.RED);
    
    // one nonmoving platform at bottom
    platform = new BasicElement(0, HEIGHT - SIZE, WIDTH, HEIGHT);
    
    apples = new ArrayList<>();
    explosions = new ArrayList<>();
    
    // start by making some apples
    for (int i = 0; i < minApples; ++i)
    {
      apples.add(makeApple());
    }
  }

  @Override
  protected void doUpdates()
  {
    player.update();
    platform.update();
    for (GameElement e : apples)
    {
      e.update();
    }
    for (GameElement e : explosions)
    {
      e.update();
    }
    
    if (player.collides(platform))
    {
      player.setBallistic(false);
      player.setPosition(player.getXReal(), platform.getYInt() - player.getHeight());
      player.setVelocity(player.getDeltaX(), 0);
      if (score >= 100)
      {
        score -= 100;
      }
      // set gravity back to zero so we don't keep colliding 
      player.setGravity(0);
    }

    // should follow the mouse, faster if farther away
    int currentX = player.getXInt();
    double delta = (mouseX - currentX);
    double velocity;
    if (Math.abs(delta) > 300) 
    {
      velocity = delta / 3;        
    }
    else if (Math.abs(delta) > 200) 
    {
      velocity = delta / 5;
    }
    else
    {
      velocity = delta / 6;
    }

    player.setVelocity(velocity, player.getDeltaY());

    // in any case, check for collision with apple when going down
    for (MovingElement s : apples)
    {
      if (s.collides(player) && player.getDeltaY() > 0)
      {
        player.setVelocity(player.getDeltaX(), PLAYER_JUMP_VELOCITY);
        player.setGravity(GRAVITY);
        s.markForDeletion();
        Renderer r = new ExplosionRenderer(30, Color.RED, getBackgroundColor());
        Flash explosion = new Flash(s.getXReal(), s.getYReal(), SIZE, SIZE, 30);
        explosion.setRenderer(r);
        explosions.add(explosion);
        score += 10;
      }
    }

    // falling off screen
    for (MovingElement s : apples)
    {
      if (s.getYInt() >= HEIGHT - 150)
      {
        s.markForDeletion();
      }
    }

    // take care of deletions
    deleteAllMarked();


    int newApples = (maxApples - apples.size()) / 2;
    for (int i = 0; i < newApples; ++i)
    {
      MovingElement e = makeApple();
      apples.add(e);
    }
  }


  /**
   * Helper method creates a new enemy with random (x, y) position
   * @return
   */
  private MovingElement makeApple()
  {
    int appleX = rand.nextInt(WIDTH - 100) + 50;
    int appleY = - rand.nextInt(500);  // spread them out...
    MovingElement e = new MovingElement(appleX, appleY, SIZE, SIZE);
    e.setRenderer(appleRenderer);
    e.setVelocity(0, appleSpeed);
    apples.add(e);
    return e;
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
    arr.add(player);  
    arr.add(platform);
    arr.addAll(apples);
    arr.addAll(explosions);
    return arr;
  }



  private void deleteAllMarked()
  {
    ArrayList<Flash> temp = new ArrayList<Flash>();
    for (Flash s : explosions)
    {
      if (!s.isMarked())
      {
        temp.add(s);
      }
    }
    explosions = temp;

    ArrayList<MovingElement> temp2 = new ArrayList<>();
    for (MovingElement s : apples)
    {
      if (!s.isMarked())
      {
        temp2.add(s);
      }
    }
    apples = temp2;
    
  }


  @Override
  protected int getScore()
  {
    return score;
  }

  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_A)
    {
      // jump, but only if we're on the platform
      if (player.getYInt() + player.getHeight() >= platform.getYInt() && !player.isBallistic())
      {
        player.setVelocity(player.getDeltaX(), PLAYER_JUMP_VELOCITY);
        player.setGravity(GRAVITY);
      }
    }
  }


  @Override
  protected void doMouseMoved(int x, int y)
  {
    if (!playerFlipped && x > player.getXInt() ||
        playerFlipped && x < player.getXInt())
    {
      playerFlipped = !playerFlipped;
    }
    mouseX = x;
    renderer.flipHorizontal(playerFlipped);
  }
  
}
