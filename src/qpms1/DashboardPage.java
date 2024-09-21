package qpms1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardPage extends JFrame {
    private JButton viewQuestionPapersButton;
    private JButton insertQuestionPapersButton;
    private JButton updateQuestionPapersButton;
    private JButton deleteQuestionPapersButton;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel messageLabel;
    private final String userType;

    public DashboardPage(String userType) {
        this.userType = userType;
        initializeUI();
        addComponents();
    }

    private void initializeUI() {
        setTitle("Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // Set background color

        // Set custom font for all components
        Font customFont = new Font("Arial", Font.BOLD, 16);
        UIManager.put("Button.font", customFont);
        UIManager.put("Label.font", customFont);

        // Create and set a custom color scheme
        Color customColor = new Color(0, 102, 204); // Dark blue
        UIManager.put("Button.background", customColor);
        UIManager.put("Button.foreground", Color.WHITE);
    }

    private void addComponents() {
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Increase the grid layout size for the new button
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        viewQuestionPapersButton = new JButton("View Question Papers");
        viewQuestionPapersButton.setForeground(Color.WHITE);
        viewQuestionPapersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewQuestionPapers();
            }
        });
        buttonPanel.add(viewQuestionPapersButton);

        ImageIcon plusIcon = new ImageIcon("plus_icon.png"); // Change "plus_icon.png" to the actual path of your plus icon image
        insertQuestionPapersButton = new JButton("Insert Question Paper", plusIcon);
        insertQuestionPapersButton.setForeground(Color.WHITE);
        insertQuestionPapersButton.setHorizontalTextPosition(SwingConstants.LEADING);
        insertQuestionPapersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InsertPaperPage insertPaperPage = new InsertPaperPage();
                insertPaperPage.setVisible(true);
            }
        });
        buttonPanel.add(insertQuestionPapersButton);

        updateQuestionPapersButton = new JButton("Update Question Paper");
        updateQuestionPapersButton.setForeground(Color.WHITE);
        updateQuestionPapersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open update page
            }
        });
        buttonPanel.add(updateQuestionPapersButton);

        deleteQuestionPapersButton = new JButton("Delete Question Paper");
        deleteQuestionPapersButton.setForeground(Color.WHITE);
        deleteQuestionPapersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open delete dialog
            }
        });
        buttonPanel.add(deleteQuestionPapersButton);

        searchField = new JTextField(20);
        buttonPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFilterAndOpenPage();
            }
        });
        buttonPanel.add(searchButton);

        if (!userType.equals("admin")) {
            // Disable buttons for non-admin users
            insertQuestionPapersButton.setEnabled(false);
            updateQuestionPapersButton.setEnabled(false);
            deleteQuestionPapersButton.setEnabled(false);
        }

        // Add button panel to frame
        add(buttonPanel, BorderLayout.CENTER);

        // Create and add message label
        messageLabel = new JLabel(" ", JLabel.CENTER);
        messageLabel.setForeground(Color.RED); // Set text color
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20)); // Add padding
        add(messageLabel, BorderLayout.SOUTH);
    }

    private void viewQuestionPapers() {
        // Implement view logic
    }

    private void openFilterAndOpenPage() {
        FilterAndOpenPage filterAndOpenPage = new FilterAndOpenPage();
        filterAndOpenPage.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String userType = "admin"; // Set the user type here
            DashboardPage dashboardPage = new DashboardPage(userType);
            dashboardPage.setVisible(true);
        });
    }
}
