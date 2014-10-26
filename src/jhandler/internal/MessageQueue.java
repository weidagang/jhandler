package jhandler.internal;

import java.util.Comparator;
import java.util.function.Predicate;

public class MessageQueue {
    private volatile boolean mQuited = false; // the initial state is NOT quited
    
    /** Message comparator based on the when field */
    private class TimeComparator implements Comparator<MessageExt> {
        @Override
        public int compare(MessageExt e1, MessageExt e2) {
            return (int) (e1.when - e2.when);
        }
    }

    /** Sorted message list by "when" */
    private SortedLinkedList<MessageExt> mSortedList = new SortedLinkedList<MessageExt>(
            new TimeComparator());

    /** The number of pending messages */
    public int size() {
        synchronized (mSortedList) {
            return mSortedList.size();
        }
    }

    /** Enqueues a message */
    public boolean enqueue(MessageExt msg) {
        // already quited
        if (mQuited) {
            return false;
        }

        // don't accept null message
        if (null == msg) {
            return false;
        }
                
        // message without target means quit
        if (null == msg.target) {
            mQuited = true; 
            // don't return here, let it be handled by looper
        }

        synchronized (mSortedList) {
            mSortedList.add(msg);
            mSortedList.notifyAll();
        }

        return true;
    }
    
    /** Enqueues a message at the front of this queue */
    public boolean enqueueAtFront(MessageExt msg) {
        synchronized (mSortedList) {
            final MessageExt front = mSortedList.peekFirst();
            final long when = (null == front ? 0 : front.when - 1);
            return enqueue(new MessageExt(msg.target, msg.message, when));
        }        
    }

    /** Returns next message in the queue (may block) */
    public MessageExt next() {
        synchronized (mSortedList) {
            while (true) {
                try {
                    long waitTimeout = 30 * 1000; //30s

                    if (mSortedList.size() > 0) {
                        waitTimeout = mSortedList.peekFirst().when - TimeUtils.uptime();
                    }
                    
                    if (waitTimeout > 0) {
                        mSortedList.wait(waitTimeout);
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                }
            }
            return mSortedList.removeFirst();
        }
    }

    /** Checks if there're any elements which match the predicate */
    public boolean hasMessages(final int what) {
        synchronized (mSortedList) {
            return mSortedList.hasElements(new Predicate<MessageExt>() {
                @Override
                public boolean test(MessageExt t) {
                    return what == t.message.what;
                }
            });
        }
    }

    /** Removes all the messages by "what" */
    public void removeMessages(final int what) {
        removeMessages(what, null);
    }

    /** Remove all the messages by "what" and "obj" */
    public void removeMessages(final int what, final Object obj) {
        synchronized (mSortedList) {
            mSortedList.removeElements(new Predicate<MessageExt>() {
                @Override
                public boolean test(MessageExt t) {
                    return what == t.message.what
                            && (null == obj || obj == t.message.obj);
                }
            });
        }
    }
}
