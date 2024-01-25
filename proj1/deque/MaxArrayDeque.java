package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private T maxItem;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        maxItem = max(c);
    }

    public T max() {
        if (size() == 0) {
            return null;
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (c.compare(this.get(i), this.get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return this.get(maxIndex);
    }
}
