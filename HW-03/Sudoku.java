import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {

	public class Spot {
		private int row, col, value;
		private boolean isSet;

		public Spot(int row, int col) {
			this.row = row;
			this.col = col;

			this.value = 0;
			this.isSet = false;
		}

		public boolean isSet() {
			return isSet;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}

		public int getValue() {return value;}

		/**
		 * Return the index of the part of board that this spot is in.
		 * Index is based on numbering left-to-right top-to-bottom.
		 * */
		public int getPartIndex() {
			return (row / PART) * (SIZE / PART) + (col / PART);
		}

		/**
		 * Return the index of the top left corner of the part
		 * */
		public Map.Entry<Integer, Integer> getPartStart() {
			int partIndex = this.getPartIndex();
			int rowIndex = partIndex / PART * PART;
			int colIndex = partIndex % (SIZE / PART) * PART;
			return new AbstractMap.SimpleEntry<Integer, Integer>(
					rowIndex, colIndex
			);
		}

		public void set(int value) {
			this.isSet = true;
			this.value = value;
			grid[row][col] = value;
		}

		public void unset() {
			this.isSet = false;
			this.value = 0;
			grid[row][col] = 0;
		}
	}

	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");


	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");


	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	private int[][] grid;
	private List<Spot> spots;
	private int solutionCount;
	private String solutionText;
	private long elapsed;
	private boolean foundMaxSolutions = false;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		this.grid = ints;
		this.solutionText = "";
		this.elapsed = -1;

		this.spots = new ArrayList<>();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				Spot currSpot = new Spot(i, j);
				if(this.grid[i][j] != 0)
					currSpot.set(this.grid[i][j]);
				spots.add(currSpot);
			}
		}
	}

	/**
	 * Sets up based on the given string: Parses ints from it and calls the constructor with ints.
	 * */
	public Sudoku(String text) {
		this(textToGrid(text));
	}
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long startTime = System.currentTimeMillis();
		this.solutionText = "";
		this.solutionCount = 0;

		if(isGridValid())
			solveRec(0);

		this.elapsed = System.currentTimeMillis() - startTime;
		return this.solutionCount;
	}

	public String getSolutionText() {
		return this.solutionText;
	}

	public long getElapsed() {
		return elapsed;
	}

	private void solveRec(int spotIndex) {
		if(foundMaxSolutions)
			return;

		if(spotIndex >= spots.size()) {
			this.solutionCount++;

			if(this.solutionCount == MAX_SOLUTIONS)
				foundMaxSolutions = true;

			if(this.solutionCount == 1) {
				this.solutionText = stringFromGrid(this.grid);
			}
			return;
		}

		Spot currSpot = this.spots.get(spotIndex);

		if(currSpot.isSet()) {
			solveRec(spotIndex + 1);
		}else {

			Set<Integer> possibleValues = possibleNumbers(currSpot, this.grid);

			for (int number : possibleValues) {
				currSpot.set(number);
				solveRec(spotIndex + 1);
				currSpot.unset();
			}
		}
	}

	private boolean isGridValid() {
		for(Spot spot : this.spots) {
			if(spot.isSet() && !possibleNumbers(spot, grid).contains(spot.getValue()))
				return false;
		}
		return true;
	}

	static Set<Integer> possibleNumbers(Spot spot, int[][] grid) {
		Set<Integer> notAllowed = new HashSet<>();
		notAllowed.add(0);

		for(int i = 0; i < grid.length; i++) {
			if(i != spot.row)
				notAllowed.add(grid[i][spot.col]);
		}
		for(int i = 0; i < grid[spot.row].length; i++) {
			if(i != spot.col)
				notAllowed.add(grid[spot.row][i]);
		}
		Map.Entry<Integer, Integer> partStart = spot.getPartStart();
		for(int i = partStart.getKey(); i < partStart.getKey() + 3; i++) {
			for(int j = partStart.getValue(); j < partStart.getValue() + 3; j++) {
				if(i != spot.row && j != spot.col)
					notAllowed.add(grid[i][j]);
			}
		}

		Set<Integer> res = new HashSet<>();
		for(int i = 1; i <= 9; i++) {
			if(!notAllowed.contains(i)) {
				res.add(i);
			}
		}

		return res;
	}

	protected static int[][] copyOfGrid(int[][] grid) {
		int[][] res = new int[grid.length][];

		for(int i = 0; i < res.length; i++) {
			res[i] = new int[grid[i].length];
			System.arraycopy(grid[i], 0, res[i], 0, res[i].length);
		}

		return  res;
	}

	protected static String stringFromGrid(int[][] grid) {
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				sb.append(" " + Integer.toString(grid[i][j]));
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		return stringFromGrid(this.grid);
	}

}
