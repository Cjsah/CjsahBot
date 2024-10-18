package net.cjsah.bot.resolver;

public class Counter {
    private volatile int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (this) {
            this.count++;
        }
    }

    public void completed() {
        synchronized (this) {
            if (this.count > 0) {
                this.count--;
            }
            if (this.count == 0) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }

    public void await() throws InterruptedException {
        if (this.count > 0) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

}
