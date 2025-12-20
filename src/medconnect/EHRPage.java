package medconnect;

import medconnect.dao.EHRDAO;
import medconnect.dao.PatientDAO;
import medconnect.model.EHR;
import medconnect.model.Patient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EHRPage extends JFrame {

    private JComboBox<String> patientBox;
    private JTextArea diagnosisArea, prescriptionsArea, notesArea;
    private JTable table;
    private DefaultTableModel tableModel;
    private EHRDAO ehrDAO = new EHRDAO();
    private PatientDAO patientDAO = new PatientDAO();

    public EHRPage() {
        setTitle("MedConnect - Electronic Health Records");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Electronic Health Records (EHR)");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(250, 10, 500, 40);
        main.add(title);

        // Patient selection
        JLabel patientLabel = new JLabel("Select Patient:");
        patientLabel.setBounds(50, 60, 150, 25);
        main.add(patientLabel);

        patientBox = new JComboBox<>();
        patientBox.setBounds(200, 60, 300, 25);
        main.add(patientBox);

        // Diagnosis TextArea
        JLabel diagLabel = new JLabel("Diagnosis:");
        diagLabel.setBounds(50, 100, 120, 25);
        main.add(diagLabel);
        diagnosisArea = new JTextArea();
        diagnosisArea.setLineWrap(true);
        diagnosisArea.setWrapStyleWord(true);
        diagnosisArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        JScrollPane diagScroll = new JScrollPane(diagnosisArea);
        diagScroll.setBounds(200, 100, 300, 80);
        main.add(diagScroll);

        // Prescriptions TextArea
        JLabel presLabel = new JLabel("Prescriptions:");
        presLabel.setBounds(50, 190, 120, 25);
        main.add(presLabel);
        prescriptionsArea = new JTextArea();
        prescriptionsArea.setLineWrap(true);
        prescriptionsArea.setWrapStyleWord(true);
        prescriptionsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        JScrollPane presScroll = new JScrollPane(prescriptionsArea);
        presScroll.setBounds(200, 190, 300, 80);
        main.add(presScroll);

        // Notes TextArea
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(50, 280, 120, 25);
        main.add(notesLabel);
        notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setBounds(200, 280, 300, 80);
        main.add(notesScroll);

        // Buttons
        JButton addBtn = new JButton("Add EHR");
        addBtn.setBounds(550, 100, 150, 30);
        main.add(addBtn);

        JButton updateBtn = new JButton("Update EHR");
        updateBtn.setBounds(550, 150, 150, 30);
        main.add(updateBtn);

        JButton deleteBtn = new JButton("Delete EHR");
        deleteBtn.setBounds(550, 200, 150, 30);
        main.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(550, 250, 150, 30);
        main.add(clearBtn);

        // Table
        tableModel = new DefaultTableModel(new String[]{"EHR ID", "Patient", "Diagnosis", "Prescriptions", "Notes", "Last Updated"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBounds(50, 380, 850, 160);
        main.add(tableScroll);

        add(main);
        setVisible(true);

        // Load patient combo and table
        loadPatientCombo();
        loadTableData();

        // Button actions
        addBtn.addActionListener(e -> addEHR());
        updateBtn.addActionListener(e -> updateEHR());
        deleteBtn.addActionListener(e -> deleteEHR());
        clearBtn.addActionListener(e -> clearFields());

        // Table click to populate fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    patientBox.setSelectedItem(table.getValueAt(row, 1).toString());
                    diagnosisArea.setText(table.getValueAt(row, 2).toString());
                    prescriptionsArea.setText(table.getValueAt(row, 3).toString());
                    notesArea.setText(table.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void loadPatientCombo() {
        patientBox.removeAllItems();
        List<Patient> list = patientDAO.getAllPatients();
        for (Patient p : list) {
            patientBox.addItem(p.getId() + " - " + p.getName());
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            List<EHR> ehrList = ehrDAO.getEHRByPatient(p.getId());
            for (EHR e : ehrList) {
                tableModel.addRow(new Object[]{
                        "EHR-" + e.getId(),
                        p.getId() + " - " + p.getName(),
                        e.getDiagnosis(),
                        e.getPrescriptions(),
                        e.getNotes(),
                        e.getLastUpdated()
                });
            }
        }
    }

    private void addEHR() {
        try {
            String selectedPatient = (String) patientBox.getSelectedItem();
            int patientId = Integer.parseInt(selectedPatient.split(" - ")[0]);
            EHR ehr = new EHR();
            ehr.setPatientId(patientId);
            ehr.setDiagnosis(diagnosisArea.getText());
            ehr.setPrescriptions(prescriptionsArea.getText());
            ehr.setNotes(notesArea.getText());
            ehrDAO.addEHR(ehr);
            JOptionPane.showMessageDialog(this, "EHR Added Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateEHR() {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an EHR to update!");
                return;
            }
            int ehrId = Integer.parseInt(table.getValueAt(row, 0).toString().replace("EHR-", ""));
            EHR ehr = new EHR();
            ehr.setId(ehrId);
            String selectedPatient = (String) patientBox.getSelectedItem();
            ehr.setPatientId(Integer.parseInt(selectedPatient.split(" - ")[0]));
            ehr.setDiagnosis(diagnosisArea.getText());
            ehr.setPrescriptions(prescriptionsArea.getText());
            ehr.setNotes(notesArea.getText());
            ehrDAO.updateEHR(ehr);
            JOptionPane.showMessageDialog(this, "EHR Updated Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteEHR() {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select an EHR to delete!");
                return;
            }
            int ehrId = Integer.parseInt(table.getValueAt(row, 0).toString().replace("EHR-", ""));
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ehrDAO.deleteEHR(ehrId);
                JOptionPane.showMessageDialog(this, "EHR Deleted Successfully!");
                loadTableData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        diagnosisArea.setText("");
        prescriptionsArea.setText("");
        notesArea.setText("");
        if (patientBox.getItemCount() > 0)
            patientBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new EHRPage();
    }
}
