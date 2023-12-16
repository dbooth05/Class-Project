package demo;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import api.Drawable;
import api.GameElement;
import api.ImageRenderer;
import hw4.BasicElement;

/**
 * Example of using an ImageRenderer with a BasicElement.
 */
public class PigsInSpace extends GameBase
{
  public static void main(String[] args)
  {
    start(PigsInSpace.class);
  } 

  // suppress compiler warning
  private static final long serialVersionUID = 1L;


  // Window size, fixed for simplicity
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;

  // Basic dimensions for objects
  private static final int SIZE = 30;

  /**
   * List of elements to draw.
   */
  private ArrayList<BasicElement> piggies;
  
  /**
   * Renderer to use for all of them.
   */
  private ImageRenderer renderer;

  /**
   * Random for generating positions.
   */
  private Random rand;

  /**
   * Rotation angle.
   */
  private int angle;
  
  /**
   * Constructor creates and starts the timer.
   */
  public PigsInSpace()
  {
    rand = new Random();
    piggies = new ArrayList<>();
    
    // load images
    Image playerImage = null;
    java.net.URL url = SuperPiggyWorld.class.getResource("pig_small_alpha.png");
    if (url != null)
    {
      playerImage = new ImageIcon(url).getImage();
    }

    renderer = new ImageRenderer(playerImage, Color.PINK);
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
    arr.addAll(piggies);
    return arr;
  }

  @Override
  protected void doUpdates()
  {
    // update everybody
    for (GameElement s : piggies)
    {
      s.update();
    }

    angle += 1;
    renderer.setRotation(angle);
    
    if (piggies.size() < 100)
    {
      // add another one
      int x = rand.nextInt(WIDTH);
      int y = rand.nextInt(HEIGHT);
      int size = rand.nextInt(SIZE * 3) + SIZE;
      BasicElement e = new BasicElement(x, y, size, size);
      e.setRenderer(renderer);
      piggies.add(e);
    }

  }

}

