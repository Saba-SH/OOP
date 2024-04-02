import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	Piece s1, s2, l1, l2, st, sq;

	private void generatePieces() {
		s1 = new Piece(Piece.S1_STR);
		s2 = new Piece(Piece.S2_STR);
		l1 = new Piece(Piece.L1_STR);
		l2 = new Piece(Piece.L2_STR);
		st = new Piece(Piece.STICK_STR);
		sq = new Piece(Piece.SQUARE_STR);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		generatePieces();
	}
	
	// Here are some sample tests to get you started
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}


	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	// tests the skirt of more pieces
	@Test
	public void testSkirt() {
		assertTrue(Arrays.equals(new int[] {0}, st.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, st.computeNextRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s1.computeNextRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 2}, l2.computeNextRotation().computeNextRotation().getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, pyr2.getSkirt()));
	}

	// check that the width and height remains the same when rotating a piece by 180 degrees
	@Test
	public void test180() {
		assertEquals(pyr1.getHeight(), pyr3.getHeight());
		assertEquals(pyr2.getHeight(), pyr2.getHeight());

		assertEquals(pyr1.getWidth(), pyr3.getWidth());
		assertEquals(pyr2.getWidth(), pyr4.getWidth());

		assertEquals(s.getWidth(), sRotated.computeNextRotation().getWidth());
		assertEquals(s.getHeight(), sRotated.computeNextRotation().getHeight());

		assertEquals(l1.getWidth(), l1.computeNextRotation().computeNextRotation().getWidth());
		assertEquals(l1.getHeight(), l1.computeNextRotation().computeNextRotation().getHeight());

		assertEquals(l2.computeNextRotation().getWidth(), l2.computeNextRotation().computeNextRotation().computeNextRotation().getWidth());
		assertEquals(l2.computeNextRotation().getHeight(), l2.computeNextRotation().computeNextRotation().computeNextRotation().getHeight());

		assertEquals(st.getWidth(), st.computeNextRotation().computeNextRotation().getWidth());
		assertEquals(st.getHeight(), st.computeNextRotation().computeNextRotation().getHeight());
	}

	//check that the square always stays the same
	@Test
	public void testSquare() {
		Piece sq = new Piece(Piece.SQUARE_STR);
		assertEquals(sq.getWidth(), sq.getHeight());

		Piece sq1 = sq.computeNextRotation();

		assertEquals(sq.getWidth(), sq1.getWidth());
		assertTrue(Arrays.equals(sq.getSkirt(), sq1.getSkirt()));

		sq1 = sq1.computeNextRotation();
		assertEquals(sq.getWidth(), sq1.getWidth());
		assertTrue(Arrays.equals(sq.getSkirt(), sq1.getSkirt()));

		sq1 = sq1.computeNextRotation();
		assertEquals(sq.getHeight(), sq1.getHeight());
		assertTrue(Arrays.equals(sq.getSkirt(), sq1.getSkirt()));

		sq1 = sq1.computeNextRotation();
		assertEquals(sq.getHeight(), sq1.getHeight());
		assertTrue(Arrays.equals(sq.getSkirt(), sq1.getSkirt()));
	}

	// tests the Piece.equals() method
	@Test
	public void testEquals() {
		assertTrue(sq.equals(sq.computeNextRotation()));

		assertFalse(st.equals(st.computeNextRotation()));
		assertTrue(st.equals(st.computeNextRotation().computeNextRotation()));

		assertFalse(s1.equals(s1.computeNextRotation()));
		assertTrue(s2.equals(s2.computeNextRotation().computeNextRotation()));

		assertFalse(l1.equals(l1.computeNextRotation().computeNextRotation()));
		assertFalse(l2.equals(l2.computeNextRotation().computeNextRotation().computeNextRotation()));

		assertFalse(pyr1.equals(pyr2));
		assertFalse(pyr2.equals(pyr3));
		assertFalse(pyr3.equals(pyr4));
	}

	// tests the basic pieces from getPieces
	@Test
	public void testGetPieces() {
		Piece[] pieces = Piece.getPieces();
		assertTrue(pieces[Piece.STICK].equals(new Piece(Piece.STICK_STR)));
		assertTrue(pieces[Piece.L1].equals(new Piece(Piece.L1_STR)));
		assertTrue(pieces[Piece.L2].equals(new Piece(Piece.L2_STR)));
		assertTrue(pieces[Piece.S1].equals(new Piece(Piece.S1_STR)));
		assertTrue(pieces[Piece.S2].equals(new Piece(Piece.S2_STR)));
		assertTrue(pieces[Piece.SQUARE].equals(new Piece(Piece.SQUARE_STR)));
		assertTrue(pieces[Piece.PYRAMID].equals(new Piece(Piece.PYRAMID_STR)));
	}

	// tests fast rotations by comparing them with regular rotations
	@Test
	public void testFastRotation() {
		Piece[] pieces = Piece.getPieces();
		Random rand = new Random();
		for(int i = 0; i < 20; i++){
			int pieceIndex = rand.nextInt(Piece.STICK, Piece.PYRAMID + 1);
			Piece p = pieces[pieceIndex];
			Piece pFast = p, pSlow = p;

			int rotationAmount = rand.nextInt(0, p.getBody().length + 1);
			for(int j = 0; j < rotationAmount; j++){
				pFast = pFast.fastRotation();
				pSlow = pSlow.computeNextRotation();
			}

			assertTrue(pFast.equals(pSlow));
		}
	}
}
