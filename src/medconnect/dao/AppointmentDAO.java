package medconnect.dao;

import medconnect.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // Add appointment
    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, status) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());

            if (a.getAppointmentDate() != null)
                ps.setTimestamp(3, Timestamp.valueOf(a.getAppointmentDate()));
            else
                ps.setTimestamp(3, null);

            ps.setString(4, a.getStatus());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
            return false;
        }
    }

    // Update appointment
    public boolean updateAppointment(Appointment a) {
        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, appointment_date=?, status=? WHERE appointment_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());

            if (a.getAppointmentDate() != null)
                ps.setTimestamp(3, Timestamp.valueOf(a.getAppointmentDate()));
            else
                ps.setTimestamp(3, null);

            ps.setString(4, a.getStatus());
            ps.setInt(5, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
            return false;
        }
    }

    // Delete appointment
    public boolean deleteAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE appointment_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    // Get appointment by ID (important for edit operations)
    public Appointment getAppointmentById(int id) {
        String sql = "SELECT * FROM appointments WHERE appointment_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching appointment by ID: " + e.getMessage());
        }
        return null;
    }

    // Get all appointments (sorted latest first)
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getTimestamp("appointment_date").toLocalDateTime(),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching appointments: " + e.getMessage());
        }
        return list;
    }
}
