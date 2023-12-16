package hw1;

public class CyrideBus {
	
	public static final int BUS_GARAGE = -1;
	private int numStops;
	private int maxCapacity;
	private int numPassengers;
	private int stop;
	private int totalRiders;
	private boolean inService;
	private int capacity;
	
	public CyrideBus(int givenMaxCapacity, int givenNumStops) {
		
		numStops = givenNumStops;
		maxCapacity = givenMaxCapacity;
		stop = BUS_GARAGE;
		capacity = givenMaxCapacity;
		inService = true;
	}
	
	public int getCurrentCapacity() {
		
		return maxCapacity;
	}
	
	public int getCurrentStop() {
		
		return stop;
	}
	
	public int getNumPassengers() {
		
		return numPassengers;
	}
	
	public int getTotalRiders() {
		
		return totalRiders;
	}
	
	public boolean isInService() {
		
		return inService;
	}
	
	public void nextStop(int peopleOff, int peopleOn) {		
		
		/*stop++;
		if(stop == (numStops)) {
			stop = 0;
		}
		*/
		
		numPassengers = Math.max(0, (numPassengers - peopleOff));
		
		peopleOn = Math.min(maxCapacity - numPassengers, peopleOn);
		
		numPassengers = Math.min(maxCapacity, (numPassengers + peopleOn));
		
		totalRiders = totalRiders + peopleOn;
		
		stop++;
		stop = Math.min(stop, stop % numStops);
		
	}
	public void placeInService() {
		
		inService = true;	
		maxCapacity = capacity;
	}
	
	public void removeFromService() {
		
		inService = false;
		maxCapacity = 0;
		stop = BUS_GARAGE;
		numPassengers = 0;
	}
	

}
