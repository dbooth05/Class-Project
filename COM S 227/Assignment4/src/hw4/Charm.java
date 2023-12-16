
package hw4;

import api.GameElement;

/**
 * Element associated with parent object such as a Platform or Elevator
 * that moves along with the parent and remains a fixed distance above it.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Charm extends MovingElement
{
	
	private int offset;
	private int hover;
	private GameElement parent;

  /**
   * Constructs a new Charm.  Before being added to a parent element
   * such as a Platform or Elevator, the x and y coordinates are zero.  
   * When a parent object is set, the initial x-coordinate becomes
   * the parent's x-coordinate, plus the given offset, and the y-coordinate
   * becomes the parent's y-coordinate, minus this element's height, minus
   * the hover amount.
   * @param width
   *   element's width
   * @param height
   *   element's height
   * @param offset
   *   when a parent is set, this amount is added to the parent's x-coordinate
   *   to calculate this element's x-coordinate
   * @param hover
   *   when a parent is set, this element's y-coordinate is the parent's
   *   y-coordinate, minus this element's height, minus the hover amount
   */
  public Charm(int width, int height, int offset, int hover)
  {
    // TODO: everything
	  super(0, 0, width, height);
	  this.offset = offset;
	  this.hover = hover;
  }
  
  // TODO: everything
  public void setParent(GameElement parent) {
	  this.parent = parent;
	  super.setPosition(parent.getXReal() + offset, parent.getYReal() - super.getHeight() - hover);
  }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		super.setPosition(parent.getXReal() + offset,  parent.getYReal() - super.getHeight() - hover);
	} 
  
}





















