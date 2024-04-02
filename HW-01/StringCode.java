import java.util.HashSet;
import java.util.Set;

import static java.lang.Character.isDigit;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int res = 0;

		for(int i = 0; i < str.length(); i++){
			// current char and its run
			char currChar = str.charAt(i);
			int curr = 0;
			// iterate over string until we see a different char
			for(; i < str.length(); i++){
				if(str.charAt(i) != currChar){
					break;
				}
				curr++;
			}
			// move i to the end of the current run
			i--;
			// update result if needed
			if(curr > res)
				res = curr;
		}

		return res;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		// construct a string builder for the result
		StringBuilder resBuilder = new StringBuilder("");

		// iterate over the given string
		for(int i = 0; i < str.length(); i++){
			char curr = str.charAt(i);
			// check if current char is a digit
			if(isDigit(curr)){
				// append nothing if we're at the end of the string
				if(i == str.length() - 1)
					break;

				int digit = Character.getNumericValue(curr);

				for(int j = 0; j < digit; j++){
					resBuilder.append(str.charAt(i + 1));
				}
			} else {
				resBuilder.append(curr);
			}
		}

		return resBuilder.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if(a.length() < len || b.length() < len)
			return false;

		Set<String> hs1 = new HashSet<String>();

		for(int i = 0, lim = a.length() - len; i <= lim; i++){
			String curr = a.substring(i, i + len);
			hs1.add(curr);
		}

		for(int i = 0, lim = b.length() - len; i <= lim; i++){
			if(hs1.contains(b.substring(i, i + len)))
				return true;
		}

		return false;
	}
}
