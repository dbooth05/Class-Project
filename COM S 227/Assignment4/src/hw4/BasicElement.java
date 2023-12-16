package hw4;

import java.awt.Rectangle;

import api.*;
/**
 * Minimal concrete extension of GameElement. The <code>update</code>
 * method in this implementation just increments the frame count.
 * 
 * @author Dylan Booth
 */
// TODO: This class must directly or indirectly extend GameElement
public class BasicElement extends GameElement
{
  
  /**
   * Constructs a new BasicElement.
   * @param x
   *   x-coordinate of upper left corner
   * @param y
   *   y-coordinate of upper left corner
   * @param width
   *   element's width
   * @param height
   *   element's height
   */
	
	private double x; //instance double to hold real x position
	private double y; //instance double to hold real y position
	private int width; //instance int to hold width of object
	private int height; //instance int to hold height of object
	private int frameCount; //instance int to hold frame count
	private boolean marked; //instance boolean to hold value to determine if time to delete
	
  public BasicElement(double x, double y, int width, int height)
  {
    // TODO: everything
	 this.x = x;
	 this.y = y;
	 this.width = width;
	 this.height = height;
	 frameCount = 0;
	 marked = false;
  }

	@Override
	public int getXInt() {
		// TODO Auto-generated method stub
		return (int) Math.round(x);
	}
	
	@Override
	public int getYInt() {
		// TODO Auto-generated method stub
		return (int) Math.round(y);
	}
	
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}
	
	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}
	
	@Override
	public Rectangle getRect() {
		// TODO Auto-generated method stub
		Rectangle r = new Rectangle(getXInt(), getYInt(), width, height);
		return r;
	}
	
	@Override
	public void setPosition(double newX, double newY) {
		// TODO Auto-generated method stub
		x = newX;
		y = newY;
	}
	
	@Override
	public double getXReal() {
		// TODO Auto-generated method stub
		return x;
	}
	
	@Override
	public double getYReal() {
		// TODO Auto-generated method stub
		return y;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		frameCount++;
		
	}
	
	@Override
	public int getFrameCount() {
		// TODO Auto-generated method stub
		return frameCount;
	}
	
	@Override
	public boolean isMarked() {
		// TODO Auto-generated method stub
		return marked;
	}
	
	@Override
	public void markForDeletion() {
		// TODO Auto-generated method stub
		marked = true;
	}

	@Override
	public boolean collides(GameElement other) {
		
		return (getRect().intersects(other.getRect()));
	}
  // TODO: everything
}
