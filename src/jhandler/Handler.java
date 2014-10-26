package jhandler;

import jhandler.internal.MessageExt;
import jhandler.internal.MessageQueue;
import jhandler.internal.TimeUtils;

/**
 * Handler to send and handle message.
 * 
 * <p>
 * Message can be sent from any thread to this handler, while message handling
 * is always in the Looper thread.
 * 
 * @author weidagang@gmail.com (Dagang Wei)
 */
public class Handler {
    private final MessageQueue mQueue = Looper.myQueue();

    /** Subclass must override this method to receive messages */
    public void handleMessage(Message msg) {
    }

    /** Sends empty message to this handler */
    public final boolean sendEmptyMessage(int what) {
        Message msg = new Message();
        msg.what = what;
        return sendMessage(msg);
    }

    /** Sends message to this handler */
    public final boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    /**
     * Sends empty message to this handler which will be handled after the delay
     */
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = new Message();
        msg.what = what;
        return sendMessageDelayed(msg, delayMillis);
    }

    /** Sends message to this handler which will be handled after the delay */
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return mQueue.enqueue(new MessageExt(this, msg, TimeUtils.uptime()
                + delayMillis));
    }

    /**
     * Enqueues a message at the front of the message queue, to be processed on
     * the next iteration of the message loop.
     */
    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return mQueue.enqueueAtFront(new MessageExt(this, msg, 0));
    }

    /** Checks if there're any pending messages with "what" */
    public final boolean hasMessages(int what) {
        return mQueue.hasMessages(what);
    }

    /** Remove all the messages with "what" */
    public final void removeMessages(int what) {
        mQueue.removeMessages(what);
    }

    /** Remove all the messages with "what" and "obj" */
    public final void removeMessages(int what, Object obj) {
        mQueue.removeMessages(what, obj);
    }

    /** Adds the Runnable r to the message queue. */
    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * Adds the Runnable r to the message queue, to be run after the specified
     * amount of time elapses.
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        MessageExt msg = new MessageExt(this, r, TimeUtils.uptime()
                + delayMillis);
        return mQueue.enqueue(msg);
    }

    /**
     * Enqueues a Runnable r at the front of the message queue, to be processed
     * on the next iterator of the message loop
     */
    public final boolean postAtFrontOfQueue(Runnable r) {
        return mQueue.enqueueAtFront(new MessageExt(this, r, 0));
    }

    /** Receives a message dispatched from Looper, to be handled immediately */
    void dispatch(MessageExt msg) {
        assert (this == msg.target);

        if (null != msg.callback) {
            msg.callback.run();
        } else {
            handleMessage(msg.message);
        }
    }
}
