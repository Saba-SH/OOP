// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.

import junit.framework.TestCase;
import org.junit.Test;

public class StringCodeTest extends TestCase {
	//
	// blowup
	//
	@Test
	public void testBlowup1() {
		// basic cases
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
	}

	@Test
	public void testBlowup2() {
		// things with digits
		
		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));
		
		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));
		
		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));
	}

	@Test
	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));
		
		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}
	
	
	//
	// maxRun
	//
	@Test
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}

	@Test
	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}

	@Test
	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}

	@Test
	public void testIntersect0(){
		assertFalse(StringCode.stringIntersect("", "", 1));
		assertFalse(StringCode.stringIntersect("", "AAA", 2));
		assertFalse(StringCode.stringIntersect("AAA", "", 3));
	}

	@Test
	public void testIntersect1(){
		assertFalse(StringCode.stringIntersect("AAA", "aaa", 1));
		assertTrue(StringCode.stringIntersect("AAA", "AAA", 3));
		assertTrue(StringCode.stringIntersect("aaa", "aaa", 2));
		assertFalse(StringCode.stringIntersect("AAA", "AAA", 4));
	}

	@Test
	public void testIntersect2(){
		assertTrue(StringCode.stringIntersect("AAaaAA", "aaAAaa", 2));
		assertTrue(StringCode.stringIntersect("AAaaAA", "aaAAaa", 3));
		assertTrue(StringCode.stringIntersect("AAaaAA", "aaAAaa", 4));
		assertFalse(StringCode.stringIntersect("AAaaAA", "aaAAaa", 5));
	}

	@Test
	public void testIntersect3(){
		assertTrue(StringCode.stringIntersect("ABC DEF", "DEF ABC", 3));
		assertFalse(StringCode.stringIntersect("ABC DEF", "DEF ABC", 4));
		assertFalse(StringCode.stringIntersect("ABCDEF", "FEDCBA", 2));
		assertTrue(StringCode.stringIntersect("ABCDEF", "ABCDEF", 6));
		assertFalse(StringCode.stringIntersect("ABCDEF", "ABCDEF", 7));
	}

}
