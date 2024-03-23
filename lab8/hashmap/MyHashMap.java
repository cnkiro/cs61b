package hashmap;


import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author cnkiro
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int N;
    private double loadFactor;
    private Set<K> set;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        N = 0;
        loadFactor = 0.75;
        set = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        loadFactor /= 2;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        N = initialSize;
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] temp = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            temp[i] = createBucket();
        }
        return temp;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int hash(K key) {
        int hashcode = key.hashCode();
        return Math.floorMod(hashcode, buckets.length);
    }

    @Override
    public void clear() {
        buckets = createTable(16);
        N = 0;
        set = new HashSet<>();
    }

    @Override
    public boolean containsKey(K key) {
        int hc = hash(key);
        if (buckets[hc] == null) {
            return false;
        }
        boolean result = false;
        for (Node node: buckets[hc]) {
            if (key.equals(node.key)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public V get(K key) {
        int hc = hash(key);
        if (!containsKey(key)) {
            return null;
        }
        V val = (V) new Object();
        for (Node node: buckets[hc]) {
            if (key.equals(node.key)) {
                val = node.value;
                break;
            }
        }
        return val;
    }

    @Override
    public int size() {
        return N;
    }

    @Override
    public void put(K key, V value) {
        int hc = hash(key);
        if (buckets[hc] == null) {
            buckets[hc] = createBucket();
        }
        if (containsKey(key)) {
            for (Node node: buckets[hash(key)]) {
                if (key.equals(node.key)) {
                    node.value = value;
                    return;
                }
            }
        }
        buckets[hc].add(createNode(key, value));
        N += 1;
        if ((double)(N/buckets.length) > loadFactor) {
            resize(2 * buckets.length);
        }
        set.add(key);
    }

    private void resize(int size) {
        Collection<Node>[] temp = createTable(size);
        for (int i = 0; i < buckets.length; i+= 1) {
            for (Node node: buckets[i]) {
                temp[Math.floorMod(node.key.hashCode(), temp.length)].add(node);
            }
        }
        buckets = temp;
        loadFactor /= 2;
    }

    @Override
    public Set<K> keySet() {
        return set;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return set.iterator();
    }
}