package junitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw3.MultiList;

class TestMultiIterator {

	static Iterator<Integer> iterator;
	static MultiList<Integer> ml;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ml = new MultiList<Integer>();

		for(int i = 0; i < 10; i++) {
			ml.add(i);
		}
		
		ml.remove(1);
		ml.remove(0);
		ml.remove(5);
		
		iterator = ml.iterator();
	}
	
	@Test
	void testBasicIterator() {	// only checks next() and hasNext()
		StringBuilder acc = new StringBuilder();
		for(Integer i : ml) {
			acc.append(i);
		}
		assertEquals("2345689", acc.toString());
	}
}
