
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Calendar;

public class CharGridTest extends TestCase {
	@Test
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	@Test
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}
	
	@Test
	public void testCharArea3(){
		char[][] grid = new char[][]{
				{'a', 'b', 'c'},
				{'d', 'e', 'f'},
				{'f', 'e', 'd'},
				{'c', 'b', 'a'}
			};

		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.charArea('g'));
		assertEquals(0, cg.charArea('\0'));
		assertEquals(12, cg.charArea('a'));
		assertEquals(12, cg.charArea('c'));
		assertEquals(2, cg.charArea('e'));
		assertEquals(4, cg.charArea('b'));
		assertEquals(6, cg.charArea('d'));
	}

	@Test
	public void testCharArea4(){
		char[][] grid = new char[][]{
				{'a', 'b'},
				{'c', 'd'}
			};

		CharGrid cg = new CharGrid(grid);

		for(char c = 'a'; c < 'd'; c++){
			assertEquals(1, cg.charArea(c));
		}

		cg = new CharGrid(new char[][]{{'a'}});

		assertEquals(1, cg.charArea('a'));
		assertEquals(0, cg.charArea('b'));

		cg = new CharGrid(new char[][]{});

		assertEquals(0, cg.charArea('\0'));
		assertEquals(0, cg.charArea(' '));
	}

	@Test
	public void testCharArea5(){
		char[][] grid = new char[][]{
				{'\0', '\0', '\0'},
				{'\0', '\0', '\0'},
				{'\0', '\0', '\0'},
				{'\0', '\0', '\0'},
				{'\0', '\0', '\0'}
			};

		CharGrid cg = new CharGrid(grid);

		assertEquals(15, cg.charArea('\0'));
		assertEquals(0, cg.charArea(' '));
	}

	@Test
	public void testCountPlus1(){
		CharGrid cg = new CharGrid(new char[][]{
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x', ' '},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x', ' '},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y', ' ', ' '},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ', 'y', ' ', ' ', ' '}
		});
		assertEquals(2, cg.countPlus());
	}

	@Test
	public void testCountPlus2(){
		CharGrid cg = new CharGrid(new char[][]{
				{},
				{},
				{}
		});
		assertEquals(0, cg.countPlus());

		cg = new CharGrid(new char[][]{

		});
		assertEquals(0, cg.countPlus());
	}

	@Test
	public void testCountPlus3(){
		CharGrid cg = new CharGrid(new char[][]{
				{'x','x','x'},
				{'x','x','x'},
		});
		assertEquals(0, cg.countPlus());

		cg = new CharGrid(new char[][]{
				{'x','x'},
				{'x','x'},
				{'x','x'}
		});
		assertEquals(0, cg.countPlus());

		cg = new CharGrid(new char[][]{
				{'x','x','x'},
				{'x','x','x'}
		});
		assertEquals(0, cg.countPlus());
	}

	@Test
	public void testCountPlus4(){
		CharGrid cg = new CharGrid(new char[][]{
				{'x','x','x'},
				{'x','x','x'},
				{'x','x','x'},
		});
		assertEquals(1, cg.countPlus());

		cg = new CharGrid(new char[][]{
				{'x','x','x','x'},
				{'x','x','x','x'},
				{'x','x','x','x'},
				{'x','x','x','x'}
		});
		assertEquals(0, cg.countPlus());

		cg = new CharGrid(new char[][]{
				{'x','x','x','x','x'},
				{'x','x','x','x','x'},
				{'x','x','x','x','x'},
				{'x','x','x','x','x'},
				{'x','x','x','x','x'}
		});
		assertEquals(1, cg.countPlus());

		cg = new CharGrid(new char[][]{
				{' ','x',' ','x','x'},
				{'x','x','x',' ',' '},
				{' ','x',' ','x',' '},
				{' ',' ','x','x','x'},
				{'x',' ',' ','x',' '}
		});
		assertEquals(2, cg.countPlus());
	}

}
