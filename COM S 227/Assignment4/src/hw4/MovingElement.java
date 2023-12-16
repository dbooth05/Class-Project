package hw4;

/**
 * Concrete implementation of GameElement in which the <code>update</code>
 * method updates the position each frame according to a <em>velocity</em> 
 * vector (deltaX, deltaY).  The units are assumed to be "pixels per frame".
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class MovingElement extends BasicElement
{

  /**
   * Constructs a MovingElement with a default velocity of zero in
   * both directions.
   * @param x
   *   x-coordinate of upper left corner
   * @param y
   *   y-coordinate of upper left corner
   * @param width
   *   object's width
   * @param height
   *   object's height
   */
	
	private double deltaX; //private variable to hold the deltaX value
	private double deltaY; //private variable to hold the deltaY value
	
  public MovingElement(double x, double y, int width, int height)
  {
    // TODO: everything
	  super(x, y, width, height);
	  deltaX = 0;
	  deltaY = 0;
  }
  
  public void setVelocity(double deltaX, double deltaY) {
	  this.deltaX = deltaX;
	  this.deltaY = deltaY;
  }
  
  public double getDeltaX() {
	  return deltaX;
  }
  
  public double getDeltaY() {
	  return deltaY;
  }

@Override
public void update() {
	// TODO Auto-generated method stub
	
	super.setPosition(super.getXReal() + deltaX, super.getYReal() + deltaY);
	super.update();
}
  
  // TODO: everything
 
}
