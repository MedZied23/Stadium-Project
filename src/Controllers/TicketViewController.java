/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.Concert;
import Modules.Evenement;
import Modules.Match;
import Modules.Ticket;
import Modules.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class TicketViewController implements Initializable {

    @FXML
    private ImageView MenuStade;
    
    @FXML
    private ImageView MenuEvents;
    
    @FXML
    private ImageView MenuTickets;
    
    @FXML
    private ImageView MenuShop;
    
    @FXML
    private ImageView NextArrow;
    
    @FXML
    private ImageView PreviousArrow;
    
    @FXML
    private Label concertTitle;
    
    @FXML
    private Label seatNumber;
    
    @FXML
    private Label concertDate;
    
    @FXML
    private Label concertTime;
    
    @FXML
    private Label ticketPrice;
    
    @FXML
    private Label ticketNumber;
    
    
    private Evenement CurrentEvent;
    private User CurrentUser;
    private List<Ticket> userTickets = new ArrayList<>();
    private List<Integer> userSiegeIds = new ArrayList<>();
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    public void setUser(User user) {this.CurrentUser = user;}
    public void setEvenement(Evenement evenement) {this.CurrentEvent = evenement;}
    
    public void setUserData(User user, Evenement evenement) {
        this.CurrentUser = user;
        this.CurrentEvent = evenement;

        if (CurrentUser != null) {
            System.out.println("Logged-in User: " + CurrentUser.getUsername());
        }

        if (CurrentEvent != null) {
            System.out.println("Selected Event: " + CurrentEvent.getEventName());
        } else {
            System.out.println("No event selected!!!");
        }
        loadUserSiegeIdsFromDatabase();
        loadTicketsFromDatabase();
        displayTicketInformation(userTickets.get(1));
        System.out.println("we are in ticket viewer !!!!");
    }
    
    
    private void loadTicketsFromDatabase() {
        userTickets.clear();  // Clear existing tickets before loading

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String selectTicketsSql = "SELECT * FROM tickets WHERE TicEventID = ?";
            PreparedStatement selectTicketsStatement = connection.prepareStatement(selectTicketsSql);
            selectTicketsStatement.setInt(1, CurrentEvent.getEventId());

            // Execute the SQL statement
            ResultSet ticketsResultSet = selectTicketsStatement.executeQuery();

            // Iterate through the result set and add tickets to the list
            while (ticketsResultSet.next()) {
                int ticketID = ticketsResultSet.getInt("TicketID");
                int eventID = ticketsResultSet.getInt("TicEventID");
                double price = ticketsResultSet.getDouble("TicPrice");
                int siegeID = ticketsResultSet.getInt("TicSiegeID");

                // Check if the ticket's siegeID is in userSiegeIds
                if (userSiegeIds.contains(siegeID)) {
                    LocalDate dateConcert = ticketsResultSet.getDate("TicDate").toLocalDate();
                    LocalTime timeConcert = LocalTime.parse(ticketsResultSet.getString("TicTime"));

                    Ticket ticket = new Ticket(ticketID, eventID, price, siegeID, dateConcert, timeConcert);
                    userTickets.add(ticket);
                }
            }

            // Close the result set, statement, and connection
            ticketsResultSet.close();
            selectTicketsStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    private void loadUserSiegeIdsFromDatabase() {
        userSiegeIds.clear();  // Clear existing data

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Assuming there's a column named "ResUserID" for user ID in the reservations table
            String selectReservationsSql = "SELECT ResSiegeID FROM reservations WHERE ResUserID = ? and ResEventID =?" ;
            PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsSql);
            selectReservationsStatement.setInt(1, CurrentUser.getUserID());
            selectReservationsStatement.setInt(2, CurrentEvent.getEventId());

            // Execute the SQL statement
            ResultSet reservationsResultSet = selectReservationsStatement.executeQuery();

            // Iterate through the result set and add siege IDs to the list
            while (reservationsResultSet.next()) {
                int siegeId = reservationsResultSet.getInt("ResSiegeID");
                userSiegeIds.add(siegeId);
            }

            // Close the result set, statement, and connection
            reservationsResultSet.close();
            selectReservationsStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    private int currentTicketIndex = 0; // Track the index of the currently displayed ticket

    @FXML
    private void handleNextButton(MouseEvent event) {
        if (!userTickets.isEmpty()) {
            currentTicketIndex = (currentTicketIndex + 1) % userTickets.size();
            displayTicketInformation(userTickets.get(currentTicketIndex));
        }
        System.out.println("Next "+currentTicketIndex);
    }

    @FXML
    private void handlePreviousButton(MouseEvent event) {
        if (!userTickets.isEmpty()) {
            currentTicketIndex = (currentTicketIndex - 1 + userTickets.size()) % userTickets.size();
            displayTicketInformation(userTickets.get(currentTicketIndex));
        }
        System.out.println("previous "+currentTicketIndex);
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

    private void displayTicketInformation(Ticket ticket) {
        concertTitle.setText(getEventTitle(CurrentEvent));
        seatNumber.setText(String.valueOf(ticket.getSeatNumber()));
        concertDate.setText(getFormattedDate(ticket.getDate()));
        concertTime.setText(getFormattedTime(ticket.getTime()));
        ticketPrice.setText("$ " + String.format("%.2f", ticket.getPrice()));
        ticketNumber.setText(String.valueOf(ticket.getTicketID()));
    }
    
    public String getEventTitle(Evenement event) {
        if (event.isConcert()) {
            return ((Concert) event).getEventName();
        } else if (event.isMatch()) {
            Match matchEvent = (Match) event;
            return matchEvent.getTeam1() + " vs " + matchEvent.getTeam2();
        } else {
            return "Unknown Event";
        }
    }
    
    public String getFormattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy");
        return date.format(formatter);
    }

    public String getFormattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h a");
        return time.format(formatter);
    }
}
