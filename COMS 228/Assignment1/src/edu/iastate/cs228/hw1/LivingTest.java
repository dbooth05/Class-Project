package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class LivingTest {
	
	Plain p;
	Plain p2;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public1-3x3.txt");
	}
	
	@Test 
	public void test1() {
		int[] arr = new int[5];
		p.grid[0][0].census(arr);
		int[] result = {1, 0, 2, 1, 0};
		assertArrayEquals(result, arr);
	}
	
	@Test
	public void test2() {
		int[] arr = new int[5];
		p.grid[1][1].census(arr);
		int[] result = { 1, 1, 4, 2, 1};
		assertArrayEquals(result, arr);
	}

}
