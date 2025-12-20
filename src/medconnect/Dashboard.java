package medconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

public class Dashboard extends JFrame {

    private String username;
    private String role;

    // No-arg constructor
    public Dashboard() {
        this.username = "Guest";
        this.role = "guest";
        initDashboardUI();
    }

    // Constructor after login
    public Dashboard(String username, String role) {
        this.username = username;
        this.role = role;
        initDashboardUI();
    }

    private void initDashboardUI() {

        // FULL SCREEN
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("MedConnect - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ================= LEFT PANEL ================= */
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(0, getHeight())); // for animation
        leftPanel.setBackground(new Color(25, 118, 210));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("MedConnect");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 34));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        leftPanel.add(title);

        JLabel userLabel = new JLabel("Hello, " + username + " (" + role + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        leftPanel.add(userLabel);

        // Role-based menus
        if (!role.equalsIgnoreCase("patient")) {
            leftPanel.add(createMenuItem("Invoice", "E:/project/MedConnect/src/icon/invoice.png", () -> new InvoicePage()));
            leftPanel.add(createMenuItem("Prescription", "E:/project/MedConnect/src/icon/prescription.jpg", () -> new PrescriptionPage()));
            leftPanel.add(createMenuItem("Medicine", "E:/project/MedConnect/src/icon/medicine.jpg", () -> new MedicinePage()));
        }

        if (role.equalsIgnoreCase("admin")) {
            leftPanel.add(createMenuItem("Doctor", "E:/project/MedConnect/src/icon/dr.jpg", () -> new DoctorPage()));
            leftPanel.add(createMenuItem("Patient", "E:/project/MedConnect/src/icon/patient.jpg", () -> new PatientPage()));
            leftPanel.add(createMenuItem("Ambulance", "E:/project/MedConnect/src/icon/amb.jpg", () -> new AmbulancePage()));
            leftPanel.add(createMenuItem("Room", "E:/project/MedConnect/src/icon/roomm.jpg", () -> new RoomPage()));
        }

        if (role.equalsIgnoreCase("doctor") || role.equalsIgnoreCase("admin")) {
            leftPanel.add(createMenuItem("EHR", "E:/project/MedConnect/src/icon/ehr.png", () -> new EHRPage()));
        }

        leftPanel.add(createMenuItem("Appointment", "E:/project/MedConnect/src/icon/appointment.png", () -> new AppointmentPage()));

        // CLOCK
        JLabel clockLabel = new JLabel("Time: --:--");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 18));
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(clockLabel);
        startClockThread(clockLabel);

        add(leftPanel, BorderLayout.WEST);
        animateSidePanel(leftPanel);

        /* ================= RIGHT PANEL ================= */
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome to MedConnect Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 55));
        welcome.setForeground(new Color(33, 33, 33));
        welcome.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(welcome, BorderLayout.NORTH);

        ImageIcon dashboardImg = new ImageIcon("E:/project/MedConnect/src/icon/login.jpg");
        Image scaledImg = dashboardImg.getImage().getScaledInstance(420, 420, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtle hover border (safe)
        imgLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                imgLabel.setBorder(BorderFactory.createLineBorder(new Color(25,118,210), 4));
            }
            public void mouseExited(MouseEvent e) {
                imgLabel.setBorder(null);
            }
        });

        centerPanel.add(imgLabel, BorderLayout.CENTER);
        rightPanel.add(centerPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /* ================= MENU ITEM ================= */
    private JPanel createMenuItem(String text, String iconPath, Runnable action) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(320, 80));
        panel.setBackground(new Color(25, 118, 210));

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

            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(30, 136, 229));
                panel.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE));
            }

            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(25, 118, 210));
                panel.setBorder(null);
            }

            public void mousePressed(MouseEvent e) {
                panel.setBackground(new Color(21, 101, 192));
            }

            public void mouseReleased(MouseEvent e) {
                action.run();
            }
        });

        return panel;
    }

    /* ================= SIDEBAR ANIMATION ================= */
    private void animateSidePanel(JPanel panel) {
        Timer timer = new Timer(5, null);
        timer.addActionListener(e -> {
            int w = panel.getPreferredSize().width;
            if (w < 370) {
                panel.setPreferredSize(new Dimension(w + 10, panel.getHeight()));
                panel.revalidate();
            } else {
                panel.setPreferredSize(new Dimension(370, panel.getHeight()));
                timer.stop();
            }
        });
        timer.start();
    }

    /* ================= CLOCK ================= */
    private void startClockThread(JLabel clockLabel) {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();
                    java.time.format.DateTimeFormatter dtf =
                            java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm");
                    SwingUtilities.invokeLater(() ->
                            clockLabel.setText("Time: " + now.format(dtf)));
                    Thread.sleep(1000);
                } catch (Exception ignored) {}
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
