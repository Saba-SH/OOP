import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		// map elements to their counts
		Map<T, Integer> mapA = new HashMap<T, Integer>();
		Map<T, Integer> mapB = new HashMap<T, Integer>();

		// iterate over both collections and fill their respective maps
		for(T elem : a){
			if(mapA.containsKey(elem)){
				mapA.put(elem, mapA.get(elem) + 1);
			}else{
				mapA.put(elem, 1);
			}
		}
		for(T elem : b){
			if(mapB.containsKey(elem)){
				mapB.put(elem, mapB.get(elem) + 1);
			}else{
				mapB.put(elem, 1);
			}
		}

		// optimization: make sure that mapA points to the smaller map
		if(mapB.size() < mapA.size()){
			Map<T, Integer> tmp = mapA;
			mapA = mapB;
			mapB = tmp;
		}

		int res = 0;
		// iterate over the first map and see how many elements of it have the same value in the second map
		for(T elem : mapA.keySet()){
			// increment the result if matching amounts for current element
			if(mapA.get(elem) == mapB.get(elem))
				res++;
		}

		return res;
	}
	
}
