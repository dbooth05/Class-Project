package junitTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw3.MultiList;

class TestMultiList {

	static MultiList<Integer> ml;
	
	@BeforeEach
	void setUpBeforeClass() throws Exception {
		ml = new MultiList<Integer>(4);
		ml.add(1);
		ml.add(2);
		ml.add(3);
	}

	@Test
	void testSizesAdd() {
		assertEquals(3, ml.size());
	}
	
	@Test
	void testSizesAddOverNode() {
		ml.add(0);	// adds over a node
		ml.add(0);
		ml.add(0);
		assertEquals(6, ml.size());
	}
	
	@Test
	void testSizesBasicRemove() {
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.remove(0);		// base remove
		assertEquals(5, ml.size());
	}
	
	@Test
	void testSizesRemoveFullMerge() {
		ml.add(0);	// adds over a node
		ml.add(0);
		ml.add(0);
		ml.remove(0);
		ml.remove(0);	// full merge
		ml.remove(0);
		assertEquals(3, ml.size());
	}
	
	@Test
	void testSizesRemoveMiniMerge() {
		ml.add(0);	// adds over a node
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.remove(0);	// mini merge
		ml.remove(0);
		ml.remove(0);
		assertEquals(4, ml.size());
	}
	
	@Test
	void testSizesAddToPositionOverflow() {
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.add(5,5);
		assertEquals(9,ml.size());
	}
	
	@Test
	void testSizesAddToPrevious() {
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.add(0);
		ml.remove(0);
		ml.remove(0);
		ml.add(3,5);
		assertEquals(7,ml.size());
	}

	@Test
	void testAddingToEmptyList() {
		MultiList<Integer> mul = new MultiList<Integer>();
		assertEquals("[]", mul.toStringInternal());
		mul.add(1);
		assertEquals("[(1, -, -, -)]", mul.toStringInternal());
	}

	@Test
	void testAddingOverflowNode() {
		ml.add(1);
		ml.add(1);
		assertEquals("[(1, 2, 3, 1), (1, -, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemove() {
		ml.add(1);
		ml.add(1);
		ml.remove(0);
		assertEquals("[(2, 3, 1, -), (1, -, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveMiniMerge() {
		ml.add(1);
		ml.add(9);
		ml.add(4);
		ml.add(3);
		ml.add(5);
		ml.remove(0);
		ml.remove(0);
		ml.remove(0);
		assertEquals("[(1, 9, -, -), (4, 3, 5, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveFullMerge() {
		ml.add(1);
		ml.add(9);
		ml.remove(0);
		ml.remove(0);
		ml.remove(0);
		assertEquals("[(1, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveLastElement() {
		ml.add(1);
		ml.add(1);
		ml.remove(4);
		assertEquals("[(1, 2, 3, 1)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveThenAdd() {
		ml.add(1);
		ml.add(1);
		ml.remove(0);
		ml.add(5);
		assertEquals("[(2, 3, 1, -), (1, 5, -, -)]", ml.toStringInternal());
	}
	
//	@Test
//	void testRemoveThenAddThreeNodes() {
//
//		ml.add(1);
//		ml.add(1);
//		ml.add(1);
//		ml.add(1);
//		ml.remove(0);
//		ml.add(5);
//		assertEquals("[(2, 3, 1, -), (1, 1, 1, 1), (1, 5, -, -)]", ml.toStringInternal());
//	}
	
	@Test
	void testAddPosition() {
		ml.add(1);
		ml.add(1);
		ml.add(1);
		ml.add(5, 5);
		assertEquals("[(1, 2, 3, 1), (1, 5, 1, -)]", ml.toStringInternal());
	}
	
	@Test
	void testAddLastPosition() {
		ml.add(3, 5);
		assertEquals("[(1, 2, 3, 5)]", ml.toStringInternal());
	}
	
	@Test
	void testAddPositionWithGap() {
		ml.add(1);
		ml.add(1);
		ml.add(1);
		ml.remove(3);
		ml.add(3, 5);
		assertEquals("[(1, 2, 3, 5), (1, 1, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testAddPositionWithSkip() {
		ml.add(1);
		ml.add(1);
		ml.add(1);
		ml.remove(3);
		ml.add(6, 5);
		assertEquals("[(1, 2, 3, -), (1, 1, -, 5)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveWithOneElement() {
		MultiList<Integer> l = new MultiList<Integer>();
		l.add(1);
		l.remove(0);
		assertEquals("[]", l.toStringInternal());
	}
	
	@Test
	void testAddSplitLess() {
		ml.add(1);
		ml.add(1);
		ml.add(0,5);
		assertEquals("[(5, 1, 2, -), (3, 1, -, -), (1, -, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testAddSplitGreater() {
		ml.add(1);
		ml.add(1);
		ml.add(3,5);
		assertEquals("[(1, 2, -, -), (3, 5, 1, -), (1, -, -, -)]", ml.toStringInternal());
	}
}
