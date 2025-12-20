package medconnect.dao;

import medconnect.model.EHR;
import medconnect.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EHRDAO {

    public List<EHR> getEHRByPatient(int patientId) {
        List<EHR> list = new ArrayList<>();
        String sql = "SELECT * FROM ehr WHERE patient_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new EHR(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("diagnosis"),
                        rs.getString("prescriptions"),
                        rs.getString("notes"),
                        rs.getTimestamp("last_updated")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addEHR(EHR ehr) {
        String sql = "INSERT INTO ehr (patient_id, diagnosis, prescriptions, notes) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ehr.getPatientId());
            ps.setString(2, ehr.getDiagnosis());
            ps.setString(3, ehr.getPrescriptions());
            ps.setString(4, ehr.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEHR(EHR ehr) {
        String sql = "UPDATE ehr SET diagnosis = ?, prescriptions = ?, notes = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ehr.getDiagnosis());
            ps.setString(2, ehr.getPrescriptions());
            ps.setString(3, ehr.getNotes());
            ps.setInt(4, ehr.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEHR(int id) {
        String sql = "DELETE FROM ehr WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
