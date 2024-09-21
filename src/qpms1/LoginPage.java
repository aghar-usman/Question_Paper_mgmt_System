package qpms1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

// LoginPage class
public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginPage() {
        initializeUI();
        addComponents();
        setupLoginAction();
        activateLoginOnEnter();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(600, 400); // Adjusted size for 1920x1080 resolution
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Increased spacing between components
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // Increased spacing between components
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font size
        buttonPanel.add(loginButton);

        messageLabel = new JLabel(" ", JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font size

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.NORTH);
    }

    private void setupLoginAction() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void activateLoginOnEnter() {
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        };

        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (DatabaseManager.authenticate(username, password)) {
                messageLabel.setText("Login successful!");
                openUserDashboard(username);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace(); // Print the stack trace for debugging
            messageLabel.setText("Error: Failed to authenticate. Please try again later.");
        }
    }

    private void openUserDashboard(String username) {
        String userType = getUserType(username);
        try {
            DashboardPage dashboardPage = new DashboardPage(userType);
            dashboardPage.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: Failed to open dashboard.");
        }
    }

    private String getUserType(String username) {
        return username.equalsIgnoreCase("admin") ? "admin" : "students";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}
