public class Doctor extends Thread {
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;

    private static final int ROOM_TIMEOUT_SECONDS = 3;
    private static final int TREATMENT_TIME_MS = 2000;

    public Doctor(String name, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms) {
        super(name);
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                // Sleeps here if no patients are waiting
                Patient p = waitingRoom.take();
                Printer.print(getName() + " took " + p.name + " from the waiting room.");

                // Wait at most 3 seconds for a free treatment room
                if (treatmentRooms.tryAcquire(ROOM_TIMEOUT_SECONDS)) {
                    // Room secured, so the patient's seat can now be freed
                    waitingRoom.releaseSeat();
                    Printer.print(getName() + " is treating " + p.name + ".");

                    Thread.sleep(TREATMENT_TIME_MS);

                    Printer.print(getName() + " finished treating " + p.name + ".");
                    treatmentRooms.release();
                } else {
                    // Timeout: put the patient back in the queue
                    Printer.print(getName() + " found no free treatment room for "
                            + p.name + ". Returning the patient to the waiting room.");
                    waitingRoom.returnPatient(p);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
