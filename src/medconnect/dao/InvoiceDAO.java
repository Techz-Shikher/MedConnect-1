package medconnect.dao;

import medconnect.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    // Get all invoices
    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM billing"; // Table name remains 'billing'

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("bill_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("appointment_id"),
                        rs.getDouble("amount"),
                        rs.getDate("bill_date"),
                        rs.getString("payment_status")
                );
                list.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add a new invoice
    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO billing(patient_id, appointment_id, amount, bill_date, payment_status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, invoice.getPatientId());
            ps.setInt(2, invoice.getAppointmentId());
            ps.setDouble(3, invoice.getAmount());
            ps.setDate(4, new java.sql.Date(invoice.getBillDate().getTime()));
            ps.setString(5, invoice.getPaymentStatus());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an invoice
    public void updateInvoice(Invoice invoice) {
        String sql = "UPDATE billing SET patient_id=?, appointment_id=?, amount=?, bill_date=?, payment_status=? WHERE bill_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, invoice.getPatientId());
            ps.setInt(2, invoice.getAppointmentId());
            ps.setDouble(3, invoice.getAmount());
            ps.setDate(4, new java.sql.Date(invoice.getBillDate().getTime()));
            ps.setString(5, invoice.getPaymentStatus());
            ps.setInt(6, invoice.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an invoice
    public void deleteInvoice(int id) {
        String sql = "DELETE FROM billing WHERE bill_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
