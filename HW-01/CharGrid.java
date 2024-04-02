// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CharGrid {

	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		// store x and y for every occurrence of the character
		List<Integer> Xs = new ArrayList<Integer>();
		List<Integer> Ys = new ArrayList<Integer>();

		// iterate over the whole grid, looking for the given character
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[0].length; j++){
				if(grid[i][j] == ch) {
					Xs.add(i);
					Ys.add(j);
				}
			}
		}

		// return 0 if no occurrences
		if(Xs.isEmpty() && Ys.isEmpty())
			return 0;

		// find lowest and highest Xs and Ys
		int minX = Collections.min(Xs), maxX = Collections.max(Xs), minY = Collections.min(Ys), maxY = Collections.max(Ys);

		// return product of the longest distances between Xs and Ys
		return (maxX - minX + 1) * (maxY - minY + 1);
	}

	/**
	 * Takes as input the coordinates of a character and sees if there is a plus around it.
	 * Assumes that the given index is valid.
	 * @return size of arms of the plus if there is one, 0 if there is none
	 * */
	private int getPlusAt(int x, int y){
		int size = 1;

		// iterate from 1 up and check all characters in all 4 directions with i distance from (x,y)
		for(int i = 1; true; i++){
			// count of matching characters in current iteration
			int currCount = 0;

			// check the character in all 4 directions
			if(x - i >= 0 && grid[x - i][y] == grid[x][y])
				currCount++;
			if(x + i < grid.length && grid[x + i][y] == grid[x][y])
				currCount++;
			if(y - i >= 0 && grid[x][y - i] == grid[x][y])
				currCount++;
			if(y + i < grid[x].length && grid[x][y + i] == grid[x][y])
				currCount++;

			// end the loop if no matching characters
			if(currCount == 0)
				break;

			// this means uneven arms, so no plus
			if(currCount != 4)
				return 0;

			// increase the size if we found a matching character in every direction
			size++;
		}

		// return 0 if there were no arms
		if(size == 1)
			return 0;

		// return the size of arms if we found a plus
		return size;
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		// can't have '+' figures without height and width of at least 3
		if(grid.length < 3 || grid[0].length < 3)
			return 0;

		int res = 0;

		// iterate over the entire grid
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				// increment the result if we found a plus around the current index
				if(getPlusAt(i, j) > 0)
					res++;
			}
		}

		return res;
	}
	
}
