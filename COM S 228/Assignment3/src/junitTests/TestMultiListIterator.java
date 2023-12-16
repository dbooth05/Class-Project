
package junitTests;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw3.MultiList;

class TestMultiListIterator {

	static ListIterator<Integer> multiIterator;
	static MultiList<Integer> ml;
	
	@BeforeEach
	void setUpBeforeClass() throws Exception {
		ml = new MultiList<Integer>();

		for(int i = 0; i < 10; i++) {
			ml.add(i);
		}
		
		ml.remove(1);
		ml.remove(0);
		ml.remove(5);
		
		multiIterator = ml.listIterator();
	}
	
	@Test
	void testIteratorStartAtPosition() {
		multiIterator = ml.listIterator(1);
		assertEquals("[(2, | 3, -, -), (4, 5, 6, -), (8, 9, -, -)]", ml.toStringInternal(multiIterator));
	}

	@Test
	void testMultiIteratorForward() {
		StringBuilder acc = new StringBuilder();
		while(multiIterator.hasNext()) {
			String line = multiIterator.next().toString();
			acc.append(line);
		}
		assertEquals("2345689", acc.toString());
	}
	
	@Test
	void testMultiIteratorNextIndex() {
		StringBuilder acc = new StringBuilder();
		while(multiIterator.hasNext()) {
			acc.append(((ListIterator<Integer>) multiIterator).nextIndex());
			multiIterator.next();
		}
		assertEquals("0123456", acc.toString());
	}
	
	@Test
	void testMultiIteratorBackward() {
		StringBuilder acc = new StringBuilder();
		multiIterator = ml.listIterator(ml.size());
		while(multiIterator.hasPrevious()) {
			String line = multiIterator.previous().toString();
			acc.append(line);
		}
		assertEquals("9865432", acc.toString());
	}
	
	@Test
	void testMultiIteratorPreviousIndex() {
		multiIterator = ml.listIterator(ml.size());
		StringBuilder acc = new StringBuilder();
		while(multiIterator.hasPrevious()) {
			acc.append(multiIterator.previousIndex());
			multiIterator.previous();
		}
		assertEquals("6543210", acc.toString());
	}
	
	@Test
	void testHasPrevious() {
		assertTrue(!multiIterator.hasPrevious());
		multiIterator.next();
		assertTrue(multiIterator.hasPrevious());
	}
	
	@Test
	void testHasNext() {
		MultiList<Integer> ml2 = new MultiList<Integer>();
		ml2.add(1);
		
		ListIterator<Integer> it = ml2.listIterator();
		
		assertTrue(it.hasNext());
	}
	
	// System.out.println(ml.toStringInternal(multiIterator));
	@Test
	void testSet() {
		multiIterator.next();
		multiIterator.set(9);
		assertEquals("[(9, 3, -, -), (4, 5, 6, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testSetOverNode() {
		multiIterator.next();
		multiIterator.next();
		multiIterator.set(9);
		assertEquals("[(2, 9, -, -), (4, 5, 6, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
//	@Test
//	void testSetMultipleTimes() {
//		try {
//			multiIterator.next();
//			multiIterator.set(9);
//			multiIterator.set(2);
//		} catch (IllegalStateException e) {
//			return;
//		}
//		fail("should throw illegal state exception when setting multiple times with no previous or next");
//	}
	
	@Test
	void testAdd() {
		multiIterator.add(9);
		assertEquals("[(9, 2, 3, -), (4, 5, 6, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testAddMultipleTimes() {
		multiIterator.add(9);
		multiIterator.add(9);
		multiIterator.add(9);
		assertEquals("[(9, 9, 9, -), (2, 3, -, -), (4, 5, 6, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemove() {
		multiIterator.next();
		multiIterator.remove();
		assertEquals("[(3, 4, -, -), (5, 6, -, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveOverNode() {
		multiIterator.next();
		multiIterator.next();
		multiIterator.remove();
		assertEquals("[(2, 4, -, -), (5, 6, -, -), (8, 9, -, -)]", ml.toStringInternal());
	}
	
	@Test
	void testRemoveMultipleTimes() {
		try {
			multiIterator.next();
			multiIterator.remove();
			multiIterator.remove();
		} catch (IllegalStateException e) {
			return;
		}
		fail("should throw illegal state exception when removing multiple times with no previous or next");
	}
}
