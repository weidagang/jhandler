package jhandler;

import jhandler.internal.MessageExt;
import jhandler.internal.MessageQueue;

/**
 * Looper used to run a message loop for a thread. A looper composes a message
 * queue.
 */
public class Looper {
    private static final ThreadLocal<Looper> sLooperHolder = new ThreadLocal<Looper>();

    // every looper is associated with a message queue
    final MessageQueue mQueue = new MessageQueue();

    // no public constructor
    private Looper() {
    }

    /**
     * Prepares looper for the current thread
     */
    public static final void prepare() {
        if (sLooperHolder.get() != null) {
            throw new RuntimeException(
                    "Only one Looper may be created per thread");
        }
        sLooperHolder.set(new Looper());
    }

    /**
     * Runs the message loop in this thread.
     */
    public static final void loop() {
        final MessageQueue queue = myQueue();

        while (true) {
            MessageExt msg = queue.next(); // might block
            assert (null != msg);

            // message w/o target means stop
            if (msg.target == null) {
                return;
            }

            msg.target.dispatch(msg);
        }
    }

    /** Returns the Looper object associated with the current thread. */
    public static final Looper myLooper() {
        if (null == sLooperHolder.get()) {
            throw new RuntimeException("Looper is not prepared");
        }
        return sLooperHolder.get();
    }

    /** Quits the message loop */
    public void quit() {
        // a message without target for quite
        mQueue.enqueue(new MessageExt(null, (Message) null, 0));
    }

    /** Gets the message queue associated with the loop of the current thread */
    static final MessageQueue myQueue() {
        return myLooper().mQueue;
    }
}
