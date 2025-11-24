package medconnect.model;

import java.util.Date;

public class Invoice {
    private int id;
    private int patientId;
    private int appointmentId;
    private double amount;
    private Date billDate;
    private String paymentStatus;

    public Invoice() {}

    public Invoice(int id, int patientId, int appointmentId, double amount, Date billDate, String paymentStatus) {
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.billDate = billDate;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
