package medconnect.model;

public class Prescription {
    private int id;
    private int appointmentId;
    private int medicineId;
    private String dosage;
    private int durationDays;

    public Prescription() {}

    public Prescription(int id, int appointmentId, int medicineId, String dosage, int durationDays) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.medicineId = medicineId;
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
}
