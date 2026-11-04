
import java.util.Scanner;

public class Hospital {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Read input
        System.out.print("Waiting room capacity: ");
        int capacity = scanner.nextInt();

        System.out.print("Number of doctors: ");
        int numberOfDoctors = scanner.nextInt();

        System.out.print("Number of patients: ");
        int numberOfPatients = scanner.nextInt();

        // Create shared resources
        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms treatmentRooms = new TreatmentRooms(2);

        // Create doctors
        Doctor[] doctors = new Doctor[numberOfDoctors];

        for (int i = 0; i < numberOfDoctors; i++) {
            doctors[i] = new Doctor(
                "Doctor " + (i + 1),
                waitingRoom,
                treatmentRooms
            );
        }

        // Create patients
        Patient[] patients = new Patient[numberOfPatients];
        Thread[] patientThreads = new Thread[numberOfPatients];

        Printer.print("\nPatients arriving:");

        for (int i = 0; i < numberOfPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
            System.out.print(patients[i].name + " ");
        }

        System.out.println("\n");

        // Start doctors
        for (Doctor doctor : doctors) {
            doctor.start();
        }

        // Start patients
        for (int i = 0; i < numberOfPatients; i++) {
            patientThreads[i] = new Thread(patients[i]);
            patientThreads[i].start();
        }

        try {
            // Wait until all patients leave the hospital
            for (Thread patientThread : patientThreads) {
                patientThread.join();
            }

            // Stop doctors
            for (Doctor doctor : doctors) {
                doctor.interrupt();
            }

            // Wait for doctors to stop
            for (Doctor doctor : doctors) {
                doctor.join();
            }

            Printer.print("\nAll patients have been treated.");
            Printer.print("Simulation completed.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        scanner.close();
    }
}
