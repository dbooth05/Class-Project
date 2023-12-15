package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

/** 
 * Empty squares are competed by various forms of life.
 */
public class Empty extends Living 
{
	public Empty (Plain p, int r, int c) 
	{
		// TODO
		plain = p;
		row = r;
		column = c;
	}
	
	public State who()
	{
		// TODO 
		return State.EMPTY; 
	}
	
	/**
	 * An empty square will be occupied by a neighboring Badger, Fox, Rabbit, or Grass, or remain empty. 
	 * @param pNew     plain of the next life cycle.
	 * @return Living  life form in the next cycle.   
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for corresponding survival rules. 
		int[] population = new int[5];
		census(population);
		Living thing = null;
		
		if(population[RABBIT] > 1) {
				thing = new Rabbit(pNew, row, column, 0);
		}	else if(population[FOX] > 1) {
				thing = new Fox(pNew, row, column, 0);
		}	else if(population[BADGER] > 1) {
				thing = new Badger(pNew, row, column, 0);
		}	else if(population[GRASS] >= 1) {
				thing = new Grass(pNew, row, column);
		}	else {
				thing = new Empty(pNew, row, column);
		}
		return thing; 
	}
}
