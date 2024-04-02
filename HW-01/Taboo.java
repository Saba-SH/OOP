
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	// for every rule stored as key, store as value a set of all the rules that it should not follow
	private Map<T, Set<T> > prohibited;
	// for every rule stored as key, store as value a set of all the rules that should not follow it
	private Map<T, Set<T> > noFollowMap;

	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		// initialize the maps
		this.prohibited = new HashMap<T, Set<T> >();
		this.noFollowMap = new HashMap<T, Set<T> >();

		// iterate over the rules from the second one to the end
		for(int i = 1, size = rules.size(); i < size; i++){
			// add the current one as a key if it isn't already added
			if(!prohibited.containsKey(rules.get(i))){
				prohibited.put(rules.get(i), new HashSet<T>());
			}
			// add the rule at the previous index in the set of prohibited rules for the current one
			prohibited.get(rules.get(i)).add(rules.get(i - 1));
		}

		// iterate over the rules from the first one to the second from last
		for(int i = 0, lim = rules.size() - 1; i < lim; i++){
			if(!noFollowMap.containsKey(rules.get(i))){
				noFollowMap.put(rules.get(i), new HashSet<T>());
			}
			// add the adjacent rule to the "no follow" set of this one
			noFollowMap.get(rules.get(i)).add(rules.get(i + 1));
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		// return an empty set if no rules are prohibited from following this one
		if(!noFollowMap.containsKey(elem))
			return Collections.emptySet();

		// return a copy of the set in the map
		return new HashSet<T>(noFollowMap.get(elem));
	}

	/**
	 * Private helper function for the reduce function.
	 * @param list the original list of elements
	 * @param indices the list of indices of elements in the list that are considered
	 * @return list of indices with indices of rule-breaking elements removed
	 * */
	private List<Integer> reduceHelper(List<T> list, List<Integer> indices){
		List<Integer> newIndices = new ArrayList<Integer>();
		// first element can't break a rule
		newIndices.add(indices.get(0));

		// iterate over indices of all the elements and only include the indices of those which don't break rules
		for(int i = 1, size = indices.size(); i < size; i++){
			// check that the element at the current index is allowed to follow the one at the previous index
			if(!prohibited.containsKey(list.get(indices.get(i))) ||
					!prohibited.get(list.get(indices.get(i))).contains(list.get(indices.get(i - 1))))
				newIndices.add(indices.get(i));
		}

		// Base Case: if no rule breakers found
		if(newIndices.size() == indices.size())
			return indices;

		// Recursive Call: there might be some more rule breakers in the new indices, so check again
		return reduceHelper(list, newIndices);
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		// return it back if it was an empty list
		if(list.isEmpty())
			return;

		// construct a list of all indices in range 0-(size-1)
		List<Integer> indices = new ArrayList<Integer>();
		for(int i = 0, size = list.size(); i < size; i++)
			indices.add(i);

		// get a list of only the indices of rules that don't break rules
		indices = reduceHelper(list, indices);

		// construct a list containing only those rules
		List<T> newList = new ArrayList<T>();
		for(int index : indices){
			newList.add(list.get(index));
		}

		// clear the original list and add all of those rules
		list.clear();
		for(T elem : newList){
			list.add(elem);
		}
	}
}
