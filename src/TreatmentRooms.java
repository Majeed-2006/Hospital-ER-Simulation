public class TreatmentRooms {
    private final Semaphore rooms;
    public TreatmentRooms(int count) {
        rooms = new Semaphore(count);
    }
     // Doctor tries to get a room, waiting at most 'seconds'
    public boolean tryAcquire(int seconds) throws InterruptedException {
        return rooms.tryAcquire(seconds);  
    }

    // Doctor finished, frees the room
    public void release() {
        rooms.release();
    }
}
