
package hw4;

import api.GameElement;

/**
 * Moving element associated with a Platform or Elevator.  Optionally,
 * a Lurker can be given a nonzero horizontal velocity and it will oscillate
 * between the left and right edges of the Platform or Elevator.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Lurker extends MovingElement
{
	
	private int initOffset;
	private double min;
	private double max;
	private GameElement parent;
	
  /**
   * Constructs a new Lurker.  Before being added to a parent element
   * such as a Platform or Elevator, the x and y coordinates are zero.  
   * When a parent element is set, the initial x-coordinate becomes
   * the parent's x-coordinate, plus the given offset, and the y-coordinate
   * becomes the parent's y-coordinate, minus this element's height.
   * @param width
   *   element's width
   * @param height
   *   element's height
   * @param initialOffset
   *   when a parent is set, this amount is added to the parent's x-coordinate
   *   to calculate this element's initial x-coordinate
   */
  public Lurker(int width, int height, int initialOffset)
  {
    // TODO: everything
	  super(0, 0, width, height);
	  initOffset = initialOffset;
	  min = 0;
	  max = 0;
	  parent = null;
  }
  
  public void setBounds(double min, double max) {
	  this.min = min;
	  this.max = max;
  }
  
  public double getMax() {
	  return max;
  }
  
  public double getMin() {
	  return min;
  }

  public void setParent(GameElement parent) {
	  this.parent = parent;	
	  super.setPosition(parent.getXReal() + initOffset, parent.getYReal() - super.getHeight());
	  setBounds(parent.getXReal(), parent.getXReal() + parent.getWidth());
  }

  // TODO: everything

  @Override
  public void update()
  {
      setBounds(parent.getXReal(),parent.getXReal() + parent.getWidth());
      
      super.update();
      super.setPosition(super.getXReal(),  parent.getYReal() - super.getHeight());
      if(super.getDeltaX() != 0) {
    	  if(super.getXReal() + super.getWidth() > max) {
    		  super.setPosition(max - super.getWidth(), super.getYReal());
    		  super.setVelocity(super.getDeltaX() * -1, super.getDeltaY());
    	  }
    	  else if(super.getXReal() < min) {
    		  super.setPosition(min + initOffset, super.getYReal());
    		  super.setVelocity(super.getDeltaX() * -1, super.getDeltaY());
    	  }
      }
      else if(super.getDeltaX() == 0) {
    	  super.setPosition(min + initOffset, super.getYReal());
      }
      
  }  
}
















