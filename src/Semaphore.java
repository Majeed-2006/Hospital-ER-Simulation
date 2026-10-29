public class Semaphore {
    private int value;

    public Semaphore(int init)
    {
        value = init;
    }

    // Takes one permit. If none is available, the thread sleeps (releasing the lock)
    // until another thread calls release(). Waits indefinitely.
    public synchronized void acquire() throws InterruptedException {
        while(value<=0) {
            wait();
        }
        value--;
    }

    // Tries to take one permit, but waits at most 'seconds' seconds.
    // Returns true if a permit was taken, false if the time ran out.
    // Used by doctors to wait up to 3 seconds for a treatment room.
    public synchronized boolean tryAcquire(int seconds) throws InterruptedException {
       long deadline = System.currentTimeMillis() + seconds * 1000L;
        while(value<=0) {
            long remaining = deadline - System.currentTimeMillis();
            if(remaining <=0)
                return false;
            wait(remaining);
        }
        value--;
        return true;

    }

    // Returns one permit and wakes up one waiting thread (if any)
    // so it can re-check whether a permit is available.
    public synchronized void release() {
        value++;
        notify();
    }
}