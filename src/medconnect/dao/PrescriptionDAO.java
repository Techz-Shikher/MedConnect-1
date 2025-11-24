package medconnect.dao;

import medconnect.model.Prescription;
import medconnect.model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // Get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Prescription p = new Prescription(
                        rs.getInt("prescription_id"),
                        rs.getInt("appointment_id"),
                        rs.getInt("medicine_id"),
                        rs.getString("dosage"),
                        rs.getInt("duration_days")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add prescription
    public void addPrescription(Prescription p) {
        String sql = "INSERT INTO prescriptions(appointment_id, medicine_id, dosage, duration_days) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getAppointmentId());
            ps.setInt(2, p.getMedicineId());
            ps.setString(3, p.getDosage());
            ps.setInt(4, p.getDurationDays());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update prescription
    public void updatePrescription(Prescription p) {
        String sql = "UPDATE prescriptions SET appointment_id=?, medicine_id=?, dosage=?, duration_days=? WHERE prescription_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getAppointmentId());
            ps.setInt(2, p.getMedicineId());
            ps.setString(3, p.getDosage());
            ps.setInt(4, p.getDurationDays());
            ps.setInt(5, p.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete prescription
    public void deletePrescription(int id) {
        String sql = "DELETE FROM prescriptions WHERE prescription_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all medicines for dropdown
    public List<Medicine> getAllMedicines() {
        List<Medicine> list = new ArrayList<>();
        String sql = "SELECT * FROM medicines";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Medicine m = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                );
                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
