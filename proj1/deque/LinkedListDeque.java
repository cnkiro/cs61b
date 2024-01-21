package deque;

import java.awt.event.ItemEvent;
import java.security.PublicKey;

public class LinkedListDeque<T> {

    int size;
    public LDNode Sentinel;
    public class LDNode {
        public T Item;
        public LDNode next;
        public LDNode prev;
        public LDNode(T item, LDNode next, LDNode prev) {
            this.Item = item;
            this.next = next;
            this.prev = prev;
        }

        public LDNode() {
            Item = null;
            next = null;
            prev = null;
        }
    }



    public LinkedListDeque() {
        Sentinel = new LDNode();
        Sentinel.prev = Sentinel;
        Sentinel.next = Sentinel;
    }


    public void addFirst(T item) {
        LDNode p = new LDNode(item, null, null);
        p.next = Sentinel.next;
        p.prev = Sentinel;
        Sentinel.next.prev = p;
        Sentinel.next = p;
        size++;
    }

    public void addLast(T item) {
        LDNode p = new LDNode(item, null, null);
        p.prev = Sentinel.prev;
        p.next = Sentinel;
        Sentinel.prev.next = p;
        Sentinel.prev = p;
        size++;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size(){
        return size;
    }

    public void printDeque() {
        if (size == 0) {
            return;
        }
        else {
            int temp = size;
            LDNode p = Sentinel.next;
            while (size > 0) {
                System.out.print(p.Item.toString() + " ");
                size--;
            }
            System.out.println();
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        LDNode p = Sentinel.next;
        Sentinel.next = p.next;
        p.next.prev = Sentinel;
        size--;
        return p.Item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        LDNode p = Sentinel.prev;
        p.prev.next = Sentinel;
        Sentinel.prev = p.prev;
        size--;
        return p.Item;
    }

    public T get(int index) {
        LDNode p = Sentinel.next;
        int i = 1;
        while (index > i) {
            p = p.next;
            i++;
        }
        return p.Item;
    }
}
