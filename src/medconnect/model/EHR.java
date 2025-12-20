package medconnect.model;

import java.sql.Timestamp;

public class EHR {
    private int id;
    private int patientId;
    private String diagnosis;
    private String prescriptions;
    private String notes;
    private Timestamp lastUpdated;

    public EHR() {}

    public EHR(int id, int patientId, String diagnosis, String prescriptions, String notes, Timestamp lastUpdated) {
        this.id = id;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.prescriptions = prescriptions;
        this.notes = notes;
        this.lastUpdated = lastUpdated;
    }

    // -------- Getters and Setters --------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescriptions() { return prescriptions; }
    public void setPrescriptions(String prescriptions) { this.prescriptions = prescriptions; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }
}
