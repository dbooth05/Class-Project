package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class FoxTest {

	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public-3x3.txt");
		p2 = new Plain("public-3x3.txt");
	}
	
	@Test
	public void test1() { //testing constructor and myAge()
		p.grid[2][2] = new Fox(p, 2, 2, 0);
		assertEquals(State.FOX, p.grid[2][2].who());
		assertEquals(0, ((Fox) p.grid[2][2]).myAge());
	}
	
	@Test
	public void test2() { //testing next method
		p2.grid[2][0] = p.grid[2][0].next(p2);
		assertEquals(0, ((Fox) p2.grid[2][0]).myAge());
	}
	
}
