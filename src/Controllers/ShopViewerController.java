/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.Evenement;
import Modules.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class ShopViewerController implements Initializable {
    
    @FXML
    private ImageView MenuStade;
    
    @FXML
    private ImageView MenuEvents;
    
    @FXML
    private ImageView MenuTickets;
    
    @FXML
    private ImageView MenuShop;

    private Evenement CurrentEvent;
    private User CurrentUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    public void setUserData(User user, Evenement evenement) {
        this.CurrentUser = user;
        this.CurrentEvent = evenement;

        // Now you can perform actions that depend on CurrentUser and CurrentEvent
        if (CurrentUser != null) {
            System.out.println("Logged-in User: " + CurrentUser.getUsername());
        }

        if (CurrentEvent != null) {
            System.out.println("Selected Event: " + CurrentEvent.getEventName());
        } else {
            System.out.println("No event selected!!!");
        }
      
        System.out.println("we are in shop viewer !!!!");
    }
    
    @FXML
    private void handleEvent(MouseEvent event) {
        if (CurrentUser != null) {
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/EventClient.fxml"));
                Parent root = loader.load();
                EventClientController nextController = loader.getController();
                nextController.setUser(CurrentUser);

                // Show the new scene
                Stage stage = (Stage) MenuEvents.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
    
    @FXML
    private void handleTickets(MouseEvent event) {
        if (CurrentUser != null) {
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/TicketView.fxml"));
                Parent root = loader.load();
                TicketViewController nextController = loader.getController();
                nextController.setUserData(CurrentUser,CurrentEvent);

                // Show the new scene
                Stage stage = (Stage) MenuTickets.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
    
    @FXML
    private void handleStade(MouseEvent event) {
        if (CurrentUser != null) {
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/StadeViewer.fxml"));
                Parent root = loader.load();
                StadeViewerController nextController = loader.getController();
                nextController.setUserData(CurrentUser, CurrentEvent);

                // Show the new scene
                Stage stage = (Stage) MenuStade.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
    
    @FXML
    private void handleShop(MouseEvent event) {
        if (CurrentUser != null) {
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/ShopViewer.fxml"));
                Parent root = loader.load();
                ShopViewerController nextController = loader.getController();
                nextController.setUserData(CurrentUser, CurrentEvent);

                // Show the new scene
                Stage stage = (Stage) MenuShop.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }
}
