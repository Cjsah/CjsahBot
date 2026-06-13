package net.cjsah.bot.resolver;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CountdownLocker {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final AtomicInteger count;

    public CountdownLocker(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        this.count = new AtomicInteger(count);
    }

    public void increment() {
        this.lock.lock();
        try {
            this.count.incrementAndGet();
        } finally {
            this.lock.unlock();
        }
    }

    public void release() {
        this.lock.lock();
        try {
            int current = this.count.get();
            if (current > 0) {
                if (this.count.decrementAndGet() == 0) {
                    this.condition.signalAll();
                }
            } else {
                this.condition.signalAll();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            if (this.count.get() > 0) {
                this.condition.await();
            }
        } finally {
            lock.unlock();
        }
    }
}
