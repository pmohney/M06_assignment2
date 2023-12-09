// Code corrected with JavaFX on 12/8/23
// Pierce Mohney
// SDEV200
// This program will connect to a MySQL server that contains three fields for a record.
// When connected the user can press a button to add a thousand random records and it returns with the server performance time,
// or the user user can select another button to see server performance time without records. 
// IMPORTANT run MySQL statements for database to work

package com.example.dataperformancecomparison;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabasePerformanceComparison extends Application {

    private TextField driverField, urlField, userField;
    private PasswordField passwordField;
    private Button connectButton;

    private Button insertButton;
    private Button runProgramButton;
//UI components for database connection
  
    private static final String DATABASE_URL = "jdbc:mysql://localhost/recordexercise";
    private static final String TABLE_NAME = "Temp";
    private static final int RECORD_COUNT = 1000;
//Sets database url and creates a value of 1000 for record count

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Database Performance");

        BorderPane root = new BorderPane();

        createDBConnectionPanel(root);

        connectButton.setOnAction(e -> connectToDatabase());

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//Creates window and button for database connection

    private void createDBConnectionPanel(BorderPane root) {
        FlowPane dbConnectionPanel = new FlowPane();
        dbConnectionPanel.setPadding(new Insets(10, 10, 10, 10));
        dbConnectionPanel.setVgap(8);
        dbConnectionPanel.setHgap(10);

        dbConnectionPanel.getChildren().add(new Label("JDBC Driver:"));
        driverField = new TextField("com.mysql.jdbc.Driver");
        dbConnectionPanel.getChildren().add(driverField);

        dbConnectionPanel.getChildren().add(new Label("Database URL:"));
        urlField = new TextField(DATABASE_URL);
        dbConnectionPanel.getChildren().add(urlField);

        dbConnectionPanel.getChildren().add(new Label("Username:"));
        userField = new TextField("scott");
        dbConnectionPanel.getChildren().add(userField);

        dbConnectionPanel.getChildren().add(new Label("Password:"));
        passwordField = new PasswordField();
        dbConnectionPanel.getChildren().add(passwordField);

        connectButton = new Button("Connect");
        dbConnectionPanel.getChildren().add(connectButton);

        root.setCenter(dbConnectionPanel);
    }
//Adds window buttons and labels

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlField.getText(), userField.getText(), passwordField.getText());
    }

    private void connectToDatabase() {
        try {
            Class.forName(driverField.getText());
            Connection connection = getConnection();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Connected");
            alert.setHeaderText(null);
            alert.setContentText("Connected to database");
            alert.showAndWait();
            connection.close();

            createInsertButtonPanel();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "Error connecting to database: " + e.getMessage());
        }
    }
//Shows connection status of database in window

    private void createInsertButtonPanel() {
        FlowPane insertButtonPanel = new FlowPane();
        insertButtonPanel.setPadding(new Insets(10, 10, 10, 10));
        insertButtonPanel.setHgap(10);

        insertButton = new Button("Batch Update");
        insertButton.setOnAction(e -> insertRecords());
        insertButtonPanel.getChildren().add(insertButton);

        runProgramButton = new Button("Non-Batch Update");
        runProgramButton.setOnAction(e -> runProgram());
        insertButtonPanel.getChildren().add(runProgramButton);

        ((BorderPane) connectButton.getParent()).setBottom(insertButtonPanel);
    }
//Adds record insert and database testing buttons into window
    private void insertRecords() {
        try {
            Class.forName(driverField.getText());
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + "(num1, num2, num3) VALUES (?, ?, ?)");

            for (int i = 0; i < RECORD_COUNT; i++) {
                preparedStatement.setDouble(1, Math.random());
                preparedStatement.setDouble(2, Math.random());
                preparedStatement.setDouble(3, Math.random());
                preparedStatement.executeUpdate();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Batch Update Completed");
            alert.setHeaderText(null);
            alert.setContentText(RECORD_COUNT + " records inserted successfully");
            alert.showAndWait();

            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Error", "Error during batch update: " + e.getMessage());
        }
    //Inserts records into database when batch update is selected, along with error handling
    }

    private void runProgram() {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Program Execution Time");
        alert.setHeaderText(null);
        alert.setContentText("Elapsed time is " + duration + " milliseconds");
        alert.showAndWait();
  //Shows execution time of database in window
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
