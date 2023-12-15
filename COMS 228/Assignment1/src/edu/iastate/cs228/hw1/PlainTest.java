package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

/*
 * @author Dylan Booth
 */
public class PlainTest {
	
	Plain p;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public2-6x6.txt");
	}
	
	@Test
	public void test1() {	//tests finding locations and types
		assertEquals(State.EMPTY, p.grid[0][1].who());
	}
	@Test
	public void test2() {	//tests finding locations and types
		assertEquals(State.BADGER, p.grid[1][0].who());
		assertEquals(0, ((Badger) p.grid[1][0]).myAge());
	}
	@Test
	public void test3() {	//tests finding locations and types
		assertEquals(State.GRASS, p.grid[5][0].who());
	}
	
	@Test 
	public void test4() { //testing the constructor method for an empty plain with width 5
		p = new Plain(5);
		p.randomInit();
		assertEquals(5, p.getWidth());
	}	

}
