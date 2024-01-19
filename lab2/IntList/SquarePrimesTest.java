package IntList;

import static org.junit.Assert.*;

import jh61b.junit.In;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrims1() {
        IntList list1 = IntList.of(1, 2, 4, 6, 8);
        boolean changed = IntListExercises.squarePrimes(list1);
        assertEquals("1 -> 4 -> 4 -> 6 -> 8", list1.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrims2() {
        IntList list2 = IntList.of(3, 7, 8, 9, 10);
        boolean changed = IntListExercises.squarePrimes(list2);
        assertEquals("9 -> 49 -> 8 -> 9 -> 10", list2.toString());
        assertTrue(changed);
    }
}
