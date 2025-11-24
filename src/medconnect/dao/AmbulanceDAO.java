package medconnect.dao;

import medconnect.model.AmbulanceRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmbulanceDAO {

    // INSERT REQUEST
    public void addRequest(AmbulanceRequest request) {
        String sql = "INSERT INTO ambulance_requests (patient_id, ambulance_id, request_time, drop_location, status) " +
                "VALUES (?, NULL, NOW(), ?, 'requested')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, request.getPatientId());
            ps.setString(2, request.getDropLocation());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // SELECT ALL REQUESTS
    public List<AmbulanceRequest> getAllRequests() {
        List<AmbulanceRequest> list = new ArrayList<>();

        String sql = """
                SELECT ar.request_id, p.name AS patient_name, 
                       COALESCE(a.ambulance_type, 'Pending') AS ambulance_type,
                       ar.request_time, ar.drop_location, ar.status
                FROM ambulance_requests ar
                INNER JOIN patients p ON ar.patient_id = p.patient_id
                LEFT JOIN ambulance a ON ar.ambulance_id = a.ambulance_id
                ORDER BY ar.request_id DESC
                """;

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                AmbulanceRequest r = new AmbulanceRequest(
                        rs.getInt("request_id"),
                        rs.getString("patient_name"),
                        rs.getString("ambulance_type"),
                        rs.getTimestamp("request_time"),
                        rs.getString("drop_location"),
                        rs.getString("status")
                );
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
