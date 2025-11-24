package medconnect;

import medconnect.dao.AppointmentDAO;
import medconnect.model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentPage extends JFrame {

    private JTextField idField, patientField, doctorField, dateField;
    private JComboBox<String> statusBox;
    private JTable table;
    private DefaultTableModel tableModel;

    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AppointmentPage() {

        setTitle("MedConnect - Appointments");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Appointment Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(250, 10, 500, 40);
        main.add(title);

        // Labels and Fields
        JLabel idLabel = new JLabel("Appointment ID:");
        idLabel.setBounds(50, 70, 150, 25);
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(idLabel);

        idField = new JTextField();
        idField.setBounds(200, 70, 150, 25);
        idField.setEditable(false);
        main.add(idField);

        JLabel patientLabel = new JLabel("Patient ID:");
        patientLabel.setBounds(50, 110, 150, 25);
        patientLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(patientLabel);

        patientField = new JTextField();
        patientField.setBounds(200, 110, 150, 25);
        main.add(patientField);

        JLabel doctorLabel = new JLabel("Doctor ID:");
        doctorLabel.setBounds(50, 150, 150, 25);
        doctorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(doctorLabel);

        doctorField = new JTextField();
        doctorField.setBounds(200, 150, 150, 25);
        main.add(doctorField);

        JLabel dateLabel = new JLabel("Date & Time:");
        dateLabel.setBounds(50, 190, 150, 25);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(dateLabel);

        dateField = new JTextField(dtf.format(LocalDateTime.now()));
        dateField.setBounds(200, 190, 150, 25);
        main.add(dateField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(50, 230, 150, 25);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(statusLabel);

        statusBox = new JComboBox<>(new String[]{"scheduled", "completed", "cancelled"});
        statusBox.setBounds(200, 230, 150, 25);
        main.add(statusBox);

        // Buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(50, 280, 100, 35);
        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(160, 280, 100, 35);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(270, 280, 100, 35);
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(380, 280, 100, 35);

        main.add(addBtn);
        main.add(updateBtn);
        main.add(deleteBtn);
        main.add(clearBtn);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Appointment ID", "Patient ID", "Doctor ID", "Date & Time", "Status"});
        table = new JTable(tableModel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 340, 780, 250);
        main.add(scroll);

        add(main);
        setVisible(true);

        // load data
        loadTableData();

        // Button Actions
        addBtn.addActionListener(e -> addAppointment());
        updateBtn.addActionListener(e -> updateAppointment());
        deleteBtn.addActionListener(e -> deleteAppointment());
        clearBtn.addActionListener(e -> clearFields());

        // When row clicked → load data
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selected = table.getSelectedRow();
                if (selected >= 0) {
                    idField.setText(table.getValueAt(selected, 0).toString());
                    patientField.setText(table.getValueAt(selected, 1).toString());
                    doctorField.setText(table.getValueAt(selected, 2).toString());
                    dateField.setText(table.getValueAt(selected, 3).toString());
                    statusBox.setSelectedItem(table.getValueAt(selected, 4).toString());
                }
            }
        });
    }

    // LOAD TABLE FROM DATABASE
    private void loadTableData() {
        tableModel.setRowCount(0);

        List<Appointment> list = appointmentDAO.getAllAppointments();

        for (Appointment a : list) {
            tableModel.addRow(new Object[]{
                    a.getId(),                                  // pure number
                    a.getPatientId(),
                    a.getDoctorId(),
                    dtf.format(a.getAppointmentDate()),
                    a.getStatus()
            });
        }
    }

    // ADD APPOINTMENT
    private void addAppointment() {
        try {
            Appointment a = new Appointment();
            a.setPatientId(Integer.parseInt(patientField.getText()));
            a.setDoctorId(Integer.parseInt(doctorField.getText()));
            a.setAppointmentDate(LocalDateTime.parse(dateField.getText(), dtf));
            a.setStatus(statusBox.getSelectedItem().toString());

            appointmentDAO.addAppointment(a);

            JOptionPane.showMessageDialog(this, "Appointment Added Successfully!");

            loadTableData();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // UPDATE APPOINTMENT
    private void updateAppointment() {
        try {
            Appointment a = new Appointment();
            a.setId(Integer.parseInt(idField.getText()));
            a.setPatientId(Integer.parseInt(patientField.getText()));
            a.setDoctorId(Integer.parseInt(doctorField.getText()));
            a.setAppointmentDate(LocalDateTime.parse(dateField.getText(), dtf));
            a.setStatus(statusBox.getSelectedItem().toString());

            appointmentDAO.updateAppointment(a);

            JOptionPane.showMessageDialog(this, "Appointment Updated Successfully!");

            loadTableData();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // DELETE APPOINTMENT
    private void deleteAppointment() {
        try {
            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to delete?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                appointmentDAO.deleteAppointment(id);
                JOptionPane.showMessageDialog(this, "Appointment Deleted Successfully!");
                loadTableData();
                clearFields();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // CLEAR FIELDS
    private void clearFields() {
        idField.setText("");
        patientField.setText("");
        doctorField.setText("");
        dateField.setText(dtf.format(LocalDateTime.now()));
        statusBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new AppointmentPage();
    }
}
