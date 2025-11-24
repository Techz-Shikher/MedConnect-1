package medconnect;

import medconnect.dao.PrescriptionDAO;
import medconnect.model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class PrescriptionPage extends JFrame {

    private JTextField idField, appointmentIdField, dosageField, durationField;
    private JComboBox<String> medicineBox;
    private JTable table;
    private DefaultTableModel tableModel;

    private PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    public PrescriptionPage() {
        setTitle("MedConnect - Prescription Management");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Prescription Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(250, 10, 400, 40);
        main.add(title);

        // Labels and Fields
        JLabel idLabel = new JLabel("Prescription ID:");
        idLabel.setBounds(50, 60, 120, 25);
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(idLabel);

        idField = new JTextField("PR-" + (new Random().nextInt(900) + 100));
        idField.setBounds(200, 60, 150, 25);
        idField.setEditable(false);
        main.add(idField);

        JLabel appointmentLabel = new JLabel("Appointment ID:");
        appointmentLabel.setBounds(50, 100, 130, 25);
        appointmentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(appointmentLabel);

        appointmentIdField = new JTextField();
        appointmentIdField.setBounds(200, 100, 150, 25);
        main.add(appointmentIdField);

        JLabel medicineLabel = new JLabel("Medicine:");
        medicineLabel.setBounds(50, 140, 100, 25);
        medicineLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(medicineLabel);

        medicineBox = new JComboBox<>();
        medicineBox.setBounds(200, 140, 150, 25);
        main.add(medicineBox);

        JLabel dosageLabel = new JLabel("Dosage:");
        dosageLabel.setBounds(50, 180, 100, 25);
        dosageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(dosageLabel);

        dosageField = new JTextField();
        dosageField.setBounds(200, 180, 150, 25);
        main.add(dosageField);

        JLabel durationLabel = new JLabel("Duration (Days):");
        durationLabel.setBounds(50, 220, 130, 25);
        durationLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(durationLabel);

        durationField = new JTextField();
        durationField.setBounds(200, 220, 150, 25);
        main.add(durationField);

        // Buttons
        JButton addBtn = new JButton("Add Prescription");
        addBtn.setBounds(50, 270, 160, 30);
        main.add(addBtn);

        JButton updateBtn = new JButton("Update Prescription");
        updateBtn.setBounds(220, 270, 170, 30);
        main.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Prescription");
        deleteBtn.setBounds(410, 270, 170, 30);
        main.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(600, 270, 100, 30);
        main.add(clearBtn);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Appointment ID", "Medicine ID", "Dosage", "Duration"});
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 320, 700, 200);
        main.add(scroll);

        add(main);
        setVisible(true);

        // Load data into table and medicine combo
        loadTableData();
        loadMedicineCombo();

        // Button Actions
        addBtn.addActionListener(e -> addPrescription());
        updateBtn.addActionListener(e -> updatePrescription());
        deleteBtn.addActionListener(e -> deletePrescription());
        clearBtn.addActionListener(e -> clearFields());

        // Table click to populate fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    idField.setText(table.getValueAt(selectedRow, 0).toString());
                    appointmentIdField.setText(table.getValueAt(selectedRow, 1).toString());
                    medicineBox.setSelectedItem(table.getValueAt(selectedRow, 2).toString());
                    dosageField.setText(table.getValueAt(selectedRow, 3).toString());
                    durationField.setText(table.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Prescription> prescriptions = prescriptionDAO.getAllPrescriptions();
        for (Prescription p : prescriptions) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getAppointmentId(), p.getMedicineId(), p.getDosage(), p.getDurationDays()
            });
        }
    }

    private void loadMedicineCombo() {
        medicineBox.removeAllItems();
        prescriptionDAO.getAllMedicines().forEach(m -> medicineBox.addItem(m.getId() + " - " + m.getName()));
    }

    private void addPrescription() {
        try {
            Prescription p = new Prescription();
            p.setAppointmentId(Integer.parseInt(appointmentIdField.getText()));
            String medItem = (String) medicineBox.getSelectedItem();
            p.setMedicineId(Integer.parseInt(medItem.split(" - ")[0]));
            p.setDosage(dosageField.getText());
            p.setDurationDays(Integer.parseInt(durationField.getText()));

            prescriptionDAO.addPrescription(p);
            JOptionPane.showMessageDialog(this, "Prescription Added Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePrescription() {
        try {
            Prescription p = new Prescription();
            p.setId(Integer.parseInt(idField.getText().replace("PR-", "")));
            p.setAppointmentId(Integer.parseInt(appointmentIdField.getText()));
            String medItem = (String) medicineBox.getSelectedItem();
            p.setMedicineId(Integer.parseInt(medItem.split(" - ")[0]));
            p.setDosage(dosageField.getText());
            p.setDurationDays(Integer.parseInt(durationField.getText()));

            prescriptionDAO.updatePrescription(p);
            JOptionPane.showMessageDialog(this, "Prescription Updated Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deletePrescription() {
        try {
            int id = Integer.parseInt(idField.getText().replace("PR-", ""));
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                prescriptionDAO.deletePrescription(id);
                JOptionPane.showMessageDialog(this, "Prescription Deleted Successfully!");
                loadTableData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("PR-" + (new Random().nextInt(900) + 100));
        appointmentIdField.setText("");
        dosageField.setText("");
        durationField.setText("");
        medicineBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new PrescriptionPage();
    }
}
