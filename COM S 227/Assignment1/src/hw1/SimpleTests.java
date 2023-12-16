
import hw1.CyrideBus;

/**
 * A few simple tests for the CyrideBus class.
 */
public class SimpleTests
{
  public static void main(String[] args)
  {
    // incrementing stops
    CyrideBus b = new CyrideBus(10, 3);
    System.out.println(b.getCurrentStop());
    System.out.println("Expected -1");  // starts out at BUS_GARAGE
    b.nextStop(0, 0);
    b.nextStop(0, 0);
    System.out.println(b.getCurrentStop());
    System.out.println("Expected 1");
    b.nextStop(0, 0);
    b.nextStop(0, 0);
    b.nextStop(0, 0);
    b.nextStop(0, 0);
    System.out.println(b.getCurrentStop());
    System.out.println("Expected 2");   
    
    // adding passengers
    b = new CyrideBus(10, 3);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 0");
    b.nextStop(0, 5);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 5");
    b.nextStop(0, 2);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 7");
    b.nextStop(0, 5);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 10");

    // removing and adding passengers
    b = new CyrideBus(10, 3);
    b.nextStop(0, 5);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 5");
    b.nextStop(2, 20);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 10");
    b.nextStop(20, 4);
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 4");
    
    // and check the total riders too...
    System.out.println(b.getTotalRiders());
    System.out.println("Expected 16");

    // taking out of service should set current capacity to zero
    b = new CyrideBus(60, 12);
    b.removeFromService();
    System.out.println(b.getCurrentCapacity());
    System.out.println("Expected 0");
    b.placeInService();
    System.out.println(b.getCurrentCapacity());
    System.out.println("Expected 60");
    
    // and should also discharge all passengers and return to bus garage
    b = new CyrideBus(10, 3);
    b.nextStop(0, 5);
    b.nextStop(0, 2);
    b.removeFromService();
    System.out.println(b.getNumPassengers());
    System.out.println("Expected 0");
    System.out.println(b.getCurrentStop());
    System.out.println("Expected -1");
    
  }
}
