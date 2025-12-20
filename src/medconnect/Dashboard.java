package medconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    private String username;
    private String role;

    // No-arg constructor (for testing)
    public Dashboard() {
        this.username = "Guest";
        this.role = "guest";
        initDashboardUI();
    }

    // Constructor for login
    public Dashboard(String username, String role) {
        this.username = username;
        this.role = role;
        initDashboardUI();
    }

    private void initDashboardUI() {
        // ⬆️ FULL SCREEN MODE
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        setTitle("MedConnect - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(370, getHeight()));
        leftPanel.setBackground(new Color(25, 118, 210));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("MedConnect");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 0));
        leftPanel.add(title);

        // WELCOME USER LABEL
        JLabel userLabel = new JLabel("Hello, " + username + " (" + role + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 0));
        leftPanel.add(userLabel);

        // MENU ITEMS (role-based)
        if (!role.equalsIgnoreCase("patient")) { // patient cannot access admin/management menus
            leftPanel.add(createMenuItem("Invoice", "E:/project/MedConnect/src/icon/invoice.png", () -> new InvoicePage()));
            leftPanel.add(createMenuItem("Prescription", "E:/project/MedConnect/src/icon/prescription.jpg", () -> new PrescriptionPage()));
            leftPanel.add(createMenuItem("Medicine", "E:/project/MedConnect/src/icon/medicine.jpg", () -> new MedicinePage()));
        }

        if (role.equalsIgnoreCase("admin")) { // only admin
            leftPanel.add(createMenuItem("Doctor", "E:/project/MedConnect/src/icon/dr.jpg", () -> new DoctorPage()));
            leftPanel.add(createMenuItem("Patient", "E:/project/MedConnect/src/icon/patient.jpg", () -> new PatientPage()));
            leftPanel.add(createMenuItem("Ambulance", "E:/project/MedConnect/src/icon/amb.jpg", () -> new AmbulancePage()));
            leftPanel.add(createMenuItem("Room", "E:/project/MedConnect/src/icon/roomm.jpg", () -> new RoomPage()));
        }

        // EHR accessible for doctors and admin
        if (role.equalsIgnoreCase("doctor") || role.equalsIgnoreCase("admin")) {
            leftPanel.add(createMenuItem("EHR", "E:/project/MedConnect/src/icon/ehr.png", () -> new EHRPage()));
        }

        leftPanel.add(createMenuItem("Appointment", "E:/project/MedConnect/src/icon/appointment.png", () -> new AppointmentPage()));

        // LIVE CLOCK
        JLabel clockLabel = new JLabel("Time: --:--");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 18));
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 0));
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(clockLabel);
        startClockThread(clockLabel);

        add(leftPanel, BorderLayout.WEST);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome to MedConnect Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 60));
        welcome.setForeground(new Color(33, 33, 33));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(welcome, BorderLayout.NORTH);

        ImageIcon dashboardImg = new ImageIcon("E:/project/MedConnect/src/icon/login.jpg");
        Image scaledImg = dashboardImg.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(imgLabel, BorderLayout.CENTER);
        rightPanel.add(centerPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MENU ITEM CREATOR
    private JPanel createMenuItem(String text, String iconPath, Runnable action) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 118, 210));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(320, 80));

        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.WHITE);

        panel.add(Box.createHorizontalStrut(10));
        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(label);

        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });

        return panel;
    }

    // LIVE CLOCK THREAD
    private void startClockThread(JLabel clockLabel) {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();
                    java.time.format.DateTimeFormatter dtf =
                            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm");

                    SwingUtilities.invokeLater(() -> clockLabel.setText("Time: " + now.format(dtf)));
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
