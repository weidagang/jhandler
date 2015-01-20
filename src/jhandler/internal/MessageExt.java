package jhandler.internal;

import jhandler.Handler;
import jhandler.Message;

/**
 * This class wraps the Message or Runnable object and adds related information to it.
 * One and only one of <code>message</code> and <code>callback</code> must be present.
 */
public final class MessageExt {
    /** The original message object passed in from public interfaces of Handler. */
    public final Message message;

    /** The original callback object passed in from public interfaces of Handler. */
    public final Runnable callback;

    /** The target handler this message is sending to. */
    public final Handler target;

    /** When should this message / callback be handled. */
    public final long when;

    /** Constructor for message object. */
    public MessageExt(Handler target, Message message, long when) {
        this.target = target;
        this.message = message;
        this.callback = null;
        this.when = when;
    }

    /** Constructor for callback object */
    public MessageExt(Handler target, Runnable callback, long when) {
        this.target = target;
        this.message = null;
        this.callback = callback;
        this.when = when;
    }
}
