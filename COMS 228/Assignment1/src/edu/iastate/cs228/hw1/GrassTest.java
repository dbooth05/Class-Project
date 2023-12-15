package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class GrassTest {
	
	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public1-3x3.txt");
	}
	
	@Test
	public void test1() { //tests the constructor
		p.grid[2][0] = new Grass(p, 2, 0);
		assertEquals(State.GRASS, p.grid[2][0].who());
	}
	@Test
	public void test2() {
		p2.grid[2][2] = p.grid[2][2].next(p2);
		assertEquals(State.GRASS, p2.grid[2][2].who());
	}
	
}
