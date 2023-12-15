package edu.iastate.cs228.hw3;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ListIterator;

public class Testing {

	public static void main(String[] args ) {
		
		MultiList ml;
		
		ml = new MultiList<Integer>(4);
		ml.add(3);
		ml.add(2);
		ml.add(1);
		
		ml.sort();
		
		System.out.println(ml.toStringInternal());
	}
	
}
