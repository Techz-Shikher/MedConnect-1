package medconnect;

import medconnect.dao.DoctorDAO;
import medconnect.model.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DoctorPage extends JFrame {

    private JTextField nameField, expField, searchField;
    private JComboBox<String> specBox, genderBox;
    private JLabel expLevel;
    private JButton addBtn, updateBtn, deleteBtn, resetBtn;
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    private DoctorDAO dao = new DoctorDAO();
    private int selectedDoctorId = -1;

    public DoctorPage() {

        setTitle("MedConnect - Doctor Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Doctor Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(300, 10, 400, 40);
        main.add(title);

        // Labels
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 70, 150, 25);
        main.add(nameLabel);

        JLabel specLabel = new JLabel("Specialization:");
        specLabel.setBounds(50, 110, 150, 25);
        main.add(specLabel);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(50, 150, 150, 25);
        main.add(genderLabel);

        JLabel expLabel = new JLabel("Experience (Years):");
        expLabel.setBounds(50, 190, 150, 25);
        main.add(expLabel);

        JLabel expLevelLabel = new JLabel("Expertise Level:");
        expLevelLabel.setBounds(50, 230, 150, 25);
        main.add(expLevelLabel);

        // Fields
        nameField = new JTextField();
        nameField.setBounds(220, 70, 250, 25);
        main.add(nameField);

        String[] specializations = {"Cardiology", "Neurology", "Orthopedics", "Dermatology",
                "Pediatrics", "ENT", "General Medicine", "Dentist", "Gynecology"};
        specBox = new JComboBox<>(specializations);
        specBox.setBounds(220, 110, 250, 25);
        main.add(specBox);

        String[] genders = {"Male", "Female", "Other"};
        genderBox = new JComboBox<>(genders);
        genderBox.setBounds(220, 150, 250, 25);
        main.add(genderBox);

        expField = new JTextField();
        expField.setBounds(220, 190, 250, 25);
        main.add(expField);

        expLevel = new JLabel("Beginner");
        expLevel.setBounds(220, 230, 250, 25);
        main.add(expLevel);

        // Live experience level calculation
        expField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    int years = Integer.parseInt(expField.getText());
                    if (years <= 3) expLevel.setText("Beginner");
                    else if (years <= 10) expLevel.setText("Experienced");
                    else expLevel.setText("Expert");
                } catch (Exception ex) {
                    expLevel.setText("Beginner");
                }
            }
        });

        // Buttons
        addBtn = new JButton("Add Doctor");
        addBtn.setBounds(50, 280, 150, 35);
        main.add(addBtn);

        updateBtn = new JButton("Update");
        updateBtn.setBounds(220, 280, 150, 35);
        main.add(updateBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(390, 280, 150, 35);
        main.add(deleteBtn);

        resetBtn = new JButton("Reset");
        resetBtn.setBounds(560, 280, 150, 35);
        main.add(resetBtn);

        // Search Field
        searchField = new JTextField();
        searchField.setBounds(720, 70, 150, 25);
        searchField.setText("Search doctor...");
        main.add(searchField);

        // Table
        doctorTable = new JTable();
        JScrollPane scroll = new JScrollPane(doctorTable);
        scroll.setBounds(50, 330, 800, 200);
        main.add(scroll);

        add(main);

        // Load doctors
        loadDoctors();

        // Table row selection
        doctorTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = doctorTable.getSelectedRow();
                if (row >= 0) {
                    selectedDoctorId = Integer.parseInt(doctorTable.getValueAt(row, 0).toString());
                    nameField.setText(doctorTable.getValueAt(row, 1).toString());
                    specBox.setSelectedItem(doctorTable.getValueAt(row, 2).toString());
                    expField.setText(doctorTable.getValueAt(row, 3).toString());
                    genderBox.setSelectedItem(doctorTable.getValueAt(row, 4).toString());
                }
            }
        });

        // Button actions
        addBtn.addActionListener(e -> addDoctor());
        updateBtn.addActionListener(e -> updateDoctor());
        deleteBtn.addActionListener(e -> deleteDoctor());
        resetBtn.addActionListener(e -> resetFields());

        setVisible(true);
    }

    private void loadDoctors() {
        List<Doctor> list = dao.getAllDoctors();
        String[] columns = {"ID", "Name", "Specialization", "Experience", "Gender"};
        String[][] data = new String[list.size()][5];

        for (int i = 0; i < list.size(); i++) {
            Doctor d = list.get(i);
            data[i][0] = String.valueOf(d.getId());
            data[i][1] = d.getName();
            data[i][2] = d.getSpecialization();
            data[i][3] = String.valueOf(d.getExperience());
            data[i][4] = d.getGender();
        }

        tableModel = new DefaultTableModel(data, columns);
        doctorTable.setModel(tableModel);
    }

    private void addDoctor() {
        try {
            int exp = Integer.parseInt(expField.getText());
            Doctor d = new Doctor(0, nameField.getText(), genderBox.getSelectedItem().toString(),
                    specBox.getSelectedItem().toString(), exp);
            dao.addDoctor(d);
            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            loadDoctors();
            resetFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid experience!");
        }
    }

    private void updateDoctor() {
        if (selectedDoctorId == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor from table first!");
            return;
        }
        try {
            int exp = Integer.parseInt(expField.getText());
            Doctor d = new Doctor(selectedDoctorId, nameField.getText(),
                    genderBox.getSelectedItem().toString(),
                    specBox.getSelectedItem().toString(), exp);
            dao.updateDoctor(d);
            JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
            loadDoctors();
            resetFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid experience!");
        }
    }

    private void deleteDoctor() {
        if (selectedDoctorId == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor from table first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteDoctor(selectedDoctorId);
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
            loadDoctors();
            resetFields();
        }
    }

    private void resetFields() {
        nameField.setText("");
        specBox.setSelectedIndex(0);
        genderBox.setSelectedIndex(0);
        expField.setText("");
        expLevel.setText("Beginner");
        selectedDoctorId = -1;
        doctorTable.clearSelection();
    }

    public static void main(String[] args) {
        new DoctorPage();
    }
}
