package Eren.hal.Components;

/**
 * This class represents a buffer.
 * A buffer is a place where data is stored temporarily.
 * A buffer has a put and a get method.
 * Threads safe concept is used in this class.
 */
public class Buffer {
    private boolean available = false;
    private float data;
    /**
     * This method is used to put a value into the buffer.
     *
     * @param x The value to be put into the buffer.
     */
    public synchronized void put(float x) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        data = x;
        available = true;
        // Wakes up all threads that are waiting on this object's monitor.
        notifyAll();
    }

    /**
     * This method is used to get a value from the buffer.
     *
     * @return The value from the buffer.
     */
    public synchronized float get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        available = false;
        // Wakes up all threads that are waiting on this object's monitor.
        notifyAll();
        return data;
    }
}