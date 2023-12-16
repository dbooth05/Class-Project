package hw4;

/**
 * Moving element in which the vertical velocity is adjusted
 * each frame by a gravitational constant to simulate gravity.
 * It may also be put into a "ballistic" state in which 
 * no other modifications to velocity take place other than gravity.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Missile extends MovingElement
{

	
	private double gravityConstant;
	
	private boolean isBallistic;
  /**
   * Constructs a new Missile.
   * @param x
   *   x-coordinate of upper left corner
   * @param y
   *   y-coordinate of upper left corner
   * @param width
   *   element's width
   * @param height
   *   element's height
   */
  public Missile(double x, double y, int width, int height)
  {
    // TODO: everything
	  super(x, y, width, height);
	  gravityConstant = 0;
	  isBallistic = false;
  }

  public void setGravity(double gravityConstant) {
	  this.gravityConstant = gravityConstant;
  }
  
  public boolean isBallistic() {
	  return isBallistic;
  }
  
  public void setBallistic(boolean isBallistic) {
	  this.isBallistic = isBallistic;
  }

@Override
public void update() {
	// TODO Auto-generated method stub
	super.update();
	
	super.setVelocity(super.getDeltaX(), super.getDeltaY() + gravityConstant);
}
  
  
  // TODO: everything

}
