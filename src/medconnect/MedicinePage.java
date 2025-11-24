package medconnect;

import medconnect.dao.MedicineDAO;
import medconnect.model.Medicine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedicinePage extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField nameField, descField, priceField;
    private MedicineDAO dao = new MedicineDAO();

    public MedicinePage() {
        setTitle("MedConnect - Medicine Management");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        // ---------------- Labels ----------------
        JLabel title = new JLabel("Medicine Management");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(250, 20, 400, 40);
        main.add(title);

        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setBounds(50, 100, 150, 30);
        main.add(nameLabel);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 18));
        descLabel.setBounds(50, 150, 150, 30);
        main.add(descLabel);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setBounds(50, 200, 150, 30);
        main.add(priceLabel);

        // ---------------- Fields ----------------
        nameField = new JTextField();
        nameField.setBounds(200, 100, 250, 30);
        main.add(nameField);

        descField = new JTextField();
        descField.setBounds(200, 150, 250, 30);
        main.add(descField);

        priceField = new JTextField();
        priceField.setBounds(200, 200, 250, 30);
        main.add(priceField);

        // ---------------- Buttons ----------------
        JButton addBtn = new JButton("Add Medicine");
        addBtn.setBounds(500, 100, 150, 30);
        main.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(500, 150, 150, 30);
        main.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(500, 200, 150, 30);
        main.add(deleteBtn);

        JButton resetBtn = new JButton("Reset");
        resetBtn.setBounds(500, 250, 150, 30);
        main.add(resetBtn);

        // ---------------- Table ----------------
        model = new DefaultTableModel(new String[]{"ID", "Name", "Description", "Price"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 300, 700, 180);
        main.add(scroll);

        // Load data
        loadTable();

        // ---------------- Button Actions ----------------
        addBtn.addActionListener(e -> addMedicine());
        updateBtn.addActionListener(e -> updateMedicine());
        deleteBtn.addActionListener(e -> deleteMedicine());
        resetBtn.addActionListener(e -> resetFields());

        // Fill fields on table row click
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    nameField.setText(model.getValueAt(row, 1).toString());
                    descField.setText(model.getValueAt(row, 2).toString());
                    priceField.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        add(main);
        setVisible(true);
    }

    // ---------------- CRUD Methods ----------------
    private void addMedicine() {
        if (nameField.getText().isEmpty() || priceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Price are required!");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            Medicine m = new Medicine(0, nameField.getText(), descField.getText(), price);
            dao.addMedicine(m);
            loadTable();
            resetFields();
            JOptionPane.showMessageDialog(this, "Medicine added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price value!");
        }
    }

    private void updateMedicine() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a medicine to update!");
            return;
        }

        try {
            int id = (int) model.getValueAt(row, 0);
            double price = Double.parseDouble(priceField.getText());
            Medicine m = new Medicine(id, nameField.getText(), descField.getText(), price);
            dao.updateMedicine(m);
            loadTable();
            resetFields();
            JOptionPane.showMessageDialog(this, "Medicine updated successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price value!");
        }
    }

    private void deleteMedicine() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a medicine to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this medicine?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            dao.deleteMedicine(id);
            loadTable();
            resetFields();
            JOptionPane.showMessageDialog(this, "Medicine deleted successfully!");
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        List<Medicine> list = dao.getAllMedicines();
        for (Medicine m : list) {
            model.addRow(new Object[]{m.getId(), m.getName(), m.getDescription(), m.getPrice()});
        }
    }

    private void resetFields() {
        nameField.setText("");
        descField.setText("");
        priceField.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        new MedicinePage();
    }
}
