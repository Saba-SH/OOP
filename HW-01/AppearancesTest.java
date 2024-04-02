import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	@Test
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}

	@Test
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	@Test
	public void testSameCount3(){
		List<Integer> a = Arrays.asList(1, 2, 0, 4, 0, 2, 1);
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(4, 0, 2, 1, 2, 0, 4)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 2, 3, 0, 4, 5, 6)));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(5, 6, 7, 0, 7, 6, 5)));
	}

	@Test
	public void testSameCount4(){
		List<String> a = Arrays.asList("AAA", "AA", "CBA", "AA", "ABCD");
		assertEquals(0, Appearances.sameCount(a, Arrays.asList("aaa", "aa", "cba", "aa", "abcd")));
		assertEquals(4, Appearances.sameCount(a, Arrays.asList("AAA", "AA", "CBA", "AA", "ABCD")));
		assertEquals(0, Appearances.sameCount(a, Arrays.asList("", "AA", "", "AAA", "", "AAA")));
	}

	@Test
	public void testSameCount5(){
		List<Integer> a = Arrays.asList(0, 0, 0, 0, 0, 0);
		assertEquals(0, Appearances.sameCount(a, Arrays.asList(-1, 1, -1, 1, -1, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(0, 0, 0, 0, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(0, 0, 0, 0, 1, 2, 3, 0, 0)));
	}
}
