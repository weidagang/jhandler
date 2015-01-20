package jhandler.internal;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Sorted linked list. This data structure will always keep the elements in order
 * after adding or removing elements.
 * 
 * @author Dagang Wei
 */
public class SortedLinkedList<T> {
    private LinkedList<T> mLinkedList = new LinkedList<T>();
    private Comparator<T> mComparator;

    /**
     * Constructor.
     * 
     * @param comparator The comparator for the element type
     */
    public SortedLinkedList(Comparator<T> comparator) {
        mComparator = comparator;
    }

    /**
     * Returns the number of elements in this list.
     * 
     * <p> The time complexity of this operation is O(1).
     * 
     * @return The number of elements in this list
     */
    public int size() {
        return mLinkedList.size();
    }

    /**
     * Adds an element to this list.
     * 
     * <p> The time complexity of this operation is O(n).
     * 
     * @param e The element
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
     * empty.
     * 
     * <p> The time complexity of this operation is O(1).
     * 
     * @return the first element or null if this list is empty
     */
    public T peekFirst() {
        return mLinkedList.peekFirst();
    }

    /**
     * Removes and returns the first element.
     * 
     * <p> The time complexity of this operation is O(1).
     * 
     * @return The removed first element
     * @throws NoSuchElementException if this list is empty
     */
    public T removeFirst() {
        return mLinkedList.removeFirst();
    }
    
    /**
     * Checks if there're any elements which match the predicate.
     * 
     * <p> The time complexity of this operation is O(n).
     * 
     * @param predicate
     * @return true - matched, false - not matched
     */
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
     * Removes elements which match the predicate.
     * 
     * <p> The time complexity of this operation is O(1).
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
