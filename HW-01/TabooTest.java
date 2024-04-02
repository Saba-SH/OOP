// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;
import org.junit.Test;

public class TabooTest extends TestCase {

    @Test
    public void testNoFollow1(){
        Taboo<Character> t = new Taboo<Character>(Arrays.asList('a', 'c', 'a', 'b'));

        assertTrue(t.noFollow('a').equals(new HashSet<Character>(Arrays.asList('c', 'b'))));
        assertTrue(t.noFollow('x').equals(Collections.emptySet()));
    }

    @Test
    public void testNoFollow2(){
        Taboo<String> t = new Taboo<String>(Arrays.asList("a", "c", "a", "b"));

        assertTrue(t.noFollow("a").equals(new HashSet<String>(Arrays.asList("c", "b"))));
        assertTrue(t.noFollow("x").equals(Collections.emptySet()));
    }

    @Test
    public void testNoFollow3(){
        // t and t1 should have same behavior
        Taboo<Integer> t = new Taboo<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));
        Taboo<Integer> t1 = new Taboo<Integer>(Arrays.asList(1, 1));
        // t2 and t3 shouldn't have any restrictions
        Taboo<Integer> t2 = new Taboo<Integer>(Arrays.asList(1));
        Taboo<Integer> t3 = new Taboo<Integer>(Collections.emptyList());

        assertTrue(t.noFollow(1).equals(new HashSet<Integer>(Arrays.asList(1))));
        assertTrue(t1.noFollow(1).equals(new HashSet<Integer>(Arrays.asList(1))));
        assertTrue(t.noFollow(0).equals(Collections.emptySet()));
        assertTrue(t1.noFollow(0).equals(Collections.emptySet()));

        assertTrue(t2.noFollow(1).equals(Collections.emptySet()));
        assertTrue(t2.noFollow(0).equals(Collections.emptySet()));
        assertTrue(t3.noFollow(1).equals(Collections.emptySet()));
        assertTrue(t3.noFollow(0).equals(Collections.emptySet()));
    }

    @Test
    public void testReduce1(){
        Taboo<Character> t = new Taboo<Character>(Arrays.asList('a', 'c', 'a', 'b'));

        List<Character> list = new ArrayList<Character>(Arrays.asList('a', 'c', 'b', 'x', 'c', 'a'));
        t.reduce(list);

        assertTrue(list.equals(Arrays.asList('a', 'x', 'c')));
    }

    @Test
    public void testReduce2(){
        Taboo<String> t = new Taboo<String>(Arrays.asList("a", "c", "a", "b"));

        List<String> list = new ArrayList<String>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        t.reduce(list);

        assertTrue(list.equals(Arrays.asList("a", "x", "c")));
    }

    @Test
    public void testReduce3(){
        // t and t1 should have same behavior
        Taboo<Integer> t = new Taboo<Integer>(Arrays.asList(1, 1, 1, 1, 1, 1));
        Taboo<Integer> t1 = new Taboo<Integer>(Arrays.asList(1, 1));
        // t2 and t3 shouldn't have any restrictions
        Taboo<Integer> t2 = new Taboo<Integer>(Arrays.asList(1));
        Taboo<Integer> t3 = new Taboo<Integer>(Collections.emptyList());

        List<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 1, 1, 1, 1, 2, 2, 3, 1, 4, 1, 1));

        List<Integer> l = new ArrayList<Integer>(list);
        List<Integer> l1 = new ArrayList<Integer>(list);
        List<Integer> l2 = new ArrayList<Integer>(list);
        List<Integer> l3 = new ArrayList<Integer>(list);

        t.reduce(l);
        t1.reduce(l1);
        t2.reduce(l2);
        t3.reduce(l3);

        // compare the results of t and t1 with each other and the expected result
        assertTrue(l.equals(l1) && l.equals(Arrays.asList(1, 2, 2, 3, 1, 4, 1)));
        // compare the results of t2 and t3 with each other and the original list
        assertTrue(l2.equals(l3) && l2.equals(list));
    }

}
