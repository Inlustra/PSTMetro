/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pstmetro.generics;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The ArraySet class is merely a custom implementation of a LinkedList and, is
 * created by starting from a head node and moving along to the element desired.
 * We created this class to prove that we are able to create efficient Data
 * structures.
 *
 * This list could be improved by doubly linking and using a tail, we deemed it
 * unnecessary for the required project as it is used in only one case.s
 *
 * @param <V> The type of Object you wish to store within the code
 * @author Thomas
 */
public class NodeLinkedList<V> implements List<V> {

    private int size = 0;
    /**
     * The start of the LinkedList.
     *
     */
    private ListNode<V> head;

    /**
     * Adds to the list by scrolling along to the desired element and creating a
     * new ListNode
     *
     * @param element The element you wish to add to the list.
     */
    @Override
    public boolean add(V element) {
        if (this.head == null) {
            head = new NodeLinkedList.ListNode(element);
        } else {
            ListNode node = head;
            while (node.getLinkedNode() != null) {
                node = node.getLinkedNode();
            }
            node.setLinkedNode(new ListNode(element));
        }
        size++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object element) {
        ListNode<V> node = head;
        ListNode<V> previous = head;
        if (head.getValue() == element) {
            if (head.getLinkedNode() != null) {
                head = head.getLinkedNode();
            } else {
                head = null;
            }
            size--;
            return true;
        }
        while (node.getLinkedNode() != null) {
            node = node.getLinkedNode();
            if (node.getValue() == element) {
                if (node.getLinkedNode() != null) {
                    previous.setLinkedNode(node.getLinkedNode());
                } else {
                    previous.setLinkedNode(null);
                }
                size--;
                return true;
            }
            previous = node;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(int index) {
        int i = 0;
        ListNode<V> node = head;
        while (index > i) {
            if (node == null) {
                throw new IndexOutOfBoundsException(i + "");
            }
            node = node.getLinkedNode();
            i++;
        }
        return node.getValue();
    }

    /**
     * returns the ListNode associated at said index.
     *
     * @param index The element you wish to add to the list.
     */
    private ListNode getNode(int index) {
        int i = 0;
        ListNode<V> node = head;
        while (index > i) {
            if (node == null) {
                throw new IndexOutOfBoundsException(i + "");
            }
            node = node.getLinkedNode();
            i++;
        }
        return node;
    }

    /**
     * Reverses the list by creating a temporary head and appending each element
     * starting from the tail of the list
     */
    public void reverse() {
        ListNode tempHead = getNode(size() - 1);
        ListNode currNode = tempHead;
        for (int i = size() - 2; i >= 0; i--) {
            currNode.setLinkedNode(getNode(i));
            currNode = getNode(i);
        }
        this.head = tempHead;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        ListNode<V> node = head;
        while (node.getLinkedNode() != null) {
            if (node.getValue() == o) {
                return true;
            }
            node = node.getLinkedNode();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<V> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        ListNode<V> node = head;
        int i = 0;
        while (node != null) {
            array[i] = node.getValue();
            node = node.getLinkedNode();
            i++;
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        ListNode<V> node = head;
        int i = 0;
        while (node != null) {
            a[i] = (T) node.getValue();
            node = node.getLinkedNode();
            i++;
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
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
        for (V o : c) {
            add(o);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        ListNode<V> node = head;
        loop:
        while (node != null) {
            for (Object o : c) {
                if (node.getValue() == o) {
                    node = node.getLinkedNode();
                    continue loop;
                }
            }
            remove(node.getValue());
            node = node.getLinkedNode();

        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V set(int index, V element) {
        ListNode<V> node = head;
        int i = 0;
        while (node.getLinkedNode() != null) {
            if (i == index) {
                V previousEl = node.getValue();
                node.setValue(element);
                return previousEl;
            }
            node = node.getLinkedNode();
            i++;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int index, V element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(int index) {
        ListNode<V> node = head;
        int i = 0;
        while (node.getLinkedNode() != null) {
            if (i == index) {
                V previousEl = node.getValue();
                remove(node.getValue());
                return previousEl;
            }
            node = node.getLinkedNode();
            i++;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<V> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<V> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class ListNode<V> {

        private V value;
        private ListNode<V> linkedNode;

        private ListNode(V value) {
            this.value = value;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public ListNode<V> getLinkedNode() {
            return linkedNode;
        }

        public void setLinkedNode(ListNode<V> linkedNode) {
            this.linkedNode = linkedNode;
        }
    }
}
