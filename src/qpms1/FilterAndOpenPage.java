package qpms1;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class FilterAndOpenPage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public FilterAndOpenPage() {
        initializeUI();
        addComponents();
    }

    private void initializeUI() {
        setTitle("Filter and Open Question Papers");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void addComponents() {
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        searchField = new JTextField(20);
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchQuestionPapers();
            }
        });
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        // Create table to display results
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Add double-click listener to open PDF file
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String filePath;
                        try {
                            Blob blob = (Blob) table.getValueAt(row, 4);
                            filePath = saveBlobToFile(blob);
                        } catch (IOException | SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(FilterAndOpenPage.this, "Error opening PDF file.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        openPDF(filePath);
                    }
                }
            }
        });
    }

    private void searchQuestionPapers() {
        String searchText = searchField.getText().trim().toLowerCase();

        String query = "SELECT course_code, course_name, year, course_credits, question_paper FROM question_papers WHERE LOWER(course_code) LIKE ? OR LOWER(course_name) LIKE ? OR year LIKE ? OR course_credits LIKE ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/QPMS", "root", "aghar123");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String likeParameter = "%" + searchText + "%";
            preparedStatement.setString(1, likeParameter);
            preparedStatement.setString(2, likeParameter);
            preparedStatement.setString(3, likeParameter);
            preparedStatement.setString(4, likeParameter);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Populate table with search results
                populateTable(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while searching. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTable(ResultSet resultSet) throws SQLException {
        tableModel.setRowCount(0); // Clear previous data

        while (resultSet.next()) {
            String courseCode = resultSet.getString("course_code");
            String courseName = resultSet.getString("course_name");
            int year = resultSet.getInt("year");
            int credits = resultSet.getInt("course_credits");

            // Add row to table
            tableModel.addRow(new Object[] { courseCode, courseName, year, credits, resultSet.getBlob("question_paper") });
        }
    }

    // Method to save Blob data to a temporary file
    private String saveBlobToFile(Blob blob) throws IOException, SQLException {
        String tempDir = System.getProperty("java.io.tmpdir");
        File file = File.createTempFile("question_paper", ".pdf", new File(tempDir));
        try (FileOutputStream fos = new FileOutputStream(file);
             InputStream is = blob.getBinaryStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return file.getAbsolutePath();
    }

    // Method to open PDF file
    private void openPDF(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "PDF file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening PDF file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FilterAndOpenPage filterAndOpenPage = new FilterAndOpenPage();
            filterAndOpenPage.setVisible(true);
        });
    }
}
