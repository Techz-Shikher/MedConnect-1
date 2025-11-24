package medconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Login extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnReset;

    public Login() {
        setTitle("MedConnect - Login Portal");
        setSize(700, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ===================
        // IMAGE (LEFT SIDE)
        // ===================
        ImageIcon icon = new ImageIcon("E:\\project\\MedConnect\\src\\icon\\login.jpg");
        Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel imgLabel = new JLabel(new ImageIcon(scaled));
        imgLabel.setBounds(10, 20, 250, 250);
        add(imgLabel);

        // ===================
        // USERNAME
        // ===================
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setBounds(300, 50, 120, 25);
        lblUser.setFont(new Font("Arial", Font.BOLD, 16));
        lblUser.setForeground(Color.BLACK);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(300, 75, 300, 30);
        add(txtUser);

        // ===================
        // PASSWORD
        // ===================
        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setBounds(300, 120, 120, 25);
        lblPass.setFont(new Font("Arial", Font.BOLD, 16));
        lblPass.setForeground(Color.BLACK);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(300, 145, 300, 30);
        add(txtPass);

        // ===================
        // LOGIN BUTTON
        // ===================
        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(300, 200, 100, 35);
        btnLogin.addActionListener(this::loginUser);
        add(btnLogin);

        // ===================
        // RESET BUTTON
        // ===================
        btnReset = new JButton("RESET");
        btnReset.setBounds(450, 200, 100, 35);
        btnReset.addActionListener(e -> {
            txtUser.setText("");
            txtPass.setText("");
        });
        add(btnReset);

        setVisible(true);
    }

    // ===========================
    // AUTHENTICATION FUNCTION
    // ===========================
    private void loginUser(ActionEvent event) {
        String username = txtUser.getText().trim();
        String password = String.valueOf(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hospital_management_system",
                    "root",
                    "123456789"
            );

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");

                JOptionPane.showMessageDialog(this,
                        "Login Successful!\nWelcome " + name);

                // Open dashboard
                new Dashboard();   // ✔ correct

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
