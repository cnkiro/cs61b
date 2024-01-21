package deque;

//Author: cnkiro
public class ArrayDeque<T> {
    private T[] array;
    private int size;
    private int head;
    private int tail;


    public ArrayDeque() {
        array = null;
        size = 0;
        head = 0;
        tail = 0;
    }

    public void addFirst(T item) {
        if (size == 0) {
            array[0] = item;
            tail = 0;
        } else {
            //if (size == array.length) {
            //    resize();
            //}
            array[(head + array.length - 1) % array.length] = item;
            head = (head + array.length - 1) % array.length;
            size += 1;
        }
    }

    public void addLast(T item) {
        if (size == 0) {
            array[0] = item;
            tail = 0;
        } else {
            //if (size == array.length) {
            //}
            array[(head + array.length + 1) % array.length] = item;
            tail = (head + array.length + 1) % array.length;
        }
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int p = head;
        while (p != tail) {
            T item = array[p];
            System.out.print(item.toString() + " ");
            p = (p + 1) % array.length;
        }
        System.out.println(array[p].toString() + " ");
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = array[head];
        array[head] = null;
        head = (head + 1) % array.length;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = array[tail];
        array[tail] = null;
        tail = (tail - 1 + array.length) % array.length;
        return item;
    }

    public T get(int index) {
        return array[(head + index) % array.length];
    }
}