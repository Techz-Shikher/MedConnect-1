package medconnect;

import medconnect.dao.PatientDAO;
import medconnect.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PatientPage extends JFrame {

    private JTextField nameField, ageField, searchField;
    private JComboBox<String> genderBox, bloodBox;
    private JLabel category;
    private JTable table;
    private DefaultTableModel model;

    private PatientDAO dao = new PatientDAO();
    private int selectedId = -1;

    public PatientPage() {

        setTitle("MedConnect - Patient Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Patient Management");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(260, 20, 400, 40);
        main.add(title);

        // ---------- LABELS ----------
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setBounds(50, 100, 200, 30);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        ageLabel.setBounds(50, 150, 200, 30);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        genderLabel.setBounds(50, 200, 200, 30);

        JLabel bloodLabel = new JLabel("Blood Group:");
        bloodLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bloodLabel.setBounds(50, 250, 200, 30);

        JLabel categoryLabel = new JLabel("Patient Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        categoryLabel.setBounds(50, 300, 200, 30);

        main.add(nameLabel);
        main.add(ageLabel);
        main.add(genderLabel);
        main.add(bloodLabel);
        main.add(categoryLabel);

        // ---------- INPUT FIELDS ----------
        nameField = new JTextField();
        nameField.setBounds(250, 100, 250, 30);

        ageField = new JTextField();
        ageField.setBounds(250, 150, 250, 30);

        String[] genders = {"Male", "Female", "Other"};
        genderBox = new JComboBox<>(genders);
        genderBox.setBounds(250, 200, 250, 30);

        String[] bloodGroups = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        bloodBox = new JComboBox<>(bloodGroups);
        bloodBox.setBounds(250, 250, 250, 30);

        category = new JLabel("Adult");
        category.setFont(new Font("Arial", Font.BOLD, 20));
        category.setBounds(250, 300, 250, 30);

        main.add(nameField);
        main.add(ageField);
        main.add(genderBox);
        main.add(bloodBox);
        main.add(category);

        ageField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    int age = Integer.parseInt(ageField.getText());
                    if (age < 12) category.setText("Child");
                    else if (age < 60) category.setText("Adult");
                    else category.setText("Senior Citizen");
                } catch (Exception ex) {
                    category.setText("Adult");
                }
            }
        });

        // ---------- SEARCH BAR ----------
        searchField = new JTextField("Search...");
        searchField.setBounds(550, 100, 250, 30);
        main.add(searchField);

        // ---------- TABLE ----------
        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Gender", "Blood Group"}, 0);
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(520, 150, 330, 300);
        main.add(scroll);

        loadPatients();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                ageField.setText(model.getValueAt(row, 2).toString());
                genderBox.setSelectedItem(model.getValueAt(row, 3).toString());
                bloodBox.setSelectedItem(model.getValueAt(row, 4).toString());
            }
        });

        // ---------- BUTTONS ----------
        JButton addBtn = new JButton("Add Patient");
        addBtn.setBounds(50, 380, 160, 40);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(230, 380, 150, 40);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(400, 380, 150, 40);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBounds(570, 380, 150, 40);

        main.add(addBtn);
        main.add(updateBtn);
        main.add(deleteBtn);
        main.add(resetBtn);

        // ---------- EVENT ACTIONS ----------
        addBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        resetBtn.addActionListener(e -> resetFields());

        add(main);
        setVisible(true);
    }

    // ---------- FUNCTIONS ----------
    private void loadPatients() {
        model.setRowCount(0);
        List<Patient> list = dao.getAllPatients();
        for (Patient p : list) {
            model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(), p.getGender(), p.getBloodGroup()
            });
        }
    }

    private void addPatient() {
        try {
            Patient p = new Patient();
            p.setName(nameField.getText());
            p.setAge(Integer.parseInt(ageField.getText()));
            p.setGender((String) genderBox.getSelectedItem());
            p.setBloodGroup((String) bloodBox.getSelectedItem());

            dao.addPatient(p);
            JOptionPane.showMessageDialog(this, "Patient Added Successfully!");
            loadPatients();
            resetFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Fill all fields correctly!");
        }
    }

    private void updatePatient() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        Patient p = new Patient(selectedId, nameField.getText(),
                (String) genderBox.getSelectedItem(),
                Integer.parseInt(ageField.getText()),
                (String) bloodBox.getSelectedItem());

        dao.updatePatient(p);
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        loadPatients();
        resetFields();
    }

    private void deletePatient() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient to delete!");
            return;
        }

        dao.deletePatient(selectedId);
        JOptionPane.showMessageDialog(this, "Deleted Successfully!");
        loadPatients();
        resetFields();
    }

    private void resetFields() {
        selectedId = -1;
        nameField.setText("");
        ageField.setText("");
        genderBox.setSelectedIndex(0);
        bloodBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new PatientPage();
    }
}
