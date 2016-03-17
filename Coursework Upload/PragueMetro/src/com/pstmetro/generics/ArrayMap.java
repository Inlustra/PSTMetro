/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The ArrayMap class is merely a custom implementation of a Map that is backed
 * by an Array of ArrayMapEntry objects. We created this class to prove that we
 * are able to create efficient Data structures.
 *
 * @param <K> The type of Keys stored within the ArrayMapEntry.
 * @param <V> The type of Values stored within the ArrayMapEntry
 * @author Thomas
 */
public class ArrayMap<K, V> implements Map<K, V> {

    private ArrayMapEntry<K, V>[] data;
    private int length;
    private int capacity;
    private final int initialCapacity;

    /**
     *
     * Creates the map and ensures the array has the required capacity.
     *
     * @param initialCapacity
     */
    public ArrayMap(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        clear();
    }

    /**
     *
     */
    public ArrayMap() {
        this(4);
    }

    /**
     * This method ensures the backing array has enough space to contain
     * (current array capacity + 'space') amount of objects of type V.
     *
     * @param space Amount of space required in the array
     */
    public void ensureSpace(int space) {
        ensureCapacity(length + space);
    }

    /**
     * This method ensures the backing array has enough space to contain
     * 'capacity' amount of objects of type ArrayMapEntry<K,V>.
     *
     * @param capacity Amount of space required in the array
     */
    public void ensureCapacity(int capacity) {
        if (capacity > this.capacity) {
            int newSize = this.capacity + this.capacity >> 1;
            if (newSize < capacity) {
                newSize = capacity;
            }
            ArrayMapEntry<K, V>[] newArray = new ArrayMapEntry[newSize];
            int i = length;
            while (i-- > 0) {
                newArray[i] = data[i];
            }
            this.capacity = newSize;
            data = newArray;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        int i = length;
        while (i-- > 0) {
            if (data[i].getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        int i = length;
        while (i-- > 0) {
            if (data[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        int i = length;
        while (i-- > 0) {
            if (data[i].getKey().equals(key)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(K key, V value) {
        ensureSpace(1);
        data[length++] = new ArrayMapEntry(key, value);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(Object key) {
        int i = length;
        while (i-- > 0) {
            ArrayMapEntry<K, V> e = data[i];
            if (e.getKey().equals(key)) {
                length--;
                int j;
                while ((j = i++) < length) {
                    data[j] = data[i];
                }
                data[length] = null;
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        ensureSpace(m.size());
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            data[length++] = new ArrayMapEntry(e.getKey(), e.getValue());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        length = 0;
        capacity = initialCapacity;
        data = new ArrayMapEntry[initialCapacity];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        ArraySet<K> s = new ArraySet<K>(length);
        int i = length;
        while (i-- > 0) {
            s.add(data[i].getKey());
        }
        return s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        ArrayList<V> l = new ArrayList<V>(length);
        int i = length;
        while (i-- > 0) {
            l.add(data[i].getValue());
        }
        return l;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        ArraySet<Map.Entry<K, V>> s = new ArraySet<Map.Entry<K, V>>(length <= 0 ? 1 : length);
        int i = length;
        while (i-- > 0) {
            s.add(data[i]);
        }
        return s;
    }
}

class ArrayMapEntry<K, V> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    public ArrayMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }
}
