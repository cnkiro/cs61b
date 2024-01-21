package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing list1 = new AListNoResizing<Integer>();
        BuggyAList list2 = new BuggyAList();

        for (int i = 4; i <= 6; i++) {
            list1.addLast(i);
            list2.addLast(i);
        }

        assertEquals(list1.size(), list2.size());

        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> List1 = new AListNoResizing<>();
        BuggyAList<Integer> List2 = new BuggyAList<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                List1.addLast(randVal);
                List2.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = List1.size();
                int size2 = List2.size();
            } else if (operationNumber == 2) {
                //getLast only when size > 0
                if (List1.size() > 0 && List2.size() > 0) {
                    int val1 = List1.getLast();
                    int val2 = List2.getLast();
                }
            } else if (operationNumber == 3) {
                //removeLast only when size > 0
                if (List1.size() > 0 && List2.size() > 0) {
                    int val1 = List1.removeLast();
                    int val2 = List2.removeLast();
                }
            }
        }
    }

}
