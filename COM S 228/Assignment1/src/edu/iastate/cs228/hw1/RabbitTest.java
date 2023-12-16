package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class RabbitTest {
	
	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public1-3x3.txt");
	}
	
	@Test
	public void test1() { //Tests constructor and who() method
		assertEquals(State.EMPTY, p.grid[2][1].who());
		p.grid[0][0] = new Empty(p, 0, 0);
		assertEquals(State.EMPTY, p.grid[0][0].who());
	}
	
	@Test
	public void test2() {
		p2.grid[2][1] = p.grid[2][1].next(p2);
		assertEquals(State.FOX, p2.grid[2][1].who());
	}
	
}
