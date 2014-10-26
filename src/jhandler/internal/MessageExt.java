package jhandler.internal;

import jhandler.Handler;
import jhandler.Message;

/**
 * Message used internally
 */
public final class MessageExt {
    public final Handler target;
    public final Message message;
    public final Runnable callback;
    public final long when;

    public MessageExt(Handler target, Message msg, long when) {
        this.target = target;
        this.message = msg;
        this.callback = null;
        this.when = when;
    }

    public MessageExt(Handler target, Runnable callback, long when) {
        this.target = target;
        this.message = null;
        this.callback = callback;
        this.when = when;
    }
}
