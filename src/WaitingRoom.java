import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private final Queue<Patient> queue = new LinkedList<>();
    private final Semaphore emptySeats;
    private final Semaphore waitingPatients = new Semaphore(0);
    private final Semaphore queueLock = new Semaphore(1);

    public WaitingRoom(int capacity) {
        emptySeats = new Semaphore(capacity);
    }

    // Patient (producer): sleeps if there is no empty seat
    public void enter(Patient p) throws InterruptedException {
        if (!emptySeats.tryAcquire(0)) { 
            Printer.print("The waiting room is full.");
            Printer.print(p.name + " is waiting outside for an available seat.");
            emptySeats.acquire();
        }
        queueLock.acquire();
        queue.add(p);
        Printer.print(p.name + " entered the waiting room.");
        queueLock.release();
        waitingPatients.release();
    }

    // Doctor (consumer): sleeps if there are no patients
    public Patient take() throws InterruptedException {
        waitingPatients.acquire();
        queueLock.acquire();
        Patient p = queue.remove();
        queueLock.release();
        emptySeats.release();
        return p;
    }
    
// Called by the doctor ONLY after successfully acquiring a treatment room to free up a seat.
    public void releaseSeat() {
        emptySeats.release();
    }

    // Called by the doctor if acquiring a treatment room times out, returning the patient to the queue.
    public void returnPatient(Patient p) throws InterruptedException {
        queueLock.acquire();
        queue.add(p);
        queueLock.release();
        waitingPatients.release();
    }
}