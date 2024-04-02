import junit.framework.TestCase;
import org.junit.Test;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		Piece.getPieces();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
		b.commit();
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max

	// numbers that the board looks right after the operations.
	@Test
	public void testEmptyClear() {
		b = new Board(3, 6);

		assertEquals(0, b.clearRows());
		for(int i = 0; i < b.getHeight(); i++) {
			assertEquals(0, b.getRowWidth(i));
		}
		for(int i = 0; i < b.getWidth(); i++) {
			assertEquals(0, b.getColumnHeight(i));
		}
	}

	@Test
	public void testFullGridClear() {
		b = new Board(4, 6);
		Piece stick = Piece.getPieces()[Piece.STICK].fastRotation();
		for(int i = 0; i < b.getHeight(); i++) {
			b.place(stick, 0, i);
			b.commit();
		}

		assertEquals(6, b.getMaxHeight());

		for(int i = 0; i < b.getWidth(); i++) {
			assertEquals(6, b.getColumnHeight(i));
		}

		for(int i = 0; i < b.getHeight(); i++) {
			assertEquals(4, b.getRowWidth(i));
		}

		assertEquals(6, b.clearRows());

		assertEquals(0, b.getMaxHeight());

		for(int i = 0; i < b.getWidth(); i++) {
			assertEquals(0, b.getColumnHeight(i));
		}

		for(int i = 0; i < b.getHeight(); i++) {
			assertEquals(0, b.getRowWidth(i));
		}
	}

	@Test
	public void testSeveralFullRows() {
		b.commit();

		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		b.commit();

		Piece stick = Piece.getPieces()[Piece.STICK];
		result = b.place(stick, 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);

		b.commit();

		assertEquals(5, b.getMaxHeight());

		result = b.place(sRotated, 1, 3);
		assertEquals(Board.PLACE_ROW_FILLED, result);

		assertEquals(6, b.getMaxHeight());

		for(int i = 0; i < 5; i++) {
			assertEquals(3, b.getRowWidth(i));
		}

		int rowsCleared = b.clearRows();

		assertEquals(5, rowsCleared);

		assertEquals(1, b.getMaxHeight());

		assertEquals(0, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));

		assertEquals(1, b.getRowWidth(0));

		for(int i = 1; i < 6; i++) {
			assertEquals(0, b.getRowWidth(i));
		}
	}

	@Test
	public void testSeparateFullRows(){
		b = new Board(3, 6);

		b.place(pyr1, 0, 0);
		b.commit();
		b.place(pyr3, 0, 2);
		b.commit();
		b.place(pyr1, 0, 4);
		b.commit();
		int clearedRows = b.clearRows();
		assertEquals(3,clearedRows);

		clearedRows = b.clearRows();
		assertEquals(0, clearedRows);

		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));

		b = new Board(5, 8);
		b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 0, 0);	b.commit();
		b.place(Piece.getPieces()[Piece.L2].fastRotation(), 2, 0);	b.commit();
		b.place(Piece.getPieces()[Piece.PYRAMID].fastRotation().fastRotation(), 0, 2);	b.commit();
		b.place(Piece.getPieces()[Piece.S2], 2, 3);	b.commit();
		b.place(Piece.getPieces()[Piece.L1], 0, 4);	b.commit();
		b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 1, 6);	b.commit();

		assertEquals(7, b.getMaxHeight());
		for(int i = 0; i < 5; i++)
			assertEquals(7, b.getColumnHeight(i));

		assertEquals(3, b.clearRows());

		assertEquals(4, b.getMaxHeight());
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(3, b.getColumnHeight(3));
		assertEquals(1, b.getColumnHeight(4));

		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(4, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
	}

	@Test
	public void testGetGrid() {
		b = new Board(3, 6);
		b.place(s, 0, 0);

		assertTrue(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 1));
		assertTrue(b.getGrid(2, 1));
		assertTrue(b.getGrid(1, 0));

		assertTrue(b.getGrid(-1, 0));
		assertTrue(b.getGrid(10, 0));
		assertTrue(b.getGrid(0, 10));
		assertTrue(b.getGrid(0, -1));

		assertFalse(b.getGrid(0, 2));
		assertFalse(b.getGrid(0, 2));
		assertFalse(b.getGrid(1, 2));
		assertFalse(b.getGrid(2, 3));
	}

	@Test
	public void testDropHeight() {
		b = new Board(6, 8);
		b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 1, 0);	b.commit();
		b.place(Piece.getPieces()[Piece.S2], 1, 1);	b.commit();
		b.place(Piece.getPieces()[Piece.L1], 2, 3);	b.commit();

		assertEquals(0, b.dropHeight(Piece.getPieces()[Piece.STICK], 0));
		assertEquals(3, b.dropHeight(Piece.getPieces()[Piece.L1], 0));
		assertEquals(6, b.dropHeight(Piece.getPieces()[Piece.S2], 0));
		assertEquals(1, b.dropHeight(Piece.getPieces()[Piece.STICK], 4));
		assertEquals(2, b.dropHeight(Piece.getPieces()[Piece.L1].fastRotation().fastRotation(), 3));
		assertEquals(4, b.dropHeight(Piece.getPieces()[Piece.L1].fastRotation().fastRotation(), 2));
		assertEquals(2, b.dropHeight(Piece.getPieces()[Piece.PYRAMID].fastRotation().fastRotation().fastRotation(), 0));
		assertEquals(1, b.dropHeight(Piece.getPieces()[Piece.L2].fastRotation().fastRotation(), 0));
		assertEquals(0, b.dropHeight(Piece.getPieces()[Piece.PYRAMID].fastRotation(), 4));
		assertEquals(4, b.dropHeight(Piece.getPieces()[Piece.S1], 3));
	}

	// helper function to check the heights and widths of a board
	private void checkWidthHeight(Board b, int[] widths, int[] heights) {
		for(int i = 0; i < b.getHeight(); i++) {
			assertEquals(widths[i], b.getRowWidth(i));
		}
		for(int i = 0; i < b.getWidth(); i++) {
			assertEquals(heights[i], b.getColumnHeight(i));
		}
	}

	@Test
	public void testBig() {
		Board b = new Board(5, 8);

		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 2, 2));
		b.undo();
		checkWidthHeight(b, new int[]{0,0,0,0,0,0,0,0}, new int[]{0,0,0,0,0});

		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 1, 0));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.S2].fastRotation(), 0, 0));
		b.commit();
		assertEquals(1, b.clearRows());
		b.commit();
		checkWidthHeight(b, new int[]{2,1,0,0,0,0,0,0}, new int[]{1,2,0,0,0});

		assertEquals(Board.PLACE_BAD, b.place(Piece.getPieces()[Piece.S1], 1, 0));
		b.undo();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.PYRAMID], 2, 0));
		b.commit();
		checkWidthHeight(b, new int[]{5,2,0,0,0,0,0,0}, new int[]{1,2,1,2,1});

		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.L2].fastRotation().fastRotation(), 0, 1));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.STICK].fastRotation(), 1, 2));
		checkWidthHeight(b, new int[]{5,3,5,2,0,0,0,0}, new int[]{4,4,3,3,3});
		b.commit();
		assertEquals(2, b.clearRows());
		b.commit();
		checkWidthHeight(b, new int[]{3,2,0,0,0,0,0,0}, new int[]{2,2,0,1,0});

		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.PYRAMID].fastRotation().fastRotation().fastRotation(), 2, 0));
		b.commit();
		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.STICK], 2, 3));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.L1].fastRotation().fastRotation(), 3, 0));
		b.commit();
		checkWidthHeight(b, new int[]{5,5,3,1,1,1,1,0}, new int[]{2,2,7,3,3});
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.SQUARE], 0, 2));
		checkWidthHeight(b, new int[]{5,5,5,3,1,1,1,0}, new int[]{4,4,7,3,3});
		assertEquals(3, b.clearRows());
		b.commit();

		checkWidthHeight(b, new int[]{3,1,1,1,0,0,0,0}, new int[]{1,1,4,0,0});
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(Piece.getPieces()[Piece.SQUARE], 0, 8));
		b.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(Piece.getPieces()[Piece.SQUARE], 0, 7));
		b.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(Piece.getPieces()[Piece.SQUARE], 2, 7));
		b.undo();
		checkWidthHeight(b, new int[]{3,1,1,1,0,0,0,0}, new int[]{1,1,4,0,0});

		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.SQUARE], 3, 0));
		assertEquals(1, b.clearRows());
		checkWidthHeight(b, new int[]{3,1,1,0,0,0,0,0}, new int[]{0,0,3,1,1});
		b.commit();

		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.S1].fastRotation(), 0, 1));
		b.commit();
		assertEquals(Board.PLACE_OK, b.place(Piece.getPieces()[Piece.L1], 1, 3));
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b.place(Piece.getPieces()[Piece.SQUARE], 3, 3));
		b.commit();
		assertEquals(1, b.clearRows());
		b.commit();
		checkWidthHeight(b, new int[]{3,2,3,3,1,0,0,0}, new int[]{3,5,3,4,4});
		assertEquals(0, b.clearRows());
		b.commit();

		assertEquals(5, b.dropHeight(Piece.getPieces()[Piece.SQUARE], 0));
		assertEquals(3, b.dropHeight(Piece.getPieces()[Piece.STICK], 0));
		assertEquals(4, b.dropHeight(Piece.getPieces()[Piece.S1], 2));
		assertEquals(4, b.dropHeight(Piece.getPieces()[Piece.L2].fastRotation(), 1));
		assertEquals(3, b.dropHeight(Piece.getPieces()[Piece.PYRAMID].fastRotation().fastRotation().fastRotation(), 2));
	}

	@Test
	public void testUncommitedPlaceClear() {
		Board b = new Board(6, 8);
		b.place(Piece.getPieces()[Piece.PYRAMID], 1, 0);

		boolean throwFlag = false;
		try{
			b.place(Piece.getPieces()[Piece.PYRAMID].fastRotation(), 0, 1);
		}catch(RuntimeException ex) {
			throwFlag = true;
		}
		assertTrue(throwFlag);		throwFlag = false;

		try{
			b.clearRows();
		} catch(RuntimeException ex) {
			throwFlag = true;
		}
		assertFalse(throwFlag);

		b.commit();
		try{
			b.place(Piece.getPieces()[Piece.STICK], 0, 0);
		} catch(RuntimeException ex) {
			throwFlag = true;
		}
		assertFalse(throwFlag);

		try{
			b.place(Piece.getPieces()[Piece.L2], 0, 0);
		} catch(RuntimeException ex) {
			throwFlag = true;
		}
		assertTrue(throwFlag);
	}
}
