package medconnect.dao;

import medconnect.model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Add patient
    public void addPatient(Patient p) {
        String sqlUser = "INSERT INTO users(username, password, name, role) VALUES(?, ?, ?, 'patient')";
        String sqlPatient = "INSERT INTO patients(patient_id, age, gender, blood_group) VALUES(?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement psUser = con.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, p.getName().toLowerCase().replaceAll(" ", ""));
            psUser.setString(2, "password123");
            psUser.setString(3, p.getName());
            psUser.executeUpdate();

            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                PreparedStatement psPat = con.prepareStatement(sqlPatient);
                psPat.setInt(1, id);
                psPat.setInt(2, p.getAge());
                psPat.setString(3, p.getGender());
                psPat.setString(4, p.getBloodGroup());
                psPat.executeUpdate();
            }
            psUser.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update patient
    public void updatePatient(Patient p) {
        String sqlUser = "UPDATE users SET name=? WHERE id=?";
        String sqlPatient = "UPDATE patients SET age=?, gender=?, blood_group=? WHERE patient_id=?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement psUser = con.prepareStatement(sqlUser);
            psUser.setString(1, p.getName());
            psUser.setInt(2, p.getId());
            psUser.executeUpdate();

            PreparedStatement psPat = con.prepareStatement(sqlPatient);
            psPat.setInt(1, p.getAge());
            psPat.setString(2, p.getGender());
            psPat.setString(3, p.getBloodGroup());
            psPat.setInt(4, p.getId());
            psPat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete patient
    public void deletePatient(int id) {
        String sqlPatient = "DELETE FROM patients WHERE patient_id=?";
        String sqlUser = "DELETE FROM users WHERE id=?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement psPat = con.prepareStatement(sqlPatient);
            psPat.setInt(1, id);
            psPat.executeUpdate();

            PreparedStatement psUser = con.prepareStatement(sqlUser);
            psUser.setInt(1, id);
            psUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT u.id, u.name, p.age, p.gender, p.blood_group " +
                "FROM users u INNER JOIN patients p ON u.id=p.patient_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("blood_group")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
