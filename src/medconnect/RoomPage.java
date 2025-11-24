package medconnect;

import medconnect.dao.RoomDAO;
import medconnect.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class RoomPage extends JFrame {

    private JTextField idField, roomNoField, priceField;
    private JComboBox<String> typeBox, statusBox;
    private JTable table;
    private DefaultTableModel tableModel;

    private RoomDAO roomDAO = new RoomDAO();

    public RoomPage() {
        setTitle("MedConnect - Room Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);

        // Title
        JLabel title = new JLabel("Room Management");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(250, 10, 400, 40);
        main.add(title);

        // Labels and Fields
        JLabel idLabel = new JLabel("Room ID:");
        idLabel.setBounds(50, 60, 100, 25);
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(idLabel);

        idField = new JTextField("RM-" + (new Random().nextInt(900) + 100));
        idField.setBounds(160, 60, 150, 25);
        idField.setEditable(false);
        main.add(idField);

        JLabel roomNoLabel = new JLabel("Room Number:");
        roomNoLabel.setBounds(50, 100, 120, 25);
        roomNoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(roomNoLabel);

        roomNoField = new JTextField();
        roomNoField.setBounds(160, 100, 150, 25);
        main.add(roomNoField);

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setBounds(50, 140, 120, 25);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(typeLabel);

        String[] types = {"general", "semi-private", "private", "icu"};
        typeBox = new JComboBox<>(types);
        typeBox.setBounds(160, 140, 150, 25);
        main.add(typeBox);

        JLabel priceLabel = new JLabel("Price / Day:");
        priceLabel.setBounds(50, 180, 120, 25);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(160, 180, 150, 25);
        main.add(priceField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(50, 220, 120, 25);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(statusLabel);

        String[] statusOptions = {"available", "occupied"};
        statusBox = new JComboBox<>(statusOptions);
        statusBox.setBounds(160, 220, 150, 25);
        main.add(statusBox);

        // Buttons
        JButton addBtn = new JButton("Add Room");
        addBtn.setBounds(50, 270, 120, 30);
        main.add(addBtn);

        JButton updateBtn = new JButton("Update Room");
        updateBtn.setBounds(180, 270, 130, 30);
        main.add(updateBtn);

        JButton deleteBtn = new JButton("Delete Room");
        deleteBtn.setBounds(320, 270, 130, 30);
        main.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(460, 270, 100, 30);
        main.add(clearBtn);

        // Table
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Room No", "Type", "Price", "Status"});
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(50, 320, 700, 200);
        main.add(scroll);

        add(main);
        setVisible(true);

        // Load data into table
        loadTableData();

        // Button Actions
        addBtn.addActionListener(e -> addRoom());
        updateBtn.addActionListener(e -> updateRoom());
        deleteBtn.addActionListener(e -> deleteRoom());
        clearBtn.addActionListener(e -> clearFields());

        // Table click to populate fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    idField.setText(table.getValueAt(selectedRow, 0).toString());
                    roomNoField.setText(table.getValueAt(selectedRow, 1).toString());
                    typeBox.setSelectedItem(table.getValueAt(selectedRow, 2).toString());
                    priceField.setText(table.getValueAt(selectedRow, 3).toString());
                    statusBox.setSelectedItem(table.getValueAt(selectedRow, 4).toString());
                }
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getId(), r.getRoomNumber(), r.getRoomType(), r.getPricePerDay(), r.getStatus()
            });
        }
    }

    private void addRoom() {
        if (roomNoField.getText().isEmpty() || priceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }
        try {
            Room room = new Room();
            room.setRoomNumber(roomNoField.getText());
            room.setRoomType((String) typeBox.getSelectedItem());
            room.setPricePerDay(Double.parseDouble(priceField.getText()));
            room.setStatus((String) statusBox.getSelectedItem());

            roomDAO.addRoom(room);
            JOptionPane.showMessageDialog(this, "Room Added Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateRoom() {
        try {
            Room room = new Room();
            room.setId(Integer.parseInt(idField.getText().replace("RM-", "")));
            room.setRoomNumber(roomNoField.getText());
            room.setRoomType((String) typeBox.getSelectedItem());
            room.setPricePerDay(Double.parseDouble(priceField.getText()));
            room.setStatus((String) statusBox.getSelectedItem());

            roomDAO.updateRoom(room);
            JOptionPane.showMessageDialog(this, "Room Updated Successfully!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteRoom() {
        try {
            int id = Integer.parseInt(idField.getText().replace("RM-", ""));
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                roomDAO.deleteRoom(id);
                JOptionPane.showMessageDialog(this, "Room Deleted Successfully!");
                loadTableData();
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("RM-" + (new Random().nextInt(900) + 100));
        roomNoField.setText("");
        typeBox.setSelectedIndex(0);
        priceField.setText("");
        statusBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new RoomPage();
    }
}
