package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> cp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        cp = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++)
            if (cp.compare(this.get(i), this.get(maxIndex)) > 0) {
                maxIndex = i;
        }
        return this.get(maxIndex);
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++)
            if (c.compare(this.get(i), this.get(maxIndex)) > 0) {
                maxIndex = i;
            }
        return this.get(maxIndex);
    }
}