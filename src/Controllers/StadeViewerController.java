/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.Concert;
import Modules.Evenement;
import Modules.Match;
import Modules.Reservation;
import Modules.Section;
import Modules.Siege;
import Modules.Ticket;
import Modules.User;
import Modules.Visitor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import static javafx.scene.shape.StrokeType.INSIDE;
import static javafx.scene.shape.StrokeType.OUTSIDE;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static javafx.scene.text.TextAlignment.CENTER;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class StadeViewerController implements Initializable {
    
    @FXML
    private ScrollPane StadePane;
    
    @FXML
    private Pane StadeLayout;
    
    @FXML
    private Label Selected_Sec_Sig;
    
    @FXML
    private Label Selected_Sec_Sig_price;
    
    @FXML
    private Label SiegeState;
    
    @FXML
    private Button BuyNowButton;
    
    @FXML
    private Button resaleButton;
    
    @FXML
    private Button switchButton;
    
    @FXML
    private ImageView MenuStade;
    
    @FXML
    private ImageView MenuEvents;
    
    @FXML
    private ImageView MenuTickets;
    
    @FXML
    private ImageView MenuShop;
    
    
    
    
    @FXML
    private Label SiegeTitle;
    
    @FXML
    private Label PriceTitle;
    
    @FXML
    private Label YourSiege1;
    
    @FXML
    private Label YourSiege2;
    
    @FXML
    private Label YourSiege3;
    
    @FXML
    private Label YourSiege4;
    
    @FXML
    private Label YourSiege5;
    
    @FXML
    private Label YourSiege6;
    
    @FXML
    private Label YourSiege7;
    
    @FXML
    private Label YourPrice1;
    
    @FXML
    private Label YourPrice2;
    
    @FXML
    private Label YourPrice3;
    
    @FXML
    private Label YourPrice4;
    
    @FXML
    private Label YourPrice5;
    
    @FXML
    private Label YourPrice6;
    
    @FXML
    private Label YourPrice7;
    
    @FXML
    private CheckBox check1;
    
    @FXML
    private CheckBox check2;
    
    @FXML
    private CheckBox check3;
    
    @FXML
    private CheckBox check4;
    
    @FXML
    private CheckBox check5;
    
    @FXML
    private CheckBox check6;
    
    @FXML
    private CheckBox check7;
    
    @FXML
    private GridPane MySpaceGrid;
    
    
    
    
    
    private Evenement CurrentEvent;
    private User CurrentUser;
    
    private static final String SELECT_ALL_SECTIONS = "SELECT * FROM stadeprojet.sections";
    
    
    private List<Section> sections = new ArrayList<>();
    private List<Group> sectionGroups = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private List<Ticket> userTickets = new ArrayList<>();
    private Map<Integer, Double> resaleOffers = new HashMap<>();
    private List<Integer> switchRequests = new ArrayList<>();
    private List<Integer> userSiegeIds = new ArrayList<>();
    
    private Siege  selectedSiege = null ;

    
    
    // Section colors and borders
    private final Color SEC_COLOR = Color.web("#e6e6fa"); // Lavender
    private final Color SEL_SEC_COLOR = Color.PURPLE;
    private final Color SEC_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SEC_BORDER_WIDTH = 0.5; // Replace 2.0 with your desired width
    private final StrokeType SEC_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed

    // Siege colors and borders
    private final Color SIG_COLOR = Color.WHITE;
    private final Color SIG_BORDER_COLOR = Color.web("#a09db2");
    private final Color SIG_SEL_BORDER_COLOR = Color.web("#ffbf00");// Slate Gray
    private final double SIG_BORDER_WIDTH = 0.2; // Replace 2.0 with your desired width
    private final StrokeType SIG_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed
    
     private final Color SIG_COLOR_YOURS = Color.web("#8BC34A");
    private final Color SIG_BORDER_COLOR_YOURS = Color.web("#4CAF50");
    
    private final Color SIG_COLOR_RES = Color.web("#548dd4");
    private final Color SIG_BORDER_COLOR_RES = Color.web("#365f91");
    
    private final Color SIG_COLOR_SWI = Color.web("#f79646");
    private final Color SIG_BORDER_COLOR_SWI = Color.web("#cc6600");
    
    private final Color SIG_COLOR_SAL = Color.web("#ff0000"); 
    private final Color SIG_BORDER_COLOR_SAL = Color.web("#cc0000");
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
       
        StadeLayout.setPrefWidth(1336);
        StadeLayout.setPrefHeight(738);
        
        
        
        
    }  
    
    public void setUser(User user) {this.CurrentUser = user;}
    public void setEvenement(Evenement evenement) {this.CurrentEvent = evenement;}
    
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
        
        System.out.println("we are in stade viewer !!!!");
        
        loadSectionsFromDatabase();
        loadUserSiegeIdsFromDatabase();
        loadReservationsFromDatabase();
        loadResaleOffersFromDatabase();
        loadSwitchOffersFromDatabase();
        loadTicketsFromDatabase();
        //Platform.runLater(this::updateMySpaceGrid);
        updateSiegeStatus();
        updateMySpaceGrid();
        
    }
    
    private void loadSectionsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SECTIONS)) {
            
            // Set radius and scale for the entire Sieges table
            setRadiusAndScale();

            while (resultSet.next()) {
                int id = resultSet.getInt("sectionId");
                String sectionName = resultSet.getString("sectionName");
                int shapeIndex = resultSet.getInt("shapeIndex");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double rotation = resultSet.getDouble("rotation");
                double scale = resultSet.getDouble("scale");

                Section section = new Section(id, sectionName, shapeIndex, x, y, rotation, scale);
                // Load sieges associated with the section
                // Create a new section group
                Group sectionGroup = new Group();
                //section.getSectionShape().setOnMouseClicked(event -> selectSection(section));
                applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
                sections.add(section);
                sectionGroups.add(sectionGroup);

                // Update the transformations
                // Scale the section shape
                section.getSectionShape().setScaleX(section.getScale());
                section.getSectionShape().setScaleY(section.getScale());

                // Ajoute la forme de section




                sectionGroup.getChildren().add(section.getSectionShape());

                // Applique les transformations de rotation et de translation
                sectionGroup.getTransforms().add(new Rotate(section.getRotation(), section.getX(), section.getY()));
                //sectionGroup.getTransforms().add(new Rotate(section.getRotation()));
                sectionGroup.getTransforms().add(new Translate(section.getX(), section.getY()));


                loadAndAddSiegesForSection(connection,section,sectionGroup);


                for (Siege siege : section.getSieges()) {
                    System.out.println("Siege X: " + siege.getCenterX() + ", Siege Y: " + siege.getCenterY());
                    //siege.getTransforms().add(new Translate(-section.getX(), -section.getY()));
                    sectionGroup.getChildren().add(siege);
                }

                StadeLayout.getChildren().add(sectionGroup); 

                }


            } catch (SQLException e) {
                e.printStackTrace(); // 
            }
        }
    
    private void loadAndAddSiegesForSection(Connection connection, Section section,Group sectionGroup) {
        int sectionId = section.getSectionId();
        String sql = "SELECT * FROM Sieges WHERE sectionId = ?";
        

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, sectionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int siegeId = resultSet.getInt("siegeId");
                    int numSiege = resultSet.getInt("numSiege");
                    double x = resultSet.getDouble("x");
                    double y = resultSet.getDouble("y");

                    Siege siege = new Siege(siegeId, numSiege, sectionId, "AVAILABLE", x, y);
                    //siege.setFill(SEL_SIEGE_COLOR);
                    applyColorAndBorder(siege, SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
                    siege.setOnMouseClicked(event -> selectSiege(siege));
                    section.addSiege(siege);                 
                }
                
                // Add the sieges to the section group
                
            }
        } catch (SQLException e) {
            e.printStackTrace(); //
        }
    }
    
    private void loadTicketsFromDatabase() {
        userTickets.clear();  // Clear existing tickets before loading

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String selectTicketsSql  = "SELECT * FROM tickets WHERE TicEventID = ?";
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
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    public void loadReservationsFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for retrieving reservations from the database
            String selectReservationsSql = "SELECT * FROM reservations where ResEventID = ?";
            PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsSql);
            selectReservationsStatement.setInt(1, CurrentEvent.getEventId());

            // Execute the SQL statement
            ResultSet reservationsResultSet = selectReservationsStatement.executeQuery();

            // Clear existing reservations
            reservations.clear();

            // Iterate through the result set and create Reservation objects
            while (reservationsResultSet.next()) {
                int eventId = reservationsResultSet.getInt("ResEventID");
                int userId = reservationsResultSet.getInt("ResUserID");
                int sectionId = reservationsResultSet.getInt("ResSectionID");
                int siegeId = reservationsResultSet.getInt("ResSiegeID");
                double price = reservationsResultSet.getDouble("Price");

                // Create Reservation object and add it to the reservations list
                Reservation reservation = new Reservation(eventId, userId, sectionId, siegeId, price);
                reservations.add(reservation);
            }

            // Close the result set, statement, and connection
            reservationsResultSet.close();
            selectReservationsStatement.close();
            connection.close();

            // Display the loaded reservations
            // (You can add code here to update your user interface with the reservations)
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    private void loadUserSiegeIdsFromDatabase() {
        userSiegeIds.clear();  // Clear existing data

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Assuming there's a column named "ResUserID" for user ID in the reservations table
            String selectReservationsSql = "SELECT ResSiegeID FROM reservations WHERE ResUserID = ?";
            PreparedStatement selectReservationsStatement = connection.prepareStatement(selectReservationsSql);
            selectReservationsStatement.setInt(1, CurrentUser.getUserID());

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
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    public void addResaleOffer(int siegeId, int sectionId, double newPrice) {
        resaleOffers.put(siegeId, newPrice);
    }

    public void addSwitchRequest(int siegeId, int sectionId) {
        switchRequests.add(siegeId);
    }
    
    // Method to update siege statuses
    private void updateSiegeStatus() {
        System.out.println("updating the status");
        for (Section section : sections) {
            for (Siege siege : section.getSieges()) {
                // Check if the siege is not in reservations
                if (reservations.stream().noneMatch(reservation -> reservation.siegeID() == siege.getSiegeId())) {
                    siege.setStatus("AVAILABLE");
                    applyColorAndBorder(siege, SIG_COLOR, SIG_BORDER_COLOR,SIG_BORDER_WIDTH,SIG_BORDER_TYPE);
                } else {
                    // Check if the siege is in resale
                    if (resaleOffers.containsKey(siege.getSiegeId())) {
                        siege.setStatus("RESALE");
                        System.out.println("siege for sale");
                        applyColorAndBorder(siege, SIG_COLOR_SAL, SIG_BORDER_COLOR_SAL,SIG_BORDER_WIDTH,SIG_BORDER_TYPE);
                    } else if (switchRequests.contains(siege.getSiegeId())) {
                        // Check if the siege is in switch
                        siege.setStatus("SWITCH");
                        System.out.println("siege for switch");
                        applyColorAndBorder(siege, SIG_COLOR_SWI, SIG_BORDER_COLOR_SWI,SIG_BORDER_WIDTH,SIG_BORDER_TYPE);
                    } else if (userSiegeIds.contains(siege.getSiegeId())) {
                        // Check if the siege is not in the current user's reservations
                        siege.setStatus("YOURS");
                        System.out.println("siege for switch");
                        applyColorAndBorder(siege, SIG_COLOR_YOURS, SIG_BORDER_COLOR_YOURS,SIG_BORDER_WIDTH,SIG_BORDER_TYPE);
                    } else {
                        // Default status
                        siege.setStatus("RESERVED");
                        applyColorAndBorder(siege, SIG_COLOR_RES, SIG_BORDER_COLOR_RES,SIG_BORDER_WIDTH,SIG_BORDER_TYPE);
                    }
                }
            }
        }
    }
    
    // Method to load resale offers from the database
    private void loadResaleOffersFromDatabase() {
        resaleOffers.clear();  // Clear existing offers before loading

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String selectResaleSql = "SELECT * FROM resale WHERE eventID = ?";
            PreparedStatement selectResaleStatement = connection.prepareStatement(selectResaleSql);
            selectResaleStatement.setInt(1, CurrentEvent.getEventId());


            // Execute the SQL statement
            ResultSet resaleResultSet = selectResaleStatement.executeQuery();

            // Iterate through the result set and add resale offers to the map
            while (resaleResultSet.next()) {
                int siegeId = resaleResultSet.getInt("siegeID");
                double newPrice = resaleResultSet.getDouble("newPrice");

                resaleOffers.put(siegeId, newPrice);
            }

            // Close the result set, statement, and connection
            resaleResultSet.close();
            selectResaleStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    public void saveResaleOffersToDatabase(int currentEventId, Map<Integer, Double> resaleOffers) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Delete existing resale offers for the current event
            String deleteResaleSql = "DELETE FROM resale WHERE eventID = ?";
            PreparedStatement deleteResaleStatement = connection.prepareStatement(deleteResaleSql);
            deleteResaleStatement.setInt(1, currentEventId);
            deleteResaleStatement.executeUpdate();
            deleteResaleStatement.close();

            // Insert new resale offers for the current event
            String insertResaleSql = "INSERT INTO resale (eventID, sectionID, siegeID, newPrice) VALUES (?, ?, ?, ?)";
            PreparedStatement insertResaleStatement = connection.prepareStatement(insertResaleSql);

            for (Map.Entry<Integer, Double> entry : resaleOffers.entrySet()) {
                int siegeId = entry.getKey();
                double newPrice = entry.getValue();

                insertResaleStatement.setInt(1, currentEventId);
                insertResaleStatement.setInt(2, getSiegeById(siegeId).getSectionId());
                insertResaleStatement.setInt(3, siegeId);
                insertResaleStatement.setDouble(4, newPrice);

                insertResaleStatement.executeUpdate();
            }

            insertResaleStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    public Siege getSiegeById(int siegeId) {
        for (Section section : sections) {
            for (Siege siege : section.getSieges()) {
                if (siege.getSiegeId() == siegeId) {
                    return siege;
                }
            }
        }
        return null; // Return null if siege with the specified ID is not found
    }
    
    // Method to load switch offers from the database
    private void loadSwitchOffersFromDatabase() {
        switchRequests.clear();  // Clear existing offers before loading

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            String selectSwitchSql = "SELECT * FROM switch WHERE eventID = ?";
            PreparedStatement selectSwitchStatement = connection.prepareStatement(selectSwitchSql);
            selectSwitchStatement.setInt(1, CurrentEvent.getEventId());

            // Execute the SQL statement
            ResultSet switchResultSet = selectSwitchStatement.executeQuery();

            // Iterate through the result set and add switch offers to the list
            while (switchResultSet.next()) {
                int siegeId = switchResultSet.getInt("siegeID");
                switchRequests.add(siegeId);
            }

            // Close the result set, statement, and connection
            switchResultSet.close();
            selectSwitchStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    // Method to save switch offers to the database
    public void saveSwitchOffersToDatabase(int currentEventId, List<Integer> switchRequests) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Delete existing switch offers for the current event
            String deleteSwitchSql = "DELETE FROM switch WHERE eventID = ?";
            PreparedStatement deleteSwitchStatement = connection.prepareStatement(deleteSwitchSql);
            deleteSwitchStatement.setInt(1, currentEventId);
            deleteSwitchStatement.executeUpdate();
            deleteSwitchStatement.close();

            // Insert new switch offers for the current event
            String insertSwitchSql = "INSERT INTO switch (eventID, sectionID, siegeID) VALUES (?, ?, ?)";
            PreparedStatement insertSwitchStatement = connection.prepareStatement(insertSwitchSql);

            for (Integer siegeId : switchRequests) {
                insertSwitchStatement.setInt(1, currentEventId);
                insertSwitchStatement.setInt(2, getSiegeById(siegeId).getSectionId());
                insertSwitchStatement.setInt(3, siegeId);

                insertSwitchStatement.executeUpdate();
            }

            insertSwitchStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    // Method to update siege information labels
    private void updateSiegeInformation(Siege selectedSiege) {
        if (selectedSiege != null) {
            int sectionId = selectedSiege.getSectionId();
            int siegeId = selectedSiege.getSiegeId();

            Selected_Sec_Sig.setText("S" + sectionId + " - " + siegeId);

            // Get the price of the section from the section prices in CurrentEvent
            Map<Integer, Double> sectionPrices = CurrentEvent.getSectionPrices();
            double sectionPrice = sectionPrices.getOrDefault(sectionId, 0.0);

            Selected_Sec_Sig_price.setText(sectionPrice + "$");

            // Get the status of the siege
            String siegeStatus = selectedSiege.getStatus();
            SiegeState.setText(siegeStatus);
        } else {
            // If no siege is selected, clear the labels
            Selected_Sec_Sig.setText("");
            Selected_Sec_Sig_price.setText("");
            SiegeState.setText("");
        }
    }
    
    private void selectSiege(Siege siege) {
        // Code to handle siege selection
        selectedSiege = siege; // Get the selected siege
        updateSiegeStatus();
        selectedSiege.setStroke(SIG_SEL_BORDER_COLOR);
        updateSiegeInformation(selectedSiege);
    }
    
    private double getSiegePrice(int siegeId) {
        Siege siege = getSiegeById(siegeId);

        if (siege != null) {
            int sectionId = siege.getSectionId();
            return CurrentEvent.getSectionPrice(sectionId);
        }

        // Return a default value if the price cannot be obtained
        return 0.0;
    }
    
    
    
    private void updateMySpaceGrid() {
        MySpaceGrid.getChildren().clear(); // Clear existing elements

        // Add Siege and Price titles
        MySpaceGrid.add(SiegeTitle, 0, 0);
        MySpaceGrid.add(PriceTitle, 1, 0);

        // Loop through userSiegeIds to update the grid
        for (int i = 0; i < userSiegeIds.size() && i < 7; i++) {
            int siegeId = userSiegeIds.get(i);

            // Get the siege and its price
            Siege siege = getSiegeById(siegeId);
            double siegePrice = getSiegePrice(siegeId);

            // Update labels and checkbox for the siege
            Label siegeLabel = getSiegeLabel(i + 1);
            Label priceLabel = getPriceLabel(i + 1);
            CheckBox checkBox = getCheckBox(i + 1);

            // Set the content of labels
            siegeLabel.setText("S" + siege.getSectionId() + " - " + siege.getNumSiege());
            priceLabel.setText(String.format("%.2f$", siegePrice));

            // Apply color to the checkbox based on siege status
            switch (siege.getStatus()) {
                case "YOURS":
                    checkBox.setStyle("-fx-background-color: white;");
                    checkBox.setStyle("-fx-mark-highlight-color: black;"); // Set check mark color
                    break;
                case "RESALE":
                    checkBox.setStyle("-fx-background-color: " + SIG_COLOR_SAL + ";");
                    checkBox.setStyle("-fx-mark-highlight-color: white;"); // Set check mark color
                    break;
                case "SWITCH":
                    checkBox.setStyle("-fx-background-color: " + SIG_COLOR_SWI + ";");
                    checkBox.setStyle("-fx-mark-highlight-color: white;"); // Set check mark color
                    break;
                default:
                    checkBox.setStyle("-fx-background-color: white;");
                    checkBox.setStyle("-fx-mark-highlight-color: black;"); // Set check mark color
            }

            // Add labels and checkbox to the grid
            MySpaceGrid.add(siegeLabel, 0, i + 1);
            MySpaceGrid.add(priceLabel, 1, i + 1);
            MySpaceGrid.add(checkBox, 2, i + 1);
        }
    }
    
    @FXML
    private void handleBuyNowButton(ActionEvent event) {
        // Get the selected siege
        

        if (selectedSiege != null) {
            // Check if the selected siege is available
            if (selectedSiege.getStatus().equals("AVAILABLE")) {
                // Get the price for the selected section
                double sectionPrice = getSiegePrice(selectedSiege.getSiegeId());

                // Create a new reservation
                Reservation newReservation = new Reservation(
                    CurrentEvent.getEventId(),
                    CurrentUser.getUserID(),  // Replace with the actual user ID
                    selectedSiege.getSectionId(),
                    selectedSiege.getSiegeId(),
                    sectionPrice
                );

                // Add the reservation to the reservations list
                reservations.add(newReservation);

                // Create a new ticket
                Ticket newTicket = new Ticket(
                    generateTicketID(),  // Implement a method to generate a unique ticket ID
                    CurrentEvent.getEventId(),
                    sectionPrice,
                    selectedSiege.getSiegeId(),
                    CurrentEvent.getEventDate(),  // Assuming currentEvent has date, replace with the actual attribute
                    CurrentEvent.getEventTime()   // Assuming currentEvent has time, replace with the actual attribute
                );

                // Add the ticket to the userTickets list
                userTickets.add(newTicket);

                // Update the userSiegeIds list
                userSiegeIds.add(selectedSiege.getSiegeId());

                // Update the status of the selected siege to "RESERVED"
                selectedSiege.setStatus("RESERVED");

                // Apply the color and border for the updated sieges
                updateSiegeStatus();

                // Update the MySpaceGrid
                updateMySpaceGrid();

                // Save the reservations and tickets to the database
                saveReservationToDatabase(newReservation);
                saveTicketToDatabase(newTicket);

                // Display a success message or perform any other necessary actions
                System.out.println("Purchase successful!");
            } else {
                // The selected siege is not available
                showAlert("Purchase Warning", "Selected Siege Not Available",
                        "The selected siege is not available for purchase.");
            }
        } else {
            // No siege selected
            showAlert("Purchase Warning", "No Siege Selected",
                "Please select a siege before attempting to purchase.");
        }
    }
    
    public void saveReservationToDatabase(Reservation reservation) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Insert a new reservation for the current user and event
            String insertReservationSql = "INSERT INTO reservations (ResEventID, ResUserID, ResSectionID, ResSiegeID, Price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertReservationStatement = connection.prepareStatement(insertReservationSql);

            insertReservationStatement.setInt(1, CurrentEvent.getEventId());
            insertReservationStatement.setInt(2, CurrentUser.getUserID());
            insertReservationStatement.setInt(3, reservation.sectionID());
            insertReservationStatement.setInt(4, reservation.siegeID());
            insertReservationStatement.setDouble(5, reservation.price());

            insertReservationStatement.executeUpdate();

            // Close the statement and connection
            insertReservationStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void saveTicketToDatabase(Ticket ticket) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Insert a new ticket for the current user and event
            String insertTicketSql = "INSERT INTO tickets (TicketID, TicEventID, TicPrice, TicSiegeID, TicDate, TicTime) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertTicketStatement = connection.prepareStatement(insertTicketSql);

            insertTicketStatement.setInt(1, ticket.getTicketID());
            insertTicketStatement.setInt(2, ticket.getEventId());
            insertTicketStatement.setDouble(3, ticket.getPrice());
            insertTicketStatement.setInt(4, ticket.getSeatNumber());
            insertTicketStatement.setDate(5, java.sql.Date.valueOf(ticket.getDate()));
            insertTicketStatement.setString(6, ticket.getTime().toString());

            insertTicketStatement.executeUpdate();

            // Close the statement and connection
            insertTicketStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
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
    //******** extrat ****************//
    private void setRadiusAndScale() {
        String query = "SELECT radius, scale FROM Sieges LIMIT 1";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                double radius = resultSet.getDouble("radius");
                double scale = resultSet.getDouble("scale");
                Siege.setStaticRadius(radius);
                Siege.setScaleAll(scale);
            } 

        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
    
    public void applyColorAndBorder(Shape shape, Color fillColor, Color borderColor, double borderWidth, StrokeType strokeType) {
        shape.setFill(fillColor);
        shape.setStroke(borderColor);
        shape.setStrokeWidth(borderWidth);
        shape.setStrokeType(strokeType);
    }
    
    private double zoomLevel = 1.0;
    public void zoomInButtonClicked() {
        zoomLevel *= 1.1; // You can adjust the factor as needed
        applyZoom();
        System.out.println("zoom in");
        
        StadeLayout.setPrefWidth(StadeLayout.getWidth()+200);
        StadeLayout.setPrefHeight(StadeLayout.getHeight()+200);
    }

    public void zoomOutButtonClicked() {
        zoomLevel /= 1.1; // You can adjust the factor as needed

        // Ensure the zoom level doesn't go below 1.0
        if (zoomLevel < 1.0) {
            zoomLevel = 1.0;
        }
        applyZoom();
        System.out.println("zoom out");
        
        StadeLayout.setPrefWidth(StadeLayout.getWidth()-200);
        StadeLayout.setPrefHeight(StadeLayout.getHeight()-200);
    }

    private void applyZoom() {
        StadeLayout.setScaleX(zoomLevel);
        StadeLayout.setScaleY(zoomLevel);
        
        System.out.println("zoom applied");
        
        StadeLayout.setTranslateX(StadeLayout.getWidth()*(zoomLevel - 1.0)/2);
        StadeLayout.setTranslateY(StadeLayout.getHeight()*(zoomLevel - 1.0)/2);
    }
    
    private Integer generateTicketID() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Select all existing ticket IDs from the tickets table
            String selectTicketIdsSql = "SELECT ticketID FROM tickets";
            Statement selectTicketIdsStatement = connection.createStatement();
            ResultSet ticketIdsResultSet = selectTicketIdsStatement.executeQuery(selectTicketIdsSql);

            // Create a set to store existing ticket IDs
            Set<Integer> existingTicketIds = new HashSet<>();

            // Populate the set with existing ticket IDs
            while (ticketIdsResultSet.next()) {
                existingTicketIds.add(ticketIdsResultSet.getInt("TicketID"));
            }

            // Close the result set and statement
            ticketIdsResultSet.close();
            selectTicketIdsStatement.close();

            // Generate a new ticket ID until a unique one is found
            int newTicketID;
            do {
                newTicketID = generateRandomTicketID();
            } while (existingTicketIds.contains(newTicketID));

            // Close the connection
            connection.close();

            return newTicketID;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return null;
        }
    }

    private Integer generateRandomTicketID() {
        // Generate a random 5-digit ticket ID
        Random random = new Random();
        int randomId = 10000 + random.nextInt(90000);
        return randomId;
    }
    
    private void showAlert(String title,String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }  
    
    private Label getSiegeLabel(int index) {
    switch (index) {
        case 1:
            return YourSiege1;
        case 2:
            return YourSiege2;
        case 3:
            return YourSiege3;
        case 4:
            return YourSiege4;
        case 5:
            return YourSiege5;
        case 6:
            return YourSiege6;
        case 7:
            return YourSiege7;
        default:
            return null;
    }
}

private Label getPriceLabel(int index) {
    switch (index) {
        case 1:
            return YourPrice1;
        case 2:
            return YourPrice2;
        case 3:
            return YourPrice3;
        case 4:
            return YourPrice4;
        case 5:
            return YourPrice5;
        case 6:
            return YourPrice6;
        case 7:
            return YourPrice7;
        default:
            return null;
    }
}

private CheckBox getCheckBox(int index) {
    switch (index) {
        case 1:
            return check1;
        case 2:
            return check2;
        case 3:
            return check3;
        case 4:
            return check4;
        case 5:
            return check5;
        case 6:
            return check6;
        case 7:
            return check7;
        default:
            return null;
    }
}
    
}
