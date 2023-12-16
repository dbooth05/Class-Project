package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 *  @author Dylan Booth
 */

public class AnimalTest {	
	
	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public3-10x10.txt");
	}
	
	@Test
	public void test1() {
		assertEquals(0, ((Badger) p.grid[0][1]).myAge());
	}
	
	@Test 
	public void test2() {
		assertEquals(0, ((Fox) p.grid[1][1]).myAge());
	}
	
	@Test 
	public void test3() {
		assertEquals(1, ((Fox) p2.grid[4][1]).myAge());
	}
	
	@Test
	public void test4() {
		assertEquals(3, ((Rabbit) p2.grid[0][7]).myAge());
	}

}
