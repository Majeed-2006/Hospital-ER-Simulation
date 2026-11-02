public class Patient implements Runnable {

    public final String name;               
    private final WaitingRoom waitingRoom;

    private final Semaphore treated = new Semaphore(0);

    public Patient(int id, WaitingRoom waitingRoom) {
        this.name = "P" + id;
        this.waitingRoom = waitingRoom;
    }

    @Override
    public void run() {
        try {
            Printer.print(name + " arrived.");

            waitingRoom.enter(this);

            treated.acquire();

            Printer.print(name + " left.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void finishTreatment() {
        treated.release();
    }
}
