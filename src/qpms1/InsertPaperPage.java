package qpms1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class InsertPaperPage extends JFrame {
    private JLabel messageLabel;

    public InsertPaperPage() {
        initializeUI();
        addComponents();
    }

    private void initializeUI() {
        setTitle("Insert Question Paper");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField courseCodeField = new JTextField();
        JTextField courseNameField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField courseCreditsField = new JTextField();
        JLabel fileLabel = new JLabel("Upload PDF:");
        JLabel selectedFileLabel = new JLabel(); // Label to display selected file name

        JButton uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle PDF upload logic
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(InsertPaperPage.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Set the selected file name to the label
                    selectedFileLabel.setText(selectedFile.getName());
                    // Store the selected file in the label's client property
                    selectedFileLabel.putClientProperty("selectedFile", selectedFile);
                }
            }
        });

        inputPanel.add(new JLabel("Course Code:"));
        inputPanel.add(courseCodeField);
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(courseNameField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Course Credits:"));
        inputPanel.add(courseCreditsField);
        inputPanel.add(fileLabel);
        inputPanel.add(uploadButton);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from input fields
                String courseCode = courseCodeField.getText();
                String courseName = courseNameField.getText();
                int year = Integer.parseInt(yearField.getText());
                int courseCredits = Integer.parseInt(courseCreditsField.getText());

                // Get the selected PDF file from the label's client property
                File selectedFile = (File) selectedFileLabel.getClientProperty("selectedFile");

                if (selectedFile == null) {
                    showMessage("Please select a PDF file.", true);
                    return;
                }

                // Call method to insert into the database
                boolean inserted = insertIntoDatabase(courseCode, courseName, year, courseCredits, selectedFile);
                if (inserted) {
                    // Show success message
                    showMessage("Question paper inserted successfully", false);
                    Timer timer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose(); // Close the window after 2 seconds
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    // Show error message
                    showMessage("Failed to insert question paper. Please try again.", true);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(insertButton);

        // Initialize messageLabel
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(messageLabel, BorderLayout.NORTH); // Add messageLabel to the NORTH position
        add(selectedFileLabel, BorderLayout.WEST); // Add selectedFileLabel to the WEST position
    }

    // Method to insert data into the database
    private boolean insertIntoDatabase(String courseCode, String courseName, int year, int courseCredits, File pdfFile) {
        String JDBC_URL = "jdbc:mysql://localhost:3306/QPMS";
        String JDBC_USERNAME = "root";
        String JDBC_PASSWORD = "aghar123"; // Change to your actual password

        String query = "INSERT INTO question_papers (course_code, course_name, year, course_credits, question_paper) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, courseCode);
            preparedStatement.setString(2, courseName);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, courseCredits);
            if (pdfFile != null && pdfFile.exists()) {
                byte[] pdfBytes = readPDFFile(pdfFile);
                if (pdfBytes != null) {
                    preparedStatement.setBytes(5, pdfBytes);
                } else {
                    preparedStatement.setNull(5, Types.BLOB);
                }
            } else {
                preparedStatement.setNull(5, Types.BLOB);
            }
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to read PDF file as byte array
    private byte[] readPDFFile(File pdfFile) {
        try (FileInputStream fis = new FileInputStream(pdfFile)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to show message on the message label
    private void showMessage(String message, boolean isError) {
        if (isError) {
            messageLabel.setForeground(Color.RED);
        } else {
            messageLabel.setForeground(Color.GREEN.darker());
        }
        messageLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InsertPaperPage insertPaperPage = new InsertPaperPage();
            insertPaperPage.setVisible(true);
        });
    }
}
