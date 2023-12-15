package edu.iastate.cs228.hw1;

/**
 *  
 * @author Dylan Booth
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is eaten. 
 *
 */
public class Grass extends Living 
{
	public Grass (Plain p, int r, int c) 
	{
		// TODO 
		plain = p;
		row = r;
		column = c;
	}
	
	public State who()
	{
		// TODO  
		return State.GRASS; 
	}
	
	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast enough to take over Grass.
	 */
	public Living next(Plain pNew)
	{
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for grass.
		int[] population = new int[5];
		census(population);
		Living thing;
		
		if(population[RABBIT] >= population[GRASS] * 3) {
				thing = new Empty(pNew, row, column);
		}	else if(population[RABBIT] >= 3) {
				thing = new Rabbit(pNew, row, column, 0);
		}	else {
				thing = new Grass(pNew, row, column);
		}
		return thing; 
	}
}
