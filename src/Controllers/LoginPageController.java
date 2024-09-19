/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Modules.Evenement;
import Modules.Ticket;
import Modules.User;
import Modules.Visitor;
import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class LoginPageController implements Initializable {

    @FXML
    private TextField UserName;
    
    @FXML
    private TextField UserPassword;
    
    @FXML
    private Button LoginButton;
    
    //private Evenement CurrentEvent;
    private User CurrentUser;
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    
    
    
    public void setUser(User user) {
        this.CurrentUser = user;

        // Now you can perform actions that depend on CurrentUser and CurrentEvent
        if (CurrentUser != null) {
            System.out.println("Logged-in User: " + CurrentUser.getUsername());
        }

        
        
        System.out.println("we are in login !!!!");
        
    }
    
    private void showAlert(String title,String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public User authenticateUser(String username, String password) {
        // Assuming you have a connection to the database (connection object)
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Query to check username and password
            String selectUserSql = "SELECT * FROM users WHERE Username = ? AND Password = ?";
            PreparedStatement selectUserStatement = connection.prepareStatement(selectUserSql);
            selectUserStatement.setString(1, username);
            selectUserStatement.setString(2, password);

            // Execute the SQL statement
            ResultSet userResultSet = selectUserStatement.executeQuery();

            // Check if a user with the provided username and password exists
            if (userResultSet.next()) {
                // User authentication successful, create a User object
                int userId = userResultSet.getInt("UserID");
                String userEmail = userResultSet.getString("Email");
                boolean isAdmin = userResultSet.getInt("isAdmin") == 1;
                String tel = userResultSet.getString("Tel");
                

                User loggedInUser = new Visitor(userId, username, password,isAdmin, userEmail,tel );

                // Close the result set, statement, and connection
                userResultSet.close();
                selectUserStatement.close();
                connection.close();

                return loggedInUser;
            }

            // Close the result set, statement, and connection
            userResultSet.close();
            selectUserStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return null; // Return null if authentication fails
    }
    
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = UserName.getText();
        String password = UserPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Warning", "Empty Fields", "Please enter both username and password!");
            return;
        }
        User loggedInUser = authenticateUser(username, password);

        if (loggedInUser != null) {
            // Login successful
            setUser(loggedInUser);

            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/EventClient.fxml"));
                Parent root = loader.load();
                EventClientController nextController = loader.getController();
                nextController.setUser(loggedInUser);

                // Show the new scene
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }

        } else {
            showAlert("Error", "Login Failed", "Incorrect username or password!");
        }
    }
}
