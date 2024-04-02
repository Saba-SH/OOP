import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.*;

public class TetrisGridTest extends TestCase {
	
	// Provided simple clearRows() test
	// width 2, height 3 grid
	public void testClear1() {
		boolean[][] before =
		{	
			{true, true, false, },
			{false, true, true, }
		};
		
		boolean[][] after =
		{	
			{true, false, false},
			{false, true, false}
		};
		
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}

	// cleared grid
	public void testClear2(){
		boolean[][] before =
				{
						{false, false},
						{false, false},
						{false, false}
				};

		boolean[][] after = new boolean[before.length][];
		// same as before
		for(int i = 0; i < after.length; i++)
			after[i] = Arrays.copyOf(before[i], before[i].length);

		TetrisGrid tetris = new TetrisGrid(before);

		tetris.clearRows();
		assertTrue(Arrays.deepEquals(tetris.getGrid(), after));
	}

	// one tile grid
	public void testClear3(){
		boolean[][] before =
				{
						{true}
				};
		boolean[][] after =
				{
						{false}
				};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue(Arrays.deepEquals(tetris.getGrid(), after));

		// clear again on an empty board and make sure it stays the same
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(tetris.getGrid(), after));
	}

	// grid with 2 adjacent full rows
	public void testClear4(){
		boolean[][] before =
				{
						{true, false, true, true, true},
						{false, true, true, true, true},
						{true, true, true, true, true},
						{true, true, true, true, false}
				};
		boolean[][] after =
				{
						{true, false, true, false, false},
						{false, true, true, false, false},
						{true, true, true, false, false},
						{true, true, false, false, false}
				};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue(Arrays.deepEquals(tetris.getGrid(), after));
	}

	// grid with a full row at the bottom and at the top
	public void testClear5(){
		boolean[][] before =
				{
						{true, false, false, true, true},
						{true, false, false, false, true},
						{true, true, true, true, true},
						{true, false, false, true, true}
				};
		boolean[][] after =
				{
						{false, false, true, false, false},
						{false, false, false, false, false},
						{true, true, true, false, false},
						{false, false, true, false, false}
				};

		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue(Arrays.deepEquals(tetris.getGrid(), after));
	}
}
