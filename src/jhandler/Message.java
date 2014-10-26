package jhandler;

/**
 * Message containing a description and arbitrary data object that can be sent
 * to a Handler.
 */
public final class Message {
    public int what;
    public int arg1;
    public int arg2;
    public Object obj;
}
