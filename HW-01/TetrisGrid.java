//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

import java.util.Arrays;

public class TetrisGrid {

	private boolean[][] grid;
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		// copy the given grid into private grid
		this.grid = new boolean[grid.length][];
		for(int i = 0; i < this.grid.length; i++)
			this.grid[i] = Arrays.copyOf(grid[i], grid[i].length);
	}

	// shifts down height amount of rows from the top
	private void shiftDown(int height){
		// iterate and for each row copy the next one into current one
		for(int i = grid[0].length - height - 1; i < grid[0].length - 1; i++){
			for(int j = 0; j < grid.length; j++){
				grid[j][i] = grid[j][i + 1];
			}
		}

		// set the last row as all false
		for(int j = 0; j < grid.length; j++){
			grid[j][grid[0].length - 1] = false;
		}
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		// iterate over the rows
		outerLoop:
		for(int i = 0; i < grid[0].length; i++){
			for(int j = 0; j < grid.length; j++){
				// move to the next row if the current one isn't to be cleared
				if(!grid[j][i])
					continue outerLoop;
			}
			// if we got here, then the current row is full and needs to be cleared
			// shift down all the rows above current one
			shiftDown(grid[0].length - i - 1);
			// current row is now updated, so move the iterator behind it so that it gets to it on the next iteration
			i--;
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid;
	}
}
