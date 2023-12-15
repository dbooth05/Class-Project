package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class BadgerTest {
	
	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public1-3x3.txt");
	}
	
	@Test
	public void test1() { //testing the constructor and myAge() method
		p.grid[2][1] = new Badger(p, 2, 1, 0);
		assertEquals(State.BADGER, p.grid[2][1].who());
		assertEquals(0, ((Badger) p.grid[2][1]).myAge());
	}
	
	@Test
	public void test2() { //testing the next method
		p2.grid[0][1] = p.grid[0][1].next(p2);
		assertEquals(0, ((Fox) p2.grid[1][0]).myAge());
	}

}
