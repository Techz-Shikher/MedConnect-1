package medconnect;

import medconnect.dao.AmbulanceDAO;
import medconnect.dao.PatientDAO;
import medconnect.model.AmbulanceRequest;
import medconnect.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AmbulancePage extends JFrame {

    private JComboBox<Patient> patientBox;
    private JTextField dropField;
    private JButton requestBtn, clearBtn;
    private JTable historyTable;
    private AmbulanceDAO dao = new AmbulanceDAO();

    public AmbulancePage() {

        setTitle("MedConnect - Ambulance Request");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        JLabel title = new JLabel("Ambulance Request");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(300, 20, 400, 40);
        main.add(title);

        JLabel patientLabel = new JLabel("Select Patient:");
        patientLabel.setBounds(50, 80, 150, 30);
        main.add(patientLabel);

        patientBox = new JComboBox<>();
        patientBox.setBounds(200, 80, 250, 30);
        main.add(patientBox);

        JLabel dropLabel = new JLabel("Drop Location:");
        dropLabel.setBounds(50, 130, 150, 30);
        main.add(dropLabel);

        dropField = new JTextField();
        dropField.setBounds(200, 130, 250, 30);
        main.add(dropField);

        requestBtn = new JButton("Request Ambulance");
        requestBtn.setBounds(200, 180, 180, 35);
        main.add(requestBtn);

        clearBtn = new JButton("Clear");
        clearBtn.setBounds(400, 180, 100, 35);
        main.add(clearBtn);

        historyTable = new JTable();
        JScrollPane scroll = new JScrollPane(historyTable);
        scroll.setBounds(50, 250, 800, 300);
        main.add(scroll);

        add(main);

        loadPatients();
        loadHistory();

        requestBtn.addActionListener(e -> requestAmbulance());
        clearBtn.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private void loadPatients() {
        PatientDAO pDao = new PatientDAO();
        List<Patient> patients = pDao.getAllPatients();
        for (Patient p : patients) {
            patientBox.addItem(p);
        }
    }

    private void loadHistory() {
        List<AmbulanceRequest> list = dao.getAllRequests();
        String[] columns = {"ID", "Patient", "Ambulance Type", "Request Time", "Drop Location", "Status"};
        String[][] data = new String[list.size()][6];

        for (int i = 0; i < list.size(); i++) {
            AmbulanceRequest r = list.get(i);
            data[i][0] = String.valueOf(r.getId());
            data[i][1] = r.getPatientName();
            data[i][2] = r.getAmbulanceType();
            data[i][3] = r.getRequestTime().toString();
            data[i][4] = r.getDropLocation();
            data[i][5] = r.getStatus();
        }

        historyTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    private void requestAmbulance() {
        Patient p = (Patient) patientBox.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Select a patient!");
            return;
        }
        if (dropField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter drop location!");
            return;
        }

        AmbulanceRequest req = new AmbulanceRequest(p.getId(), dropField.getText());
        dao.addRequest(req);

        JOptionPane.showMessageDialog(this, "Ambulance Requested Successfully!");
        loadHistory();
    }

    private void clearFields() {
        patientBox.setSelectedIndex(0);
        dropField.setText("");
    }

    public static void main(String[] args) {
        new AmbulancePage();
    }
}
