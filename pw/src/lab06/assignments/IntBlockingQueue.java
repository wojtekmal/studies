package lab06.assignments;

public class IntBlockingQueue {
    private final int capacity;
    private final int[] buffer;
    private int size;
    private int firstOccupied;
    private int firstEmpty;
    private boolean isShutdown;

    public IntBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.buffer = new int[capacity];
        this.size = 0;
        this.firstOccupied = 0;
        this.firstEmpty = 0;
        this.isShutdown = false;
    }

    public synchronized int take() throws InterruptedException, ShutdownException {
        while (getSize() == 0 && !isShutdown) wait();

        if (getSize() == 0)
            throw new ShutdownException();

        int first = buffer[firstOccupied];
        firstOccupied = (firstOccupied + 1) % capacity;
        size--;

        notifyAll();

        return first;
    }

    public synchronized void put(int item) throws InterruptedException, ShutdownException {
        while (getSize() == capacity && !isShutdown) wait();

        if (isShutdown)
            throw new ShutdownException();

        buffer[firstEmpty] = item;
        firstEmpty = (firstEmpty + 1) % capacity;
        size++;

        notifyAll();
    }

    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    public synchronized int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public static class ShutdownException extends Exception {
        public ShutdownException() {
            super("Queue has been shut down.");
        }
    }
}