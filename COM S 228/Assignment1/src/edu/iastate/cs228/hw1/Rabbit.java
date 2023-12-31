package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal 
{	
	/**
	 * Creates a Rabbit object.
	 * @param p: plain  
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Rabbit (Plain p, int r, int c, int a) 
	{
		// TODO 
		plain = p;
		row = r;
		column = c;
		age = a;
	}
		
	// Rabbit occupies the square.
	public State who()
	{
		// TODO  
		return State.RABBIT; 
	}
	
	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a fox.  
	 * @param pNew     plain of the next cycle 
	 * @return Living  new life form occupying the same square
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a rabbit. 
		int[] population = new int[5];
		census(population);
		Living thing;
		
		if(age == RABBIT_MAX_AGE) {
				thing = new Empty(pNew, row, column);
		}	else if(population[GRASS] < 1) {
				thing = new Empty(pNew, row, column);
		}	else if(population[BADGER] + population[FOX] >= population[RABBIT] && population[FOX] > population[BADGER]) {
				thing = new Fox(pNew, row, column, 0);
		}	else if(population[BADGER] > population[RABBIT]) {
				thing = new Badger(pNew, row, column, 0);
		}	else {
				age++;
				thing = new Rabbit(pNew, row, column, age);
		}
		return thing; 
	}
}
