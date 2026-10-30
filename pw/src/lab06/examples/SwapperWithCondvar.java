package lab06.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SwapperWithCondvar<T> {
    private T value;
    private final List<T> history;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public SwapperWithCondvar(T value) {
        this.value = value;
        this.history = new ArrayList<>();
        this.history.add(value);
    }

    public void swapValue(T expectedValue, T newValue) throws InterruptedException {
        lock.lock();
        try {
            while (!value.equals(expectedValue)) {
                condition.await();
            }
            value = newValue;
            history.add(newValue);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public List<T> getHistory() {
        lock.lock();
        try {
            return List.copyOf(history);
        } finally {
            lock.unlock();
        }
    }
}
