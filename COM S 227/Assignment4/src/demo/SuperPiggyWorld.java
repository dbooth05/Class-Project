package demo;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import api.CircleRenderer;
import api.Drawable;
import api.ExplosionRenderer;
import api.GameElement;
import api.ImageRenderer;
import api.Renderer;
import hw4.BasicElement;
import hw4.Charm;
import hw4.Elevator;
import hw4.Flash;
import hw4.Lurker;
import hw4.Missile;
import hw4.MovingElement;
import hw4.Platform;

/**
 * Demo of pretty much all the game elements in hw4.  Rescue the princess
 * from the tower before she jumps off in despair!  Jump on the enemies
 * to vanquish them.  Grab the charm to pause the motion of the 
 * tower carrying the princess on her march to doom.  But watch out 
 * for those enemies, they'll kill you if you run into one, unless you
 * jump on them from above.
 * 
 * Left and right arrows to control player, 'a' key to jump.  
 * 
 * The two images need to be present in the src/demo directory, since
 * they are loaded as resources on the classpath, not as files in
 * the working directory.
 * 
 * @author smkautz
 *
 */
public class SuperPiggyWorld extends GameBase
{
  public static void main(String[] args)
  {
    start(SuperPiggyWorld.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;
  
  // Window size, fixed for simplicity
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  // Basic dimensions to use for objects
  private static final int SIZE = 30;
  private static final int PLAYER_SIZE = 48;

  // names of image files
  private static final String PLAYER_IMAGE = "pig_small_alpha.png";
  private static final String APPLE_IMAGE = "apple_small_alpha.png";

  // Misc constants
  private static final double PLAYER_JUMP_VELOCITY = -12;
  private static final double PLAYER_BASE_VELOCITY = 5; // >= elevator velocities

  private static final double GRAVITY = 0.8;
  private static final double PLAYER_SPEED = 2;
  private static final int EXPLOSION_LIFETIME = 40;
  private static final int ENEMY_COUNT = 1;
  private static final int ENEMY_HIT_SCORE = 1000;
  private static final int CHARM_INTERVAL = 75;
  
  /**
   * The player
   */
  private Missile player;
  
  /**
   * List of platforms
   */
  private ArrayList<Platform>  platforms;
  
  /**
   * List of platforms
   */
  private ArrayList<Elevator>  elevators;
  
  /**
   * List of explosions
   */
  private ArrayList<Flash> explosions;
  
  /**
   * Charm that may occasionally appear...
   */
  private Charm charm;
  
  /**
   * Track the current platform, so we don't generate new enemies on it.
   */
  private GameElement currentPlatform;

  /**
   * Count of number of frames right arrow key has been held down, to potentially speed up
   * player.
   */
  private int rightArrow;
  
  /**
   * Count of number of frames left arrow key has been held down, to potentially speed up
   * player.
   */
  private int leftArrow;
  
  /**
   * Stop timer when game is over.
   */
  private boolean over;
  
  /**
   * Generator for random positions.
   */
  private Random rand;

  /**
   * Create this once, since we need it a lot.
   */
  private Renderer enemyRenderer;

  /**
   * Current score for the game.
   */
  private int score;
  
  /**
   * Renderer for player, so we can change player's direction.
   */
  private ImageRenderer renderer;

  /**
   * Renderer for princess, so we can change direction.
   */
  private ImageRenderer princessRenderer;
  
  /**
   * Counter to let a number of frames elapse before adding/removing charm.
   */
  private int charmCounter;
  
  /**
   * Tower for the princess
   */
  private MovingElement tower;
  
  /**
   * The princess!
   */
  private Missile princess;
  
  /**
   * Initial speed of princess and tower.
   */
  private double princessSpeed = -0.5;
  
  /**
   * When we get a charm, pause the tower for a couple of seconds.
   */
  private boolean princessPaused = false;
  
  /**
   * Construct everything;
   */
  public SuperPiggyWorld()
  {
    rand = new Random();
    
    // create all the elements
    setupLevel();
  }
  
  private void setupLevel()
  {
    over = false;

    // load images
    Image playerImage = null;
    java.net.URL url = SuperPiggyWorld.class.getResource(PLAYER_IMAGE);
    if (url != null)
    {
      playerImage = new ImageIcon(url).getImage();
    }

    Image enemyImage = null;
    url = SuperPiggyWorld.class.getResource(APPLE_IMAGE);
    if (url != null)
    {
      enemyImage = new ImageIcon(url).getImage();
    }
    renderer = new ImageRenderer(playerImage, Color.PINK);
    princessRenderer = new ImageRenderer(playerImage, Color.PINK);
    
    // platforms
    platforms = new ArrayList<>();
    Platform p0 = new Platform(100 - SIZE * 2, 340, SIZE * 2, SIZE * 2);
    p0.setColor(Color.GREEN);
    platforms.add(p0);
    Platform p1 = new Platform(100, 400, SIZE * 5, SIZE);
    p1.setColor(Color.GREEN);
    platforms.add(p1);
    Platform p2 = new Platform(200, 480, SIZE * 5, SIZE);
    p2.setColor(Color.GREEN);
    platforms.add(p2);
    p2.setBounds(200, 500);
    p2.setVelocity(2, 0);  // moving
    Platform p3 = new Platform(500, 400, SIZE * 5, SIZE);
    p3.setColor(Color.GREEN);
    platforms.add(p3);    
    
    Platform p5 = new Platform(370, 280, SIZE * 4, SIZE / 2);
    platforms.add(p5);
     
    // elevators
    elevators = new ArrayList<>();
    
    Elevator e0 = new Elevator(200, 180, SIZE * 4, SIZE);
    elevators.add(e0);
    e0.setBounds(150, 350);
    e0.setVelocity(0, 3);  // moving
//    Platform e0 = new Platform(200, 350 , SIZE * 4, SIZE);
//    platforms.add(e0);
//    //e0.setBounds(150, 350);
//    //e0.setVelocity(0, 3);  // moving

    
    Elevator e1 = new Elevator(680, 200, SIZE * 2, SIZE);
    elevators.add(e1);
    e1.setBounds(200, 350);
    e1.setVelocity(0, 4);  // moving

    Elevator e2 = new Elevator(580, 100, SIZE * 2, SIZE);
    //System.out.println("xxxx " + e0.getChildren());
    elevators.add(e2);
    e2.setBounds(150, 250);
    e2.setVelocity(0, 5);  // moving

    // player
    player = new Missile(100, 100, PLAYER_SIZE, PLAYER_SIZE);
    player.setRenderer(renderer);
    player.setPosition(p0.getXInt() + (p0.getWidth() - PLAYER_SIZE) / 2, p0.getYInt()  - PLAYER_SIZE);
    player.setVelocity(0, PLAYER_BASE_VELOCITY);
    currentPlatform = p0;

    // tower and princess
    tower = new MovingElement(700, 80, SIZE * 2, SIZE);
    tower.setVelocity(princessSpeed, 0);
    tower.setColor(Color.PINK);
    int princessSize =  (int) (PLAYER_SIZE * .8);
    princess =  new Missile(100, 100, princessSize, princessSize);
    princess.setRenderer(princessRenderer);
    princess.setVelocity(princessSpeed, 0);
    princess.setPosition(tower.getXInt() + (tower.getWidth() - princessSize) / 2, tower.getYInt() - princessSize);
    
    // platforms and enemies
    enemyRenderer = new ImageRenderer(enemyImage, Color.RED);
    setUpEnemies();

    // no explosions yet (but there will be...)
    explosions = new ArrayList<>();

  }

  
  @Override
  protected int getGameWidth()
  {
    return WIDTH;
  }
  
  @Override
  protected int getGameHeight()
  {
    return HEIGHT;
  }

  @Override 
  protected int getScore()
  {
    return score;
  }
  
  @Override
  protected boolean isOver()
  {
    return over;
  }
  

  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();
    arr.add(player);
    arr.add(tower);
    arr.add(princess);
    if (charm != null)
    {
      arr.add(charm);
    }
    arr.addAll(platforms);
    for (Platform p : platforms)
    {
      arr.addAll(p.getChildren());
    }
    arr.addAll(elevators);
    for (Elevator p : elevators)
    {
      arr.addAll(p.getChildren());
    }
    arr.addAll(explosions); // render last
    return arr;
  }

  
  
  
  @Override
  protected void doUpdates()
  {
    player.update();
    tower.update();
    princess.update();
    for (GameElement p : platforms)
    {
      p.update();
    }
    for (GameElement p : elevators)
    {
      p.update();
    }
    for (GameElement s : explosions)
    {
      s.update();
    }

    // if player is on a moving platform, adjust for motion
    double deltaX = 0;
    if (currentPlatform instanceof Platform)
    {
      deltaX = ((Platform) currentPlatform).getDeltaX();
    }
    else if (currentPlatform instanceof Elevator)
    {
      deltaX = ((Elevator) currentPlatform).getDeltaX();
    }
    if (!player.isBallistic() && deltaX != 0)
    {
      player.setPosition(player.getXReal() + deltaX, player.getYReal());
    }

    // adjust player speed if arrow key has been held down
    if (leftArrow > 0) 
    {
      leftArrow += 1;
      if (leftArrow > 5)
      {
        player.setVelocity(-PLAYER_SPEED * 2, player.getDeltaY());
      }
    }
    if (rightArrow > 0) 
    {
      rightArrow += 1;
      if (rightArrow > 5)
      {
        player.setVelocity(PLAYER_SPEED * 2, player.getDeltaY());
      }
    }
    
    for (Platform s : platforms)
    {
      checkCollisionWithPlatform(s);
    }
    for (Elevator s : elevators)
    {
      checkCollisionWithPlatform(s);
    }
    
    // if we wandered off a platform, go ballistic
    if (!player.isBallistic() && ! isOnPlatform())
    {
      // I'm falling!!
      player.setGravity(GRAVITY);
      player.setBallistic(true);      
    }

    // in any case, check for collision with enemy
    ArrayList<Lurker> enemies = getAllEnemies();
    for (Lurker s : enemies)
    {
      checkCollidesWithEnemy(s);
    }
    
    // also the charm
    if (charm != null)
    {
      if (player.collides(charm))
      {
        charm.markForDeletion();
        Flash f = new Flash(charm.getXInt(), charm.getYInt(), SIZE * 2, SIZE * 2, EXPLOSION_LIFETIME); 
        f.setRenderer(new ExplosionRenderer(EXPLOSION_LIFETIME, Color.PINK, getBackgroundColor()));
        explosions.add(f);
        score += ENEMY_HIT_SCORE * 10;
        charm = null;
        princessPaused = true;
      }
    }

    // also the tower?
    checkCollisionWithPlatform(tower);
    if (currentPlatform == tower)
    {
      score += ENEMY_HIT_SCORE * 100;
      over = true;
    }
    
    // fall off screen?
    if (player.getYInt() + player.getHeight() > HEIGHT - SIZE)
    {
      over = true;
      renderer.flipVertical(true);
    }

    // princess falls off?
    if (princess.getYInt() + princess.getHeight() > HEIGHT - SIZE)
    {
      over = true;
      princessRenderer.flipVertical(true);
    }    
    
    if (princessPaused && tower.getXInt() > 25)
    {
      tower.setVelocity(0, 0);
      princess.setVelocity(0, 0);      
    }
    else
    {
      // if the tower gets halfway across, speed it up
      if (tower.getXInt() == WIDTH / 2)
      {
        princessSpeed *= 2;
        princessRenderer.flipHorizontal(true);
      }   

      // if the tower gets all the way to left, princess jumps off
      if (tower.getXInt() <= 25 && tower.getXInt() > 0)
      {
        princessRenderer.flipHorizontal(true);
        princess.setVelocity(3, 0);
      }
      else if (tower.getXInt() <= 0 && !princess.isBallistic())
      {
        princess.setVelocity(3, 5);
        princess.setGravity(GRAVITY);
        princess.setBallistic(true);
      }
      else if (tower.getXInt() > 25)
      {
        // update motion of tower, maybe
        tower.setVelocity(princessSpeed, 0);
        princess.setVelocity(princessSpeed, 0);
      }
    }
    charmCounter -= 1;
    if (charmCounter <= 0)
    {
      if (charm != null)
      {
        charm.markForDeletion();
        charm = null;
        charmCounter = CHARM_INTERVAL + rand.nextInt(50);
      }
      else
      {
        makeCharm();
        charmCounter = CHARM_INTERVAL + rand.nextInt(50);
        princessPaused = false;
      }
    }
    
    // take care of deletions
    deleteAllMarked();
  }
  
  /**
   *  Set the element's position to a random location on the platform.
   */
  private void setPositionOnPlatform(Platform p, Lurker s)
  {
    int minX = p.getXInt();
    int maxX = p.getXInt() + p.getWidth() - s.getWidth();
    int bound = maxX - minX;
    bound = Math.max(1, bound);   
    int x = rand.nextInt(bound) + minX;
    int y = p.getYInt() - s.getHeight();
    s.setPosition(x, y);
  }

  /**
   *  Set the element's position to a random location on the platform.
   */
  private void setPositionOnPlatform(Elevator p, Lurker s)
  {
    int minX = p.getXInt();
    int maxX = p.getXInt() + p.getWidth() - s.getWidth();
    int bound = maxX - minX;
    bound = Math.max(1, bound);   
    int x = rand.nextInt(bound) + minX;
    int y = p.getYInt() - s.getHeight();
    s.setPosition(x, y);
  }
  
  /**
   * Make sure there is an enemy on each platform other than the one the player is on
   */
  private void setUpEnemies()
  {
    for (Platform p : platforms)
    {
      // don't plop an enemy on top of the player
      if (p != currentPlatform)
      {
        if (p.getChildren().size() < ENEMY_COUNT)
        {
          Lurker e = new Lurker(SIZE, SIZE, 0);
          e.setRenderer(enemyRenderer);
          setPositionOnPlatform(p, e);
          p.addChild(e);
          double dx = 1 + rand.nextDouble();
          if (rand.nextInt(2) > 0) dx = -dx;
          e.setVelocity(dx, 0);
        }
      }
    }
    for (Elevator p : elevators)
    {
      // don't plop an enemy on top of the player
      if (p != currentPlatform)
      {
        if (p.getChildren().size() < ENEMY_COUNT)
        {
          Lurker e = new Lurker(SIZE, SIZE, 0);
          e.setRenderer(enemyRenderer);
          setPositionOnPlatform(p, e);
          p.addChild(e);
          double dx = 1 + rand.nextDouble();
          if (rand.nextInt(2) > 0) dx = -dx;
          e.setVelocity(dx, 0);
        }
      }
    }
  }

  private void makeCharm()
  {
    int max = platforms.size() + elevators.size();
    int which = rand.nextInt(max);
    int count = 0;
    CircleRenderer r = new CircleRenderer(Color.PINK);
    r.setMargin(5);
    for (Platform p : platforms)
    {
      if (count == which)
      {
        charm = new Charm(SIZE, SIZE, (p.getWidth() - SIZE)/ 2, 100);
        charm.setRenderer(r);
        p.addChild(charm);
        return;
      }
      count += 1;
    }
    for (Elevator p : elevators)
    {
      if (count == which)
      {
        charm = new Charm(SIZE, SIZE, (p.getWidth() - SIZE)/ 2, 100);
        charm.setRenderer(r);
        p.addChild(charm);
        return;
      }
      count += 1;
    }
  }
  
  /**
   * Returns a list of all enemies.
   */
  private ArrayList<Lurker> getAllEnemies()
  {
    ArrayList<Lurker> arr = new ArrayList<>();
    for (Platform p : platforms)
    {
      for (GameElement b : p.getChildren())
      {
        if (b != charm)
        {
          arr.add((Lurker) b);
        }
      }
      
    }
    for (Elevator p : elevators)
    {
      for (GameElement b : p.getChildren())
      {
        if (b != charm)
        {
          arr.add((Lurker) b);
        }
      }
      
    }
    return arr;
  }


  /**
   * Determines whether the player is currently on a platform.
   */
  private boolean isOnPlatform()
  {
    double saved = player.getYReal();
    player.setPosition(player.getXReal(), saved + 2);
    for (Platform s : platforms)
    {
      if (player.collides(s))
      {
        player.setPosition(player.getXReal(), saved);
        return true;
      }
    }
    for (Elevator s : elevators)
    {
      if (player.collides(s))
      {
        player.setPosition(player.getXReal(), saved);
        return true;
      }
    }
    player.setPosition(player.getXReal(), saved);
    return false;
  }

  
  /**
   * Update player's state appropriately in case of collision with a platform.
   * This could be from below, left, right, or above (landing on the platform).
   */
  private void checkCollisionWithPlatform(GameElement s)
  {
    
    if (player.collides(s)) // && player.getDeltaY() >= 0)
    {
      MovingElement platform = (MovingElement) s;     

      // need to figure out whether we actually landed on the platform,
      // or collided with it in some other way. Strategy is to check
      // whether moving the player up a bit stops it from colliding,
      // where "a bit" is based on player dy plus maximal possible
      // vertical motion of platform.  We can't just use platform dy, because
      // it changes sign at top and bottom of motion, so use absolute
      // value of dy as a bound
      
      boolean landed = false;
      double possibleShift = 0;
      double fudge = .01;  // for possible rounding errors
      if (player.getDeltaY() > 0)
      {
        possibleShift  += player.getDeltaY();
      }
      if (platform.getDeltaY() < 0)
      {
        possibleShift += -platform.getDeltaY();
      }
      else
      {
        possibleShift += platform.getDeltaY();
      }

      double savedY = player.getYReal();
      player.setPosition(player.getXReal(), savedY - possibleShift - fudge);
      if (!player.collides(platform) && player.getDeltaY() > 0)
      {
        // ok, we landed
        landed = true;
      }
      if (landed)
      {
        // be on top of it...
        player.setPosition(player.getXReal(), platform.getYReal() - player.getHeight());
        player.setBallistic(false);
        player.setVelocity(player.getDeltaX(), PLAYER_BASE_VELOCITY);
        checkArrowKeys();
        currentPlatform = s;
        
        // this will generate a new enemy on the platform the player left
        setUpEnemies();
      }
      else
      {
        // as we were
        player.setPosition(player.getXReal(), savedY);
        
        // Player is not landing.  If this is a collision from below,
        // then reverse the vertical velocity, and if it's horizontal
        // then set horizontal velocity to zero.  Note that player may
        // be ballistic or not in a horizontal motion.
        
        // temporarily set ballistic false, since we have to adjust dx
        boolean jumping = player.isBallistic(); 
        player.setBallistic(false); 

        double newX = player.getXReal();
        double newY = player.getYReal();
        double dx = player.getDeltaX();
        double dy = player.getDeltaY();
        
        // this is all a bit crude and does not account for platform motion...
        
        if (dy < 0 && player.getYInt() < s.getYInt() + s.getHeight())
        {
          // collide from below
          newY = s.getYInt() + s.getHeight();
          dy = -dy;
        }
        else if (dx > 0 && (player.getXInt() + player.getWidth()) > s.getXInt())
        {
          // collide from left
          newX = s.getXInt() - player.getWidth();
          dx = 0;
        }
        else if (dx < 0 && player.getXInt() < s.getXInt() + s.getWidth())
        {      
          // collide from right
          newX = s.getXInt() + s.getWidth();   
          dx = 0;
        }

        player.setPosition(newX, newY);
        player.setVelocity(dx, dy);
        player.setBallistic(jumping);

      }
    }
  }
  
  /**
   * Check whether an arrow key was released while player was ballistic, in
   * order to correctly set dx when landing.
   */
  private void checkArrowKeys()
  {
    // This is kind of tedious.  Since the player can't change dx when
    // ballistic, a release of the arrow key would not have been recorded
    // in player's dx.  So we have to explicitly check whether arrow key is 
    // down and set dx accordingly
    double dx = player.getDeltaX();
    if (leftArrow == 0 && rightArrow == 0) 
    {
      player.setVelocity(0, player.getDeltaY());
    }
    else if (leftArrow > 0)
    {
      if (dx < 0)
      {
        player.setVelocity(dx, 0);
      }
      else
      {
        player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
      }
    }
    else if (rightArrow > 0)
    {
      if (dx > 0)
      {
        player.setVelocity(dx, player.getDeltaY());
      }
      else
      {
        player.setVelocity(PLAYER_SPEED, player.getDeltaY());
      }
    }
  }
  
  /**
   * Check for player's collision with enemy.  If player lands on enemy from the
   * top, enemy dies; otherwise player dies.
   */
  private void checkCollidesWithEnemy(GameElement s)
  {
    if (player.collides(s))
    {
      // If we come in from top, we kill the enemy, but if we hit from side
      // then we lose.  So, if bottom of player is higher than half the enemy's height,
      // then it kills the enemy, otherwise it kills the player
      int playerBase = player.getYInt() + player.getHeight();
      int enemyBase = s.getYInt() + s.getHeight() / 2;
      if (playerBase < enemyBase)
      {
        //System.out.println("Enemy dies");
        s.markForDeletion();
        Flash e = new Flash(s.getXInt(), s.getYInt(), SIZE, SIZE, EXPLOSION_LIFETIME); 
        e.setRenderer(new ExplosionRenderer(EXPLOSION_LIFETIME, Color.ORANGE, getBackgroundColor()));
        explosions.add(e);
        score += ENEMY_HIT_SCORE;
      }
      else
      {
        //System.out.println("Player dies");
        over = true;
        renderer.flipVertical(true);
      }       
    } 
  }
  
  /**
   * Deletes all marked explosions or enemies.
   */
  private void deleteAllMarked()
  {
    ArrayList<Flash> temp = new ArrayList<>();
    for (Flash s : explosions)
    {
      if (!s.isMarked())
      {
        temp.add(s);
      }
    }
    explosions = temp;
    for (Platform p : platforms)
    {
      p.deleteMarkedChildren();
    }
    for (Elevator p : elevators)
    {
      p.deleteMarkedChildren();
    }
  }

  
  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      //System.out.println("left");
      leftArrow += 1;
      rightArrow = 0;
      player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
      //playerFlipped = false;
      renderer.flipHorizontal(false);
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      //System.out.println("right");
      rightArrow += 1;
      leftArrow = 0;
      player.setVelocity(PLAYER_SPEED, player.getDeltaY());
      //playerFlipped = true;
      renderer.flipHorizontal(true);

    }
    else if (ch == KeyEvent.VK_A)
    {
      // jump
      if (!player.isBallistic())
      {
        double jumpVelocity = PLAYER_JUMP_VELOCITY;
        
        // if we're on a moving platform, we carry that velocity too
        double deltaX = 0;
        if (currentPlatform instanceof Platform)
        {
          deltaX = ((Platform) currentPlatform).getDeltaX();
        }
        else if (currentPlatform instanceof Elevator)
        {
          deltaX = ((Elevator) currentPlatform).getDeltaX();
        }
        double dx = player.getDeltaX() + deltaX;
        
        // if we're moving fast, we can jump higher
        if (Math.abs(dx) >= PLAYER_SPEED * 2)
        {
          jumpVelocity *= 1.3;
        }
        player.setVelocity(dx, jumpVelocity);
        player.setGravity(GRAVITY);
        player.setBallistic(true);
      }
    }
  }
  
  @Override
  protected void doKeyReleased(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      //System.out.println("left released");
      leftArrow = 0;
      player.setVelocity(0, player.getDeltaY());       
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      //System.out.println("right released");
      rightArrow = 0;
      player.setVelocity(0, player.getDeltaY());
    }
  }
}
