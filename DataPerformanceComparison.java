// 12/1/23
// Pierce Mohney
// SDEV200
// This program will connect to a MySQL server that contains three fields for a record.
// When connected the user can press a button to add a thousand random records and it returns with the server performance time,
// or the user user can select another button to see server performance time without records. 
// IMPORTANT run MySQL statements for database to work

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabasePerformanceComparison extends JFrame {

    private JPanel dbConnectionPanel;
    private JTextField driverField, urlField, userField;
    private JPasswordField passwordField;
    private JButton connectButton;

    private JPanel insertButtonPanel;
    private JButton insertButton;
    private JButton runProgramButton;
//Creates buttons for running program 

    private static final String DATABASE_URL = "jdbc:mysql://localhost/recordexercise";
    private static final String TABLE_NAME = "Temp";
    private static final int RECORD_COUNT = 1000;

    public DatabasePerformanceComparison() {
        setTitle("Database Performance");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createDBConnectionPanel();
        add(dbConnectionPanel, BorderLayout.CENTER);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToDatabase();
            }
        });
    }
//Creates window frame for database connection

    private void createDBConnectionPanel() {
        dbConnectionPanel = new JPanel();
        dbConnectionPanel.setLayout(new GridLayout(5, 2));

        dbConnectionPanel.add(new JLabel("JDBC Driver:"));
        driverField = new JTextField("com.mysql.jdbc.Driver");
        dbConnectionPanel.add(driverField);

        dbConnectionPanel.add(new JLabel("Database URL:"));
        urlField = new JTextField(DATABASE_URL);
        dbConnectionPanel.add(urlField);

        dbConnectionPanel.add(new JLabel("Username:"));
        userField = new JTextField("scott");
        dbConnectionPanel.add(userField);

        dbConnectionPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField("tiger");
        dbConnectionPanel.add(passwordField);

        connectButton = new JButton("Connect");
        dbConnectionPanel.add(connectButton);
    }
//First panel methods and elements

    private void createInsertButtonPanel() {
        insertButtonPanel = new JPanel();
        insertButtonPanel.setLayout(new FlowLayout());
        insertButton = new JButton("Batch Update");
        insertButtonPanel.add(insertButton);
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertRecords();
            }
        });

        runProgramButton = new JButton("Non-Batch Update");
        insertButtonPanel.add(runProgramButton);
        runProgramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runProgram();
            }
        });
//Second panel elements
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlField.getText(), userField.getText(), String.valueOf(passwordField.getPassword()));
    }

    private void connectToDatabase() {
        try {
            Class.forName(driverField.getText());
            Connection connection = getConnection();
            JOptionPane.showMessageDialog(this, "Connected to the database", "Connected", JOptionPane.INFORMATION_MESSAGE);
            connection.close();

            createInsertButtonPanel();
            add(insertButtonPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
//Methods for connecting to database, and to return data from batch update process
    }

    private void insertRecords() {
        try {
            Class.forName(driverField.getText());
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + "(num1, num2, num3) VALUES (?, ?, ?)");

            for (int i = 0; i < RECORD_COUNT; i++) {
                preparedStatement.setDouble(1, Math.random());
                preparedStatement.setDouble(2, Math.random());
                preparedStatement.setDouble(3, Math.random());
                preparedStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, RECORD_COUNT + "Batch update completed", JOptionPane.INFORMATION_MESSAGE);

            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }
    //Method for running batch update process with error message

    private void runProgram() {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        JOptionPane.showMessageDialog(this, "Elapsed time is " + duration, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DatabasePerformanceComparison().setVisible(true);
            }
        });
//Tests database without batch update process
    }
}
