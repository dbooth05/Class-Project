
package hw4;

/**
 * Implementation of GameElement that does not move. Instead, it is intended to
 * appear on the screen for a fixed number of frames.
 * 
 * @author Dylan Booth
 */
//TODO: This class must directly or indirectly extend GameElement
public class Flash extends BasicElement
{

	private int lifeFrames;
  /**
   * Constructs a new Flash.
   * 
   * @param x
   *          x-coordinate of upper left corner
   * @param y
   *          y-coordinate of upper left corner
   * @param width
   *          element's width
   * @param height
   *          element's height
   * @param initialLife
   *          the number of frames until this element marks itself for deletion
   */
  public Flash(double x, double y, int width, int height, int initialLife)
  {
    // TODO: everything
	  super(x, y, width, height);
	  
	  this.lifeFrames = initialLife;
  }
  
  public int getRemainingLife() {
	  return lifeFrames;
  }
  
  // TODO: everything
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		
		lifeFrames--;
		
		if(lifeFrames <= 0) {
			super.markForDeletion();
		}
	}
}
