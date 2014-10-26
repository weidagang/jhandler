package jhandler.internal;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Sorted linked list
 */
public class SortedLinkedList<T> {
    private LinkedList<T> mLinkedList = new LinkedList<T>();
    private Comparator<T> mComparator;

    /**
     * Constructor
     * 
     * @param comparator
     *            The comparator for the element type
     */
    public SortedLinkedList(Comparator<T> comparator) {
        mComparator = comparator;
    }

    /**
     * The number of elements in this list
     * 
     * @return The number of elements in this list
     */
    public int size() {
        return mLinkedList.size();
    }

    /**
     * Add an element to this list
     * 
     * @param e
     *            The element
     */
    public void add(T e) {
        ListIterator<T> it = mLinkedList.listIterator();

        while (it.hasNext()) {
            T current = it.next();
            if (mComparator.compare(e, current) < 0) {
                it.previous();
                break;
            }
        }

        it.add(e);
    }

    /**
     * Retrieves, but does not remove, the first element or null if this list is
     * empty
     * 
     * @return the first element or null if this list is empty
     */
    public T peekFirst() {
        return mLinkedList.peekFirst();
    }

    /**
     * Removes and returns the first element
     * 
     * @return The first element
     * @throws NoSuchElementException
     *             - if this list is empty
     */
    public T removeFirst() {
        return mLinkedList.removeFirst();
    }
    
    /** Checks if there're any elements which match the predicate */
    public boolean hasElements(Predicate<T> predicate) {
        assert (null != predicate);

        final ListIterator<T> it = mLinkedList.listIterator();
        while (it.hasNext()) {
            T current = it.next();
            if (predicate.test(current)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Removes elements which match the predicate
     * 
     * @param predicate
     * @return the number of removed elements
     */
    public int removeElements(Predicate<T> predicate) {
        assert (null != predicate);

        int n = 0;
        final ListIterator<T> it = mLinkedList.listIterator();
        while (it.hasNext()) {
            T current = it.next();
            if (predicate.test(current)) {
                ++n;
                it.remove();
            }
        }

        return n;
    }
}
