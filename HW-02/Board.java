// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid, gridBackup;
	private boolean DEBUG = true;
	boolean committed;

	/* private variables to make sure accessors work in constant time */
	// number of filled cells in each row
	private int[] rowWidths, rowWidthsBackup;
	// number of the highest cell in each column
	private int[] columnHeights, columnHeightsBackup;
	// maximum height out of all columns
	private int maxHeight, maxHeightBackup;

	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		
		// YOUR CODE HERE
		rowWidths = new int[height];
		columnHeights = new int[width];
		maxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			if(this.width != this.grid.length)
				throw new RuntimeException("!!!BOARD WIDTH INCORRECT!!!");
			if(this.height != this.grid[0].length)
				throw new RuntimeException("!!!BOARD HEIGHT INCORRECT!!!");

			int actualMaxHeight = 0;
			for(int i = 0; i < this.grid.length; i++) {
				int currHeight = 0;
				for(int j = this.grid[0].length - 1; j >= 0; j--) {
					if(this.grid[i][j]){
						currHeight = j + 1;
						break;
					}
				}
				if(currHeight > actualMaxHeight)
					actualMaxHeight = currHeight;
				if(this.columnHeights[i] != currHeight)
					throw new RuntimeException("!!!COLUMN " + i + " HEIGHT INCORRECT!!!" +
							" Expected: " + currHeight + ", Found: " + this.columnHeights[i]);
			}

			if(actualMaxHeight != this.maxHeight)
				throw new RuntimeException("!!!MAX HEIGHT INCORRECT!!!" +
						" Expected: " + actualMaxHeight + ", Found: " + this.maxHeight);

			for(int i = 0; i < this.grid[0].length; i++) {
				int currWidth = 0;
				for(int j = 0; j < this.grid.length; j++) {
					if(this.grid[j][i])
						currWidth++;
				}
				if(this.rowWidths[i] != currWidth)
					throw new RuntimeException("!!!ROW " + i + " WIDTH INCORRECT!!!" +
							" Expected: " + currWidth + ", Found: " + this.rowWidths[i]);
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		// if the piece dont fit in the board, just return the height of its first block
		if(x + piece.getWidth() > this.width)
			return piece.getSkirt()[0];

		// calculate the height that each origin of piece skirt would land at if dropped individually
		int[] skirt = piece.getSkirt();
		int[] landArray = new int[skirt.length];

		for(int i = 0; i < skirt.length; i++) {
			landArray[i] = this.getColumnHeight(x + i) - skirt[i];
		}

		// find the one with the highest landing height
		int highestLand = -2;

		for(int i = 0; i < landArray.length; i++) {
			if(landArray[i] > highestLand) {
				highestLand = landArray[i];
			}
		}

		return highestLand;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return this.columnHeights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		return this.rowWidths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(!inBounds(x, y))
			return true;

		return this.grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
			
		int result = PLACE_OK;

		this.gridBackup = new boolean[this.width][];
		for(int i = 0; i < this.width; i++) {
			this.gridBackup[i] = new boolean[this.height];
			System.arraycopy(this.grid[i], 0, this.gridBackup[i], 0, this.height);
		}
		this.rowWidthsBackup = new int[this.height];
		System.arraycopy(this.rowWidths, 0, this.rowWidthsBackup, 0, this.height);
		this.columnHeightsBackup = new int[this.width];
		System.arraycopy(this.columnHeights, 0, this.columnHeightsBackup, 0, this.width);
		this.maxHeightBackup = this.maxHeight;

		committed = false;

		
		TPoint[] pieceBody = piece.getBody();

		for (TPoint point : pieceBody) {
			int xInGrid = x + point.x, yInGrid = y + point.y;
			// check that this spot is in bounds
			if(!inBounds(xInGrid, yInGrid)){
				return PLACE_OUT_BOUNDS;
			}
			// check that this spot is free
			if(this.grid[xInGrid][yInGrid]) {
				return PLACE_BAD;
			}

			// mark the spot as filled
			grid[xInGrid][yInGrid] = true;
			// update the width and height
			this.rowWidths[yInGrid]++;
			if(yInGrid >= this.columnHeights[xInGrid])
				this.columnHeights[xInGrid] = yInGrid + 1;

			// update the result to note that a row was filled
			if(this.rowWidths[yInGrid] == this.width)
				result = PLACE_ROW_FILLED;
		}

		// update max height
		for(int i = 0; i < this.width; i++) {
			if(this.columnHeights[i] > maxHeight)
				maxHeight = this.columnHeights[i];
		}

		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed) {
			this.gridBackup = new boolean[this.width][];
			for(int i = 0; i < this.width; i++) {
				this.gridBackup[i] = new boolean[this.height];
				System.arraycopy(this.grid[i], 0, this.gridBackup[i], 0, this.height);
			}
			this.rowWidthsBackup = new int[this.height];
			System.arraycopy(this.rowWidths, 0, this.rowWidthsBackup, 0, this.height);
			this.columnHeightsBackup = new int[this.width];
			System.arraycopy(this.columnHeights, 0, this.columnHeightsBackup, 0, this.width);
			this.maxHeightBackup = this.maxHeight;
		}

		committed = false;

		int rowsCleared = 0;

		while(true) {
			int currentPass = clearLowestBunch();
			rowsCleared += currentPass;
			if(currentPass == 0)
				break;
		}

		sanityCheck();
		return rowsCleared;
	}

	/** helper function for clearRows(). clears the lowest bunch of filled rows
	 	@return the height of the bunch of cleared rows
	 */
	private int clearLowestBunch() {
		int bunchStart = -1, bunchEnd = -1, result = 0;

		// look for a filled row
		for(int i = 0; i < this.maxHeight; i++) {
			if(this.rowWidths[i] == this.width) {
				bunchStart = i;
				break;
			}
		}

		// return 0 to signify that no rows were cleared
		if(bunchStart == -1)
			return 0;

		// look for a non-filled row
		for(int i = bunchStart + 1; i < this.maxHeight; i++) {
			if(this.rowWidths[i] != this.width) {
				bunchEnd = i;
				break;
			}
		}

		// if there were no non-filled non-empty rows
		if(bunchEnd == -1) {
			result = this.maxHeight - bunchStart;
			// clear all rows from the bunch start up
			for(int i = bunchStart; i < this.maxHeight; i++) {
				for(int j = 0; j < this.width; j++) {
					this.grid[j][i] = false;
				}
				this.rowWidths[i] = 0;
			}
		} else {
			result = bunchEnd - bunchStart;

			// shift down all rows after the (to-be) cleared ones
			for(int i = bunchStart, lim = this.maxHeight - result; i < lim; i++) {
				for(int j = 0; j < this.width; j++) {
					this.grid[j][i] = this.grid[j][i + result];
					this.rowWidths[i] = this.rowWidths[i + result];
				}
			}
			// shift in empty rows from the top
			for(int i = this.maxHeight - result; i < this.maxHeight; i++) {
				for(int j = 0; j < this.width; j++) {
					this.grid[j][i] = false;
					this.rowWidths[i] = 0;
				}
			}
		}

		// update the column heights
		for(int i = 0; i < this.width; i++) {
			this.columnHeights[i] = -1;
			for(int j = this.maxHeight - 1; j >= 0; j--) {
				if(this.grid[i][j]) {
					this.columnHeights[i] = j;
					break;
				}
			}
			this.columnHeights[i]++;
		}
		// update the max height
		this.maxHeight -= result;
		// return the height of the bunch of rows
		return result;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed)
			return;

		this.maxHeight = this.maxHeightBackup;

		int[] tmp = this.rowWidths;
		this.rowWidths = this.rowWidthsBackup;
		this.rowWidthsBackup = tmp;

		tmp = this.columnHeights;
		this.columnHeights = columnHeightsBackup;
		this.columnHeightsBackup = tmp;

		boolean[][] temp = this.grid;
		this.grid = this.gridBackup;
		this.gridBackup = temp;

		committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		if(committed)
			return;

		committed = true;
	}

	/** private helper function to determine whether the given coordinates are in bounds of the grid*/
	private boolean inBounds(int x, int y) {
		if(x < 0 || x >= this.width || y < 0 || y >= this.height)
			return false;

		return true;
	}
	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


