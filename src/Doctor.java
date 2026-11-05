import java.util.Random;

public class Doctor extends Thread {
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final Random random = new Random();

    private static final int ROOM_TIMEOUT_SECONDS = 3;
    private static final int MIN_TREATMENT_MS = 1000;
    private static final int MAX_TREATMENT_MS = 4000;

    public Doctor(String name, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms) {
        super(name);
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
              
                Patient p = waitingRoom.take();
                Printer.print(getName() + " is assigned to " + p.name + ".");

              
                Printer.print(getName() + " is trying to acquire a treatment room.");
                if (treatmentRooms.tryAcquire(ROOM_TIMEOUT_SECONDS)) {
                    try {
                        Printer.print(getName() + " acquired a treatment room.");
                        Printer.print(getName() + " begins treating " + p.name + ".");

                    
                        Thread.sleep(MIN_TREATMENT_MS
                                + random.nextInt(MAX_TREATMENT_MS - MIN_TREATMENT_MS + 1));

                        Printer.print(getName() + " finished treating " + p.name + ".");
                    } finally {
                    
                        Printer.print(getName() + " released the treatment room.");
                        treatmentRooms.release();
                    }
                    
                    p.finishTreatment();
                } else {
                    
                    Printer.print(getName() + " could not acquire a treatment room in time for "
                            + p.name + ".");
                    waitingRoom.returnPatient(p);
                }

                Printer.print(getName() + " is now available.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
