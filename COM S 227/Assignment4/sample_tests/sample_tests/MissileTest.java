package sample_tests;

import hw4.Missile;

public class MissileTest
{
  public static void main(String[] args)
  {
    Missile p = new Missile(0, 0, 0, 0);
    p.setVelocity(2, 3);
    p.update();
    System.out.println(p.getYReal()); // expected 3
    System.out.println(p.getDeltaY());// expected 3
    p.update();
    System.out.println(p.getYReal()); // expected 6
    System.out.println(p.getDeltaY());// expected 3
    p.setGravity(5);
    p.update();
    System.out.println(p.getYReal());  // 6 + 3 = 9
    System.out.println(p.getDeltaY()); // 3 + 5 = 8
    p.update();
    System.out.println(p.getYReal());  // 9 + 8 = 17
    System.out.println(p.getDeltaY()); // 8 + 5 = 13
    p.update();
    System.out.println(p.getYReal());  // 17 + 13 = 30
    System.out.println(p.getDeltaY()); // 13 + 5 = 18
    
  }
}
