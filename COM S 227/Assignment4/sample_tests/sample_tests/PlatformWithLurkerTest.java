package sample_tests;
import hw4.Lurker;
import hw4.Platform;

public class PlatformWithLurkerTest
{
  public static void main(String[] args)
  {
      // first try an example where the parent doesn't move
      // left side at x = 50, width 10, right side at 60
      Platform p = new Platform(50, 200, 10, 10);
      p.setBounds(40, 70);
      
      // size 5 x 5, initial offset 2 units from left of platform
      Lurker e = new Lurker(5, 5, 2);
      e.setVelocity(2, 0);
      
      // this should automatically make p the parent of e
      // and the left and right sides of p the boundaries of e
      p.addChild(e);      
      System.out.println(e.getMin());  // 50
      System.out.println(e.getMax());  // 60
      
      // x position should be the parent position + 2 = 52
      // y position should be parent y - lurker height = 195
      System.out.println(e.getXReal());  // expected 52
      System.out.println(e.getYReal());  // expected 195
      
      // platform doesn't move here, but lurker does
      p.update();
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // expected 54, 59
      System.out.println(e.getDeltaX()); // expected 2.0
      p.update();
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5)); // expected 55, 60
      System.out.println(e.getDeltaX()); // expected -2.0
      System.out.println();

      
      // next, what if platform is moving, but lurker velocity is 0?
      // left side at x = 50, width 10, right side at 60
      p = new Platform(50, 200, 10, 10);
      p.setBounds(40, 70);
      p.setVelocity(3, 0);
      
      // size 5 x 5, initial offset 2 units from left of platform
      e = new Lurker(5, 5, 2);
      p.addChild(e);
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // expected 52, 57

      // when p moves, the boundaries of e should shift
      p.update();
      System.out.println("bounds " + e.getMin() + ", " + e.getMax()); // 53, 63

      // but e is still 2 units from left side
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // expected 55, 60
      System.out.println();

      
      // next, what if platform is moving, and lurker velocity is nonzero?
      // left side at x = 50, width 10, right side at 60
      p = new Platform(50, 200, 10, 10);
      p.setBounds(40, 70);
      p.setVelocity(3, 0);
      
      // size 5 x 5, initial offset 2 units from left of platform
      e = new Lurker(5, 5, 2);
      e.setVelocity(2, 0);
      p.addChild(e);
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // expected 52, 57
      
      p.update();
      // e is now 4 units from left bound, since its velocity is 2
      System.out.println("bounds " + e.getMin() + ", " + e.getMax()); // 53, 63
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // 57, 62

      p.update();
      // p has moved to [56, 66], e attempts to move another 2 units 
      // relative to p, to [62, 67], but hits the right boundary at 66
      // and reverses direction
      System.out.println("bounds " + e.getMin() + ", " + e.getMax()); // 56, 66
      System.out.println(e.getXReal() + ", " + (e.getXReal() + 5));  // 61, 66
      System.out.println("velocity " + e.getDeltaX());  // expected -2
  }
}
