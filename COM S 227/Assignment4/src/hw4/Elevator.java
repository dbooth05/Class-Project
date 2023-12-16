package hw4;

import api.GameElement;

/**
 * An Elevator is a GameElement with two distinctive behaviors.  First,
 * it can be set up to move vertically within a fixed set of boundaries.
 * On reaching a boundary, the y-component of its velocity is reversed.
 * Second, it maintains a list of <em>child</em> elements whose basic
 * motion all occurs relative to the Elevator.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Elevator extends Parent
{
  
  /**
   * Constructs a new Elevator.  Initially the upper and lower
   * boundaries are <code>Double.NEGATIVE_INFINITY</code> and
   * <code>Double.POSITIVE_INFINITY</code>, respectively.
   * @param x
   *   x-coordinate of initial position of upper left corner
   * @param y
   *   y-coordinate of initial position of upper left corner
   * @param width
   *   element's width
   * @param height
   *   element's height
   */
	
  public Elevator(double x, double y, int width, int height)
  {
    // TODO: everything
	  super(x, y, width, height);
  }

  // TODO: everything

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		if(super.getYReal() <= super.getMin()) {
			super.setVelocity(-super.getDeltaX(), -super.getDeltaY());
			super.setPosition(super.getXReal(), super.getMin());
		}
		else if(super.getYReal() + super.getHeight() >= super.getMax()) {
			super.setVelocity(-super.getDeltaX(), -super.getDeltaY());
			super.setPosition(super.getXReal(), super.getMax() - super.getHeight());
		}
		
		for(GameElement child : super.getChildren()) {
			child.update();
		}
}
  

}
