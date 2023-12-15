package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		// TODO 
		plain = p;
		row = r;
		column = c;
		age = a;
	}
	
	/**
	 * A badger occupies the square. 	 
	 */
	public State who()
	{
		// TODO 			
		return State.BADGER; 
	}
	
	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a badger.
		int[] population = new int[5];
		census(population);
		Living thing;
		
		if(age == BADGER_MAX_AGE) { //dies
				thing = new Empty(pNew, row, column);
		}	else if(population[BADGER] == 1 && population[FOX] > 1) { //dies and turns into a Fox space
				thing = new Fox(pNew, row, column, 0);
		}	else if(population[BADGER] + population[FOX] > population[RABBIT]) { //turns into empty square
				thing = new Empty(pNew, row, column);
		}	else {
				age++;
				thing = new Badger(pNew, row, column, age);
		}
		return thing;
	}
}
