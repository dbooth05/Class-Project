package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

/**
 * A fox eats rabbits and competes against a badger. 
 */
public class Fox extends Animal 
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Fox (Plain p, int r, int c, int a) 
	{
		// TODO 
		plain = p;
		row = r;
		column = c;
		age = a;
	}
		
	/**
	 * A fox occupies the square. 	 
	 */
	public State who()
	{
		// TODO 
		return State.FOX; 
	}
	
	/**
	 * A fox dies of old age or hunger, or from attack by numerically superior badgers. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a fox. 
		int[] population = new int[5];
		census(population);
		Living thing;
		
		if(age == FOX_MAX_AGE) {
				thing = new Empty(pNew, row, column);
		}	else if(population[BADGER] > population[FOX]) {
				thing = new Badger(pNew, row, column, 0);
		}	else if(population[BADGER] + population[FOX] > population[RABBIT]) { //turns into empty square
				thing = new Empty(pNew, row, column);
		}	else {
				age++;
				thing = new Fox(pNew, row, column, age);
		}
		return thing; 
	}
}
