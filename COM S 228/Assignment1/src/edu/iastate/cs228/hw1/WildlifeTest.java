package edu.iastate.cs228.hw1;

import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * @author Dylan Booth
 */

public class WildlifeTest {

	Plain p;
	Plain p2;
	Plain p3;
	
	@BeforeEach
	public void prep() throws FileNotFoundException {
		p = new Plain("public1-3x3.txt");
		p2 = new Plain("public1-3x3.txt");
		p3 = new Plain("public1-5cycles.txt");
	}
	
	@Test 
	public void test1() { //tests updatePlain() method
		for(int i = 0; i < 5; i++) {
			if(i % 2 == 0) {
				Wildlife.updatePlain(p, p2);
			}
			else {
				Wildlife.updatePlain(p2,  p);
			}
		}
		assertEquals(p3.toString(), p2.toString());
	}
	
	@Test
	public void test2() throws FileNotFoundException {
		p = new Plain("public2-6x6.txt");
		p2 = new Plain("public2-6x6.txt");
		p3 = new Plain("public2-8cycles.txt");
		for(int i = 0; i < 8; i++) {
			if(i % 2 == 0) {
				Wildlife.updatePlain(p, p2);
			}
			else {
				Wildlife.updatePlain(p2,  p);
			}
		}
		assertEquals(p3.toString(), p.toString());
	}

}
