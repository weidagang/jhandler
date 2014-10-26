package jhandler;

import java.util.Comparator;
import java.util.function.Predicate;

import jhandler.internal.SortedLinkedList;

import org.junit.Assert;
import org.junit.Test;

public class SortedLinkedListTest {
    private class A {
        final int key;
        final int value;

        public A(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    Comparator<A> mComparator = new Comparator<A>() {
        @Override
        public int compare(A o1, A o2) {
            return o1.key - o2.key;
        }
    };

    @Test
    public void testSortedProperty() {
        SortedLinkedList<Integer> list = new SortedLinkedList<Integer>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                });

        Assert.assertTrue(0 == list.size());
        Assert.assertTrue(null == list.peekFirst());

        {
            list.add(5);
            Assert.assertTrue(1 == list.size());
            Assert.assertTrue(5 == list.peekFirst());
        }

        {
            list.add(2);
            Assert.assertTrue(2 == list.size());
            Assert.assertTrue(2 == list.peekFirst());
        }

        {
            list.add(3);
            Assert.assertTrue(3 == list.size());
            Assert.assertTrue(2 == list.peekFirst());
        }

        {
            Integer msg = list.removeFirst();
            Assert.assertTrue(2 == msg);
            Assert.assertTrue(2 == list.size());
            Assert.assertTrue(3 == list.peekFirst());
        }

        {
            Integer msg = list.removeFirst();
            Assert.assertTrue(3 == msg);
            Assert.assertTrue(1 == list.size());
            Assert.assertTrue(5 == list.peekFirst());
        }

        {
            Integer msg = list.removeFirst();
            Assert.assertTrue(5 == msg);
            Assert.assertTrue(0 == list.size());
            Assert.assertTrue(null == list.peekFirst());
        }
    }

    @Test
    public void testTiebreakerRule() {
        SortedLinkedList<A> list = new SortedLinkedList<A>(mComparator);

        final int N = 100;

        for (int i = 1; i <= N; ++i) {
            final A a = new A(100, i); // the same key, different value
            list.add(a);
            Assert.assertTrue(100 == list.peekFirst().key);
            Assert.assertTrue(1 == list.peekFirst().value);
            Assert.assertTrue(i == list.size());
        }

        for (int i = 1; i <= N; ++i) {
            final A a = list.removeFirst();
            Assert.assertTrue(100 == a.key);
            Assert.assertTrue(a.value == i);
            Assert.assertTrue(N - i == list.size());
        }
    }
    
    @Test
    public void testRemoveElements() {
        class PredicateKey implements Predicate<A> {
            final int mKey;
            
            public PredicateKey(int key) {
                this.mKey = key;
            }
            @Override
            public boolean test(A t) {
                return mKey == t.key;
            }
        }
        
        SortedLinkedList<A> list = new SortedLinkedList<A>(mComparator);
        list.add(new A(5, 5));
        list.add(new A(10, 10));
        list.add(new A(2, 2));
        list.add(new A(3, 3));
        list.add(new A(5, 50));
        Assert.assertEquals(5, list.size());
        Assert.assertEquals(2, list.peekFirst().key);
        
        Assert.assertTrue(list.hasElements(new PredicateKey(2)));        
        list.removeElements(new PredicateKey(2));
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(3, list.peekFirst().key);
        Assert.assertFalse(list.hasElements(new PredicateKey(2)));

        Assert.assertTrue(list.hasElements(new PredicateKey(5)));
        list.removeElements(new PredicateKey(5));
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(3, list.peekFirst().key);
        Assert.assertFalse(list.hasElements(new PredicateKey(5)));
        
        Assert.assertTrue(list.hasElements(new PredicateKey(10)));
        list.removeElements(new PredicateKey(10));
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(3, list.peekFirst().key);
        Assert.assertFalse(list.hasElements(new PredicateKey(10)));
        
        Assert.assertTrue(list.hasElements(new PredicateKey(3)));
        list.removeElements(new PredicateKey(3));
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(null, list.peekFirst());
        Assert.assertFalse(list.hasElements(new PredicateKey(3)));
        
        list.removeElements(new PredicateKey(3));//duplicate removes
        Assert.assertEquals(0, list.size());        
    }
}
