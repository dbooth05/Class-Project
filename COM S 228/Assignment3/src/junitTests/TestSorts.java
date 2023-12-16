package junitTests;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw3.MultiList;

class TestSorts {

	static MultiList<Integer> ml;

	@BeforeEach
	void setUpBeforeClass() throws Exception {
		ml = new MultiList<Integer>(4);
		ml.add(3);
		ml.add(1);
		ml.add(2);
		ml.add(9);
		ml.add(7);
		ml.add(3);
	}

	@Test
	void testInsertionSort() {
		ml.sort();
		assertEquals("[(1, 2, 3, 3), (7, 9, -, -)]", ml.toStringInternal());	
	}
	
	@Test
	void testBubbleSort() {
		ml.sortReverse();
		assertEquals("[(9, 7, 3, 3), (2, 1, -, -)]", ml.toStringInternal());	
	}

}
