package medconnect;

import medconnect.dao.InvoiceDAO;
import medconnect.model.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class InvoicePage extends JFrame {

    private JTextField idField, patientField, appointmentField, amountField, gstField, totalField;
    private JComboBox<String> methodBox, statusBox;
    private JTable table;
    private DefaultTableModel tableModel;

    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    public InvoicePage() {
        setTitle("MedConnect - Invoice Management");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Billing Invoice");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(280, 10, 400, 40);
        main.add(title);

        // ---------------- Fields ----------------
        JLabel idLabel = new JLabel("Bill ID:");
        idLabel.setBounds(50, 60, 100, 25);
        main.add(idLabel);

        idField = new JTextField("INV-" + (new Random().nextInt(9000) + 1000));
        idField.setBounds(160, 60, 150, 25);
        idField.setEditable(false);
        main.add(idField);

        JLabel patientLabel = new JLabel("Patient ID:");
        patientLabel.setBounds(50, 100, 100, 25);
        main.add(patientLabel);

        patientField = new JTextField();
        patientField.setBounds(160, 100, 150, 25);
        main.add(patientField);

        JLabel appointmentLabel = new JLabel("Appointment ID:");
        appointmentLabel.setBounds(50, 140, 120, 25);
        main.add(appointmentLabel);

        appointmentField = new JTextField();
        appointmentField.setBounds(160, 140, 150, 25);
        main.add(appointmentField);

        JLabel amountLabel = new JLabel("Amount (₹):");
        amountLabel.setBounds(50, 180, 100, 25);
        main.add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(160, 180, 150, 25);
        main.add(amountField);

        JLabel gstLabel = new JLabel("GST (18%):");
        gstLabel.setBounds(50, 220, 100, 25);
        main.add(gstLabel);

        gstField = new JTextField();
        gstField.setBounds(160, 220, 150, 25);
        gstField.setEditable(false);
        main.add(gstField);

        JLabel totalLabel = new JLabel("Total (₹):");
        totalLabel.setBounds(50, 260, 100, 25);
        main.add(totalLabel);

        totalField = new JTextField();
        totalField.setBounds(160, 260, 150, 25);
        totalField.setEditable(false);
        main.add(totalField);

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setBounds(50, 300, 120, 25);
        main.add(methodLabel);

        String[] methods = {"Cash", "UPI", "Card", "Net Banking"};
        methodBox = new JComboBox<>(methods);
        methodBox.setBounds(160, 300, 150, 25);
        main.add(methodBox);

        JLabel statusLabel = new JLabel("Payment Status:");
        statusLabel.setBounds(50, 340, 120, 25);
        main.add(statusLabel);

        String[] statuses = {"paid", "unpaid"};
        statusBox = new JComboBox<>(statuses);
        statusBox.setBounds(160, 340, 150, 25);
        main.add(statusBox);

        // ---------------- Buttons ----------------
        JButton calcBtn = new JButton("Calculate Total");
        calcBtn.setBounds(330, 60, 150, 30);
        main.add(calcBtn);

        JButton addBtn = new JButton("Add Invoice");
        addBtn.setBounds(330, 100, 150, 30);
        main.add(addBtn);

        JButton updateBtn = new JButton("Update Invoice");
        updateBtn.setBounds(330, 140, 150, 30);
        main.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Invoice");
        deleteBtn.setBounds(330, 180, 150, 30);
        main.add(deleteBtn);

        JButton clearBtn = new JButton("Clear Fields");
        clearBtn.setBounds(330, 220, 150, 30);
        main.add(clearBtn);

        // ---------------- Table ----------------
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Bill ID", "Patient ID", "Appointment ID", "Amount", "Bill Date", "Status", "Method"});
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 380, 750, 150);
        main.add(scroll);

        add(main);
        setVisible(true);

        // Load table
        loadTableData();

        DecimalFormat df = new DecimalFormat("0.00");

        // ---------------- Button Actions ----------------
        calcBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                double gst = amount * 0.18;
                double total = amount + gst;
                gstField.setText(df.format(gst));
                totalField.setText(df.format(total));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount!");
            }
        });

        addBtn.addActionListener(e -> addInvoice());
        updateBtn.addActionListener(e -> updateInvoice());
        deleteBtn.addActionListener(e -> deleteInvoice());
        clearBtn.addActionListener(e -> clearFields());

        // Table click to populate fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(table.getValueAt(row, 0).toString());
                    patientField.setText(table.getValueAt(row, 1).toString());
                    appointmentField.setText(table.getValueAt(row, 2).toString());
                    amountField.setText(table.getValueAt(row, 3).toString());
                    totalField.setText(table.getValueAt(row, 4).toString());
                    statusBox.setSelectedItem(table.getValueAt(row, 5).toString());
                    methodBox.setSelectedItem(table.getValueAt(row, 6).toString());
                }
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Invoice inv : invoices) {
            tableModel.addRow(new Object[]{
                    "INV-" + inv.getId(),
                    inv.getPatientId(),
                    inv.getAppointmentId(),
                    inv.getAmount(),
                    sdf.format(inv.getBillDate()),
                    inv.getPaymentStatus(),
                    methodBox.getSelectedItem()
            });
        }
    }

    private void addInvoice() {
        try {
            Invoice inv = new Invoice();
            inv.setPatientId(Integer.parseInt(patientField.getText()));
            inv.setAppointmentId(Integer.parseInt(appointmentField.getText()));
            inv.setAmount(Double.parseDouble(amountField.getText()));
            inv.setBillDate(new Date());
            inv.setPaymentStatus((String) statusBox.getSelectedItem());

            invoiceDAO.addInvoice(inv);
            JOptionPane.showMessageDialog(this, "Invoice Added Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateInvoice() {
        try {
            Invoice inv = new Invoice();
            inv.setId(Integer.parseInt(idField.getText().replace("INV-", "")));
            inv.setPatientId(Integer.parseInt(patientField.getText()));
            inv.setAppointmentId(Integer.parseInt(appointmentField.getText()));
            inv.setAmount(Double.parseDouble(amountField.getText()));
            inv.setBillDate(new Date());
            inv.setPaymentStatus((String) statusBox.getSelectedItem());

            invoiceDAO.updateInvoice(inv);
            JOptionPane.showMessageDialog(this, "Invoice Updated Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteInvoice() {
        try {
            int id = Integer.parseInt(idField.getText().replace("INV-", ""));
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                invoiceDAO.deleteInvoice(id);
                JOptionPane.showMessageDialog(this, "Invoice Deleted Successfully!");
                loadTableData();
                clearFields();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("INV-" + (new Random().nextInt(9000) + 1000));
        patientField.setText("");
        appointmentField.setText("");
        amountField.setText("");
        gstField.setText("");
        totalField.setText("");
        statusBox.setSelectedIndex(0);
        methodBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new InvoicePage();
    }
}
