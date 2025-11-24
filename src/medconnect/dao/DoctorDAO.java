package medconnect.dao;

import medconnect.model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // Add doctor
    public void addDoctor(Doctor doctor) {
        String sqlUser = "INSERT INTO users(username, password, name, role) VALUES(?, ?, ?, 'doctor')";
        String sqlDoctor = "INSERT INTO doctors(doctor_id, specialization, experience, gender) VALUES(?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement psUser = con.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {

            psUser.setString(1, doctor.getName().toLowerCase().replaceAll(" ", ""));
            psUser.setString(2, "password123");
            psUser.setString(3, doctor.getName());
            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                PreparedStatement psDoc = con.prepareStatement(sqlDoctor);
                psDoc.setInt(1, id);
                psDoc.setString(2, doctor.getSpecialization());
                psDoc.setInt(3, doctor.getExperience());
                psDoc.setString(4, doctor.getGender());
                psDoc.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update doctor
    public void updateDoctor(Doctor doctor) {
        String sqlUser = "UPDATE users SET name=? WHERE id=?";
        String sqlDoctor = "UPDATE doctors SET specialization=?, experience=?, gender=? WHERE doctor_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement psUser = con.prepareStatement(sqlUser);
             PreparedStatement psDoc = con.prepareStatement(sqlDoctor)) {

            psUser.setString(1, doctor.getName());
            psUser.setInt(2, doctor.getId());
            psUser.executeUpdate();

            psDoc.setString(1, doctor.getSpecialization());
            psDoc.setInt(2, doctor.getExperience());
            psDoc.setString(3, doctor.getGender());
            psDoc.setInt(4, doctor.getId());
            psDoc.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete doctor
    public void deleteDoctor(int id) {
        String sqlDoctor = "DELETE FROM doctors WHERE doctor_id=?";
        String sqlUser = "DELETE FROM users WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement psDoc = con.prepareStatement(sqlDoctor);
             PreparedStatement psUser = con.prepareStatement(sqlUser)) {

            psDoc.setInt(1, id);
            psDoc.executeUpdate();

            psUser.setInt(1, id);
            psUser.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT u.id, u.name, d.specialization, d.experience, d.gender " +
                "FROM users u INNER JOIN doctors d ON u.id=d.doctor_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("specialization"),
                        rs.getInt("experience")
                );
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
