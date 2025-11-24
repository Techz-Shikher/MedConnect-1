package medconnect.model;

import java.sql.Timestamp;

public class AmbulanceRequest {

    private int id;
    private String patientName;
    private String ambulanceType;
    private Timestamp requestTime;
    private String dropLocation;
    private String status;
    private int patientId;

    public AmbulanceRequest() {}

    // For INSERT
    public AmbulanceRequest(int patientId, String dropLocation) {
        this.patientId = patientId;
        this.dropLocation = dropLocation;
        this.status = "requested";
    }

    // For SELECT
    public AmbulanceRequest(int id, String patientName, String ambulanceType,
                            Timestamp requestTime, String dropLocation, String status) {
        this.id = id;
        this.patientName = patientName;
        this.ambulanceType = ambulanceType;
        this.requestTime = requestTime;
        this.dropLocation = dropLocation;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getAmbulanceType() { return ambulanceType; }
    public Timestamp getRequestTime() { return requestTime; }
    public String getDropLocation() { return dropLocation; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setAmbulanceType(String ambulanceType) { this.ambulanceType = ambulanceType; }
    public void setRequestTime(Timestamp requestTime) { this.requestTime = requestTime; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    public void setStatus(String status) { this.status = status; }
}
