/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.generics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * The ArraySet class is merely a custom implementation of a set that is backed
 * by an Array. We created this class to prove that we are able to create
 * efficient Data structures.
 *
 * @param <V> Type of objects stored within the Array
 * @author Thomas
 */
public class ArraySet<V> implements Set<V> {

    private V[] array;
    private int length;
    private int capacity;
    private final int initialCapacity;

    /**
     * Creates the array and ensures capacity.
     *
     * @param initialCapacity Starting space within the array backing the set.
     */
    public ArraySet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be greater than 0.");
        }
        this.initialCapacity = initialCapacity;
        clear();
    }

    /**
     *
     *
     */
    public ArraySet() {
        this(4);
    }

    /**
     * This method ensures the backing array has enough space to contain
     * 'capacity' amount of objects of type V.
     *
     * @param capacity Amount of space required in the array
     */
    public void ensureCapacity(int capacity) {
        if (capacity > this.capacity) {
            int newSize = ((array.length * 3) / 2) + 1;
            if (newSize < capacity) {
                newSize = capacity;
            }
            V[] newArray = (V[]) new Object[newSize];
            int i = length;
            while (i-- > 0) {
                newArray[i] = array[i];
            }
            this.capacity = newSize;
            array = newArray;
        }
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
    public boolean contains(Object o) {
        for (V obj : array) {
            if (obj == o) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<V> iterator() {
        return new ArrayIterator(array, length);
    }

    @Override
    public Object[] toArray() {
        Object[] a = new Object[array.length];
        System.arraycopy(array, 0, a, 0, array.length);
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < array.length) {
            a = (T[]) new Object[array.length];
        }
        for (int i = 0; i < array.length; i++) {
            a[i] = (T) array[i];
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(V e) {
        if (contains(e)) {
            return false;
        }
        ensureSpace(1);
        array[length++] = e;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        int i = length;
        while (i-- > 0) {
            V e = array[i];
            if (e.equals(o)) {
                length--;
                int j;
                while ((j = i++) < length) {
                    array[j] = array[i];
                }
                array[length] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object value : c) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends V> c) {
        boolean addedAll = true;
        ensureSpace(c.size());
        for (V value : c) {
            if (!add(value)) {
                addedAll = false;
            }
        }
        return addedAll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retainedAll = true;
        for (V value : array) {
            if (!c.contains(value)) {
                remove(value);
                retainedAll = false;
            }
        }
        return retainedAll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removedAll = false;
        for (V value : array) {
            if (c.contains(value)) {
                remove(value);
                removedAll = true;
            }
        }
        return removedAll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        length = 0;
        capacity = initialCapacity;
        array = (V[]) new Object[initialCapacity];
    }

    /**
     * A simple iterator for the ArraySet.
     *
     * @param <V> The type of object required.
     */
    public class ArrayIterator<V> implements Iterator<V> {

        int current;
        int length;
        V[] array;

        /**
         *
         * @param array
         * @param length
         */
        public ArrayIterator(V[] array, int length) {
            this.array = array;
            this.length = length;
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return length > current;
        }

        @Override
        public V next() {
            V v = array[current++];
            return v;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unable to remove, size of array is fixed."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
