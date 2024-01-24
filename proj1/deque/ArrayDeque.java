package deque;

import org.hamcrest.internal.ArrayIterator;

import java.util.Iterator;
import java.util.LinkedList;

//Author: cnkiro
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;


    public ArrayDeque() {
        items = (T[])new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast= 5;
    }

    public void addFirst(T item) {
        if (items.length == size) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }

    public void addLast(T item) {
        if (items.length == size) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < nextLast; i++) {
            temp[i] = items[i];
        }
        for (int i = nextFirst + 1; i < items.length; i++) {
            temp[i + size] = items[i];
        }
        items = temp;
        nextFirst = nextFirst + size;
    }

    public void printDeque() {
        int index = nextFirst + 1;
        while (index != (nextLast - 1 + items.length) % items.length) {
            T item = items[index];
            System.out.print(item.toString());
            index = (index + 1) % items.length;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size <= items.length / 4) {
            resize(2 * size);
        }
        T item = items[(nextFirst + 1) % items.length];
        items[(nextFirst + 1) % items.length] = null;
        size -= 1;
        nextFirst = (nextFirst + 1) % items.length;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size <= items.length / 4) {
            resize(2 * size);
        }
        T item = items[(nextLast - 1 + items.length) % items.length];
        items[(nextLast - 1 + items.length) % items.length] = null;
        size -= 1;
        nextLast = (nextLast - 1 + items.length) % items.length;
        return item;
    }

    public T get(int index) {
        return items[(index + nextFirst - 1) % items.length];
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        ArrayDeque<T> obj = (ArrayDeque<T>) o;
        if (obj.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.get(i) != obj.get(i)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {

        private int wizpos;

        public ArrayIterator() {
            wizpos = 0;
        }

        @Override
        public boolean hasNext() {
            if (wizpos < size) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            T x = items[wizpos];
            wizpos += 1;
            return x;
        }
    }
}
