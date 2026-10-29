## Class Dependencies and Implementation Order

To ensure successful compilation and execution without missing symbol errors, classes should be implemented and integrated in the following order based on their dependencies:

### 1. Independent Core Classes
These classes have no dependencies on other project files and can be built and tested individually:
- `Printer.java`: Thread-safe utility for synchronized output.
- `Semaphore.java`: Custom counting semaphore implementation managing permits, blocking, and timed acquires.

### 2. Low-Level Shared Resources
- `TreatmentRooms.java`: Manages treatment room capacity using `Semaphore`.
- `Patient.java`: Represents patient threads. Depends on `Printer`.

### 3. High-Level Shared Resources
- `WaitingRoom.java`: Bounded buffer queue for waiting patients. 
  - **Dependencies:** Requires `Patient`, `Semaphore`, and `Printer`.

### 4. Consumer Threads
- `Doctor.java`: Represents doctor threads handling patient extraction, treatment room acquisition timeout, and fallback logic.
  - **Dependencies:** Requires `Patient`, `WaitingRoom`, `TreatmentRooms`, and `Printer`.

### 5. Application Entry Point
- `Hospital.java`: Reads user input, initializes all shared resources (`WaitingRoom`, `TreatmentRooms`), creates patient/doctor threads, and starts the simulation.
  - **Dependencies:** Requires all above classes (`Printer`, `Semaphore`, `Patient`, `Doctor`, `WaitingRoom`, `TreatmentRooms`).
