package bstmap;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private int size;

    private class BSTNode {
        public K key;
        public V value;
        private BSTNode left;
        private BSTNode right;
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }


    public BSTMap() {}

    @Override
    public Iterator<K> iterator() {
        return null;
    }


    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        return containsKey(root, key);
    }

    private boolean containsKey(BSTNode T, K key) {
        boolean result = false;
        if (T == null) {
            return false;
        }
        if (key.compareTo(T.key) < 0) {
            result = containsKey(T.left, key);
        }
        if (key.compareTo(T.key) > 0) {
            result = containsKey(T.right, key);
        }
        if (key.compareTo(T.key) == 0) {
            result = true;
        }
        return result;
    }


    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode T, K key) {
        if (T == null || key == null) {
            return null;
        }
        V val = (V) new Object();
        if (key.compareTo(T.key) == 0) {
            val = T.value;
        }
        if (key.compareTo(T.key) > 0) {
            val = get(T.right, key);
        }
        if (key.compareTo(T.key) < 0) {
            val =  get(T.left, key);
        }
        return val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            return;
        }
        root = put(root, key, value);
        size += 1;
    }

    private BSTNode put(BSTNode T, K key, V value) {
        if (T == null) {
            return new BSTNode(key, value);
        }
        if (key.compareTo(T.key) < 0) {
            T.left = put(T.left, key, value);
        }
        if (key.compareTo(T.key) > 0) {
            T.right = put(T.right, key, value);
        }
        return T;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}