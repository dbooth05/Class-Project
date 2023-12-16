package hw4;

import api.GameElement;

/**
 * A Platform is a GameElement with two distinctive behaviors.  First,
 * it can be set up to move horizontally within a fixed set of boundaries.
 * On reaching a boundary, the x-component of its velocity is reversed.
 * Second, it maintains a list of <em>child</em> elements whose basic
 * motion all occurs relative to the Platform.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Platform extends Parent
{
	
  /**
   * Constructs a new Platform. Initially the left and right
   * boundaries are <code>Double.NEGATIVE_INFINITY</code> and
   * <code>Double.POSITIVE_INFINITY</code>, respectively.
   * @param x
   *   x-coordinate of initial position of upper left corner
   * @param y
   *   y-coordinate of initial position of upper left corner
   * @param width
   *   object's width
   * @param height
   *   object's height
   */
  public Platform(double x, double y, int width, int height)
  {
	  // TODO: everything
	  super(x, y, width, height);
  }
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		if(super.getXReal() <= super.getMin()) {
			super.setVelocity(-super.getDeltaX(), 0);
			super.setPosition(super.getMin(), super.getYReal());
		}
		else if(super.getXReal() + super.getWidth() >= super.getMax()) {
			super.setVelocity(-super.getDeltaX(), 0);
			super.setPosition(super.getMax() - super.getWidth(), super.getYReal());
		}
		
		for(GameElement child : super.getChildren()) {
			child.setPosition(child.getXReal() + super.getDeltaX(), child.getYReal());
			child.update();
		}		
	}
	

	
  
}