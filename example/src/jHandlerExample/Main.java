package jHandlerExample;

import jhandler.Handler;
import jhandler.Looper;
import jhandler.Message;

class Example1 {
    static final private int MAX_ROUND = 10;
    static volatile private Handler mPingHandler;
    static volatile private Handler mPongHandler;
    
    static class PingThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            mPingHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("Ping: received message, what=" + msg.what + ", arg1=" + msg.arg1);
                    if (msg.arg1 >= MAX_ROUND) {
                        Looper.myLooper().quit();
                    }
                    Message m = new Message();
                    m.what = 0;
                    m.arg1 = msg.arg1 + 1;
                    mPongHandler.sendMessageDelayed(m, 2 * 1000);                        
                }
            };

            while (mPongHandler == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // kick off
            System.out.println("Ping: kick off");
            mPongHandler.sendEmptyMessage(0);
            
            Looper.loop();
        }
    }

    static class PongThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            mPongHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("Pong: received message, what=" + msg.what + ", arg1=" + msg.arg1);
                    if (msg.arg1 >= MAX_ROUND) {
                        Looper.myLooper().quit();                    
                    }
                    Message m = new Message();
                    m.what = 1;
                    m.arg1 = msg.arg1 + 1;
                    mPingHandler.sendMessageDelayed(m, 2 * 1000);                        
                }
            };
            Looper.loop();
        }
    }

    public static void run() throws InterruptedException {
        PingThread pingThread = new PingThread();        
        PongThread pongThread = new PongThread();
        pingThread.start();
        pongThread.start();        
        pingThread.join();
        pongThread.join();
        System.out.println("finished");
    }

}

public class Main {
    public static void main(String[] argv) throws InterruptedException {
        Example1.run();
    }
}
