/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.Concert;
import Modules.Evenement;
import Modules.Match;
import Modules.User;
import Modules.Visitor;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static javafx.scene.shape.StrokeType.INSIDE;
import static javafx.scene.shape.StrokeType.OUTSIDE;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import static javafx.scene.text.TextAlignment.CENTER;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class EventClientController implements Initializable {

    @FXML
    private VBox mainLayout;
    
    @FXML
    private HBox NewBox;
    
    private User CurrentUser;

    private List<Evenement> events = new ArrayList<>();
    private Map<String, HBox> matchDisplayMap = new HashMap<>();
    private Map<String, HBox> concertDisplayMap = new HashMap<>();
    private final int maxColumns = 7; // Adjust as needed
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainLayout.getChildren().clear();
        loadEventsFromDatabase();
        
    }    
    public void setUser(User user) {this.CurrentUser = user;}
    public void loadEventsFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for retrieving matches from the database
            String selectMatchesSql = "SELECT * FROM matches";
            PreparedStatement selectMatchesStatement = connection.prepareStatement(selectMatchesSql);

            // Execute the SQL statement
            ResultSet matchesResultSet = selectMatchesStatement.executeQuery();

            // Clear existing events
            events.clear();

            // Iterate through the result set and create Match objects
            while (matchesResultSet.next()) {
                int eventId = matchesResultSet.getInt("matchId");
                String eventName = matchesResultSet.getString("eventName");
                LocalDate eventDate = matchesResultSet.getDate("eventDate").toLocalDate();
                LocalTime eventTime = LocalTime.parse(matchesResultSet.getString("eventTime"));
                String description = matchesResultSet.getString("description");
                String team1Name = matchesResultSet.getString("team1");
                String team2Name = matchesResultSet.getString("team2");
                String logoTeam1 = matchesResultSet.getString("team1Logo");
                String logoTeam2 = matchesResultSet.getString("team2Logo");

                // Fetch section prices for the match
                Map<Integer, Double> sectionPrices = fetchSectionPrices(eventId);

                // Create Match object and add it to the events list
                Match match = new Match(eventId, eventName, eventDate, eventTime, sectionPrices, description, team1Name, team2Name, logoTeam1, logoTeam2);
                events.add(match);
            }

            // Close the result set, statement, and connection
            matchesResultSet.close();
            selectMatchesStatement.close();
            
            // Prepare the SQL statement for retrieving concerts from the database
            String selectConcertsSql = "SELECT * FROM concerts";
            PreparedStatement selectConcertsStatement = connection.prepareStatement(selectConcertsSql);

            // Execute the SQL statement
            ResultSet concertsResultSet = selectConcertsStatement.executeQuery();

            // Iterate through the result set and create Concert objects
            while (concertsResultSet.next()) {
                int eventId = concertsResultSet.getInt("concertId");
                String eventName = concertsResultSet.getString("concertName");
                LocalDate eventDate = concertsResultSet.getDate("concertDate").toLocalDate();
                LocalTime eventTime = LocalTime.parse(concertsResultSet.getString("concertTime"));
                String description = concertsResultSet.getString("concertDescription");
                String posterImg = concertsResultSet.getString("posterImg");

                // Fetch section prices for the concert
                Map<Integer, Double> sectionPrices = fetchConcertSectionPrices(eventId);
                
                Set<String> artists = new HashSet<>(Arrays.asList(concertsResultSet.getString("artists").split(",\\s*")));
                

                // Create Concert object and add it to the events list
                Concert concert = new Concert(eventId, eventName, eventDate, eventTime, sectionPrices, description,artists,posterImg);
                events.add(concert);
            }

            // Close the result set, statement, and connection
            concertsResultSet.close();
            selectConcertsStatement.close();
        
            connection.close();

            // Display the loaded events
            updateEventDisplays();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    private Map<Integer, Double> fetchConcertSectionPrices(int eventId) {
        Map<Integer, Double> sectionPrices = new HashMap<>();

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for retrieving section prices from the database
            String selectSectionPricesSql = "SELECT * FROM concert_section_prices WHERE concertId = ?";
            PreparedStatement selectSectionPricesStatement = connection.prepareStatement(selectSectionPricesSql);
            selectSectionPricesStatement.setInt(1, eventId);

            // Execute the SQL statement
            ResultSet sectionPricesResultSet = selectSectionPricesStatement.executeQuery();

            // Iterate through the result set and add section prices to the map
            while (sectionPricesResultSet.next()) {
                int sectionId = sectionPricesResultSet.getInt("sectionId");
                double price = sectionPricesResultSet.getDouble("price");

                sectionPrices.put(sectionId, price);
            }

            // Close the result set and statement
            sectionPricesResultSet.close();
            selectSectionPricesStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return sectionPrices;
    }

    private Map<Integer, Double> fetchSectionPrices(int eventId) {
        Map<Integer, Double> sectionPrices = new HashMap<>();

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for retrieving section prices from the database
            String selectSectionPricesSql = "SELECT * FROM match_section_prices WHERE matchId = ?";
            PreparedStatement selectSectionPricesStatement = connection.prepareStatement(selectSectionPricesSql);
            selectSectionPricesStatement.setInt(1, eventId);

            // Execute the SQL statement
            ResultSet sectionPricesResultSet = selectSectionPricesStatement.executeQuery();

            // Iterate through the result set and add section prices to the map
            while (sectionPricesResultSet.next()) {
                int sectionId = sectionPricesResultSet.getInt("sectionId");
                double price = sectionPricesResultSet.getDouble("price");

                sectionPrices.put(sectionId, price);
            }

            // Close the result set, statement, and connection
            sectionPricesResultSet.close();
            selectSectionPricesStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return sectionPrices;
    }
    
    private void updateEventDisplays() {
        // Clear the mainLayout
        mainLayout.getChildren().clear();

        // Add NewBox to mainLayout (if needed)
        mainLayout.getChildren().add(NewBox);

        // Loop through all events and create/update displays
        HBox eventDisplay = new HBox();
        for (Evenement event : events) {
            if (event instanceof Match) {
                Match match = (Match) event;
                eventDisplay = createDuplicateMatchDisplay(match);
            } else if (event instanceof Concert) {
                Concert concert = (Concert) event;
                eventDisplay = createDuplicateConcertDisplay(concert);
            }

            System.out.println("Displaying an event");
            // Add the customized display to mainLayout
            mainLayout.getChildren().add(eventDisplay);
        }
    }
    
    private HBox createDuplicateConcertDisplay(Concert event) {
        System.out.println("Creating an event");
        HBox duplicatedDisplay = new HBox();
        
        VBox.setMargin(duplicatedDisplay, new Insets(20, 50, 0, 50));
        duplicatedDisplay.setPadding(new Insets(15, 0, 20, 0));
        
        duplicatedDisplay.getStyleClass().add("Event-display");
        duplicatedDisplay.setAlignment(Pos.CENTER_LEFT);

        // Create and configure the elements based on the values of the Match event
        Region region = new Region();
        region.setPrefHeight(85.0);
        region.setPrefWidth(30.0);

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.TOP_CENTER);
        imageBox.setPrefHeight(48.0);
        imageBox.setPrefWidth(60.0);

        System.out.println("football image before");
        ImageView concertSymbol = new ImageView(new Image("/img/guitar-128 (1).png"));
        System.out.println("football image after");
        concertSymbol.setFitHeight(60.0);
        concertSymbol.setFitWidth(60.0);
        concertSymbol.setPickOnBounds(true);
        concertSymbol.setPreserveRatio(true);

        
        imageBox.getChildren().add(concertSymbol);
        System.out.println("football iamge");
        
        Region region2 = new Region();
        region2.setPrefHeight(83.0);
        region2.setPrefWidth(25.0);

        ImageView posterImg = new ImageView(new Image(event.getPosterImagePath()));
        posterImg.setFitHeight(209.0);
        posterImg.setFitWidth(215.0);
        posterImg.setPreserveRatio(true);
        
        System.out.println("poster image");

        HBox posterBox = new HBox();
        posterBox.setAlignment(Pos.CENTER);
        posterBox.getChildren().add(posterImg);

        
        imageBox.setMargin(concertSymbol, new Insets(0, 0, 0, 10));
        
        

        Region region3 = new Region();
        region.setPrefHeight(209.0);
        region.setPrefWidth(10.0);
        
        VBox innerVBox = new VBox();
        innerVBox.setPrefHeight(209.0);
        innerVBox.setPrefWidth(315.0);

        VBox artistsVBox = new VBox();
        artistsVBox.setAlignment(Pos.TOP_LEFT);
        artistsVBox.setPrefHeight(209.0);
        artistsVBox.setPrefWidth(329.0);
        artistsVBox.setPadding(new Insets(0, 0, 0, 20));
        
        Label EventNameLabel = new Label(event.getEventName());
        EventNameLabel.setPadding(new Insets(0, 0, 0, 20));
        EventNameLabel.getStyleClass().add("ConcertTitle-style");
        
        for (String artist : event.getArtists()) {
            Label artistLabel = new Label(artist);
            artistLabel.getStyleClass().add("artistNames-style");
            // Add styling or other properties to the artist label as needed
            artistsVBox.getChildren().add(artistLabel);
        }

        VBox priceRangeBox = new VBox();
        priceRangeBox.setAlignment(Pos.TOP_RIGHT);
        priceRangeBox.setPrefHeight(85.0);
        priceRangeBox.setPrefWidth(193.0);
        VBox.setMargin(priceRangeBox,new Insets(0, 20, 0, 0));

        Text priceRangeText = new Text("Price Range");
        priceRangeText.setFill(Color.NAVY);
        priceRangeText.setStrokeType(OUTSIDE);
        priceRangeText.setStrokeWidth(0.0);
        priceRangeText.getStyleClass().add("smallTitle-style");
        priceRangeText.setTextAlignment(CENTER);
        priceRangeText.setWrappingWidth(158.13677978515625);

        double minPrice = event.getMinPrice();
        double maxPrice = event.getMaxPrice();

        String minPriceText = minPrice % 1 == 0 ? String.format("%.0f", minPrice) : String.format("%.2f", minPrice);
        String maxPriceText = maxPrice % 1 == 0 ? String.format("%.0f", maxPrice) : String.format("%.2f", maxPrice);

        Label priceBoxLabel = new Label(minPriceText + "$ - " + maxPriceText + "$");
        priceBoxLabel.setAlignment(Pos.CENTER);
        priceBoxLabel.setPrefHeight(45.0);
        priceBoxLabel.setPrefWidth(170.0);
        priceBoxLabel.getStyleClass().add("prices-style");
        priceBoxLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));


        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(Color.WHITE);
        rectangle.setHeight(220.0);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(INSIDE);
        rectangle.setWidth(5.0);

        VBox descriptionBox = new VBox();
        descriptionBox.setPrefHeight(45.0);
        descriptionBox.setPrefWidth(400.0);

        HBox dateBox = new HBox();
        dateBox.setPrefHeight(45.0);
        dateBox.setPrefWidth(193.0);

        Label dateLabelTitle = new Label("Date :");
        dateLabelTitle.setPrefHeight(30.0);
        dateLabelTitle.setPrefWidth(63.0);
        dateLabelTitle.getStyleClass().add("smallTitle2-style");
        dateLabelTitle.getStylesheets().add("/Styles/stadebuilder.css");

        Label dateLabel = new Label(event.getFormattedDate());
        dateLabel.getStyleClass().add("label-style");

        HBox timeBox = new HBox();
        timeBox.setPrefHeight(30.0);
        timeBox.setPrefWidth(200.0);

        Label timeLabelTitle = new Label("Time :");
        timeLabelTitle.setPrefHeight(30.0);
        timeLabelTitle.setPrefWidth(63.0);
        timeLabelTitle.getStyleClass().add("smallTitle2-style");
        timeLabelTitle.getStylesheets().add("/Styles/stadebuilder.css");

        Label timeLabel = new Label(event.getFormattedTime());
        timeLabel.getStyleClass().add("label-style");

        VBox descriptionContentBox = new VBox();
        descriptionContentBox.setPrefHeight(98.0);
        descriptionContentBox.setPrefWidth(637.0);

        Text descriptionText = new Text(event.getDescription());
        descriptionText.setId("MatchDescription");
        descriptionText.setFill(Color.NAVY);
        descriptionText.setStrokeType(OUTSIDE);
        descriptionText.setStrokeWidth(0.0);
        descriptionText.getStyleClass().add("Description-style");
        descriptionText.setWrappingWidth(750.4700317382812);
        
        HBox.setMargin(descriptionBox, new Insets(10, 0, 0, 10));

        //descriptionText.setPadding(new Insets(0, 10, 0, 10));
        descriptionContentBox.getChildren().add(descriptionText);
        
        
        
        Region region4 = new Region();
        region4.setPrefHeight(85.0);
        region4.setPrefWidth(80.0);

        

        VBox buttonsBox = new VBox();
        buttonsBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonsBox.setPrefHeight(92.0);
        buttonsBox.setPrefWidth(463.0);

        HBox buttonsInnerBox = new HBox();
        buttonsInnerBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonsInnerBox.setPrefHeight(42.0);
        buttonsInnerBox.setPrefWidth(315.0);
        buttonsInnerBox.setSpacing(10.0);

        Button checkMerchButton = new Button("Check Merch");
        checkMerchButton.getStyleClass().add("button-style");
        checkMerchButton.setPrefWidth(140);
        checkMerchButton.setPrefHeight(50);

        Button checkTicketButton = new Button("Check Ticket");
        checkTicketButton.getStyleClass().add("button-style");
        checkTicketButton.setPrefWidth(140);
        checkTicketButton.setPrefHeight(50);

        Button reserveNowButton = new Button("Reserve Now");
        reserveNowButton.getStyleClass().add("button-style");
        reserveNowButton.setPrefWidth(140);
        reserveNowButton.setPrefHeight(50);

        buttonsInnerBox.getChildren().addAll(checkMerchButton, checkTicketButton, reserveNowButton);
        HBox.setMargin(reserveNowButton, new Insets(0, 10, 0, 0)); // Adjust as needed

        buttonsBox.getChildren().add(buttonsInnerBox);
        VBox.setMargin(buttonsBox, new Insets(0, 10, 0, 10));
        

        System.out.println("step 6");
        
        priceRangeBox.getChildren().addAll(priceRangeText, priceBoxLabel);
        innerVBox.getChildren().addAll(EventNameLabel,artistsVBox, priceRangeBox);
        descriptionBox.getChildren().addAll(dateBox, descriptionContentBox, timeBox);

        System.out.println("step 7");
        dateBox.getChildren().addAll(
                createLabelBox("Date :", dateLabel, "smallTitle2-style", "/Styles/stadebuilder.css"),
                createLabelBox("Time :", timeLabel, "smallTitle2-style", "/Styles/stadebuilder.css")
        );
        
        VBox dateAndDescriptionBox = new VBox();
        dateAndDescriptionBox.getChildren().addAll(dateBox, descriptionBox);
        System.out.println("step 8");
        
        Region region31 = new Region();
        region31.setPrefWidth(20.0);
        duplicatedDisplay.getChildren().addAll(
                region, imageBox, region2, posterBox, region3,
                innerVBox, rectangle, region31,dateAndDescriptionBox, buttonsBox
        );
        System.out.println("step 9");
        
        reserveNowButton.setOnAction(ev -> {
                try {
                    // Load the FXML file for the StadeViewer
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/StadeViewer.fxml"));
                    Parent stadeViewerRoot = stadeViewerLoader.load();
                    Scene stadeViewerScene = new Scene(stadeViewerRoot);

                    // Get the controller for the StadeViewer
                    StadeViewerController stadeViewerController = stadeViewerLoader.getController();

                    // Pass information (user and evenement) to the StadeViewer
                    //stadeViewerController.setUser(CurrentUser); // Replace 'currentUser' with your User object
                    Evenement selectedEvenement;
                    stadeViewerController.setUserData(CurrentUser,event); // Replace 'selectedEvenement' with your Evenement object

                    // Set up the primary stage for the StadeViewer
                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(stadeViewerScene);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        
        checkMerchButton.setOnAction(ev -> {
                try {
                    // Load the FXML file for the StadeViewer
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/ShopViewer.fxml"));
                    Parent shopViewerRoot = stadeViewerLoader.load();
                    Scene shopViewerScene = new Scene(shopViewerRoot);

                    // Get the controller for the StadeViewer
                    StadeViewerController shopViewerController = stadeViewerLoader.getController();

                    // Pass information (user and evenement) to the StadeViewer
                    //stadeViewerController.setUser(CurrentUser); // Replace 'currentUser' with your User object
                    Evenement selectedEvenement;
                    shopViewerController.setUserData(CurrentUser,event); // Replace 'selectedEvenement' with your Evenement object

                    // Set up the primary stage for the StadeViewer
                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(shopViewerScene);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });
        
        checkTicketButton.setOnAction(ev -> {
                try {
                    // Load the FXML file for the StadeViewer
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/TicketView.fxml"));
                    Parent ticketViewRoot = stadeViewerLoader.load();
                    Scene ticketViewScene = new Scene(ticketViewRoot);

                    // Get the controller for the StadeViewer
                    TicketViewController ticketViewCon = stadeViewerLoader.getController();

                    // Pass information (user and evenement) to the StadeViewer
                    ticketViewCon.setUserData(CurrentUser,event); // Replace 'currentUser' with your User object
                    Evenement selectedEvenement;
                    //stadeViewerController.setEvenement(event); // Replace 'selectedEvenement' with your Evenement object

                    // Set up the primary stage for the StadeViewer
                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(ticketViewScene);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            });

        //deleteButton.setOnAction(this::handleDeleteConcertButtonClick);
        //editButton.setOnAction(this::handleEditConcertButtonClick);

        //deleteButton.setOnAction(e -> handleDeleteButtonClick(e));

        return duplicatedDisplay;
    }
    
    private HBox createDuplicateMatchDisplay(Match event) {
        System.out.println("Creating an event");
        HBox duplicatedDisplay = new HBox();
        
        VBox.setMargin(duplicatedDisplay, new Insets(20, 50, 0, 50));
        duplicatedDisplay.setPadding(new Insets(15, 0, 20, 0));
        
        duplicatedDisplay.getStyleClass().add("Event-display");
        duplicatedDisplay.setAlignment(Pos.CENTER_LEFT);

        // Create and configure the elements based on the values of the Match event
        Region region = new Region();
        region.setPrefHeight(85.0);
        region.setPrefWidth(30.0);

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefHeight(48.0);
        imageBox.setPrefWidth(60.0);

        System.out.println("football image before");
        ImageView matchSymbol = new ImageView(new Image("/img/football-2-128 (1).png"));
        System.out.println("football image after");
        matchSymbol.setFitHeight(60.0);
        matchSymbol.setFitWidth(60.0);
        matchSymbol.setPickOnBounds(true);
        matchSymbol.setPreserveRatio(true);

        
        imageBox.getChildren().add(matchSymbol);
        System.out.println("football iamge");
        
        Region region2 = new Region();
        region2.setPrefHeight(83.0);
        region2.setPrefWidth(25.0);

        ImageView team1Logo = new ImageView(new Image(event.getLogoTeam1()));
        team1Logo.setFitHeight(73.0);
        team1Logo.setFitWidth(65.0);
        team1Logo.setPreserveRatio(true);
        
        System.out.println("logo1 image");

        Text vsText = new Text("VS");
        vsText.setTextAlignment(TextAlignment.CENTER);
        vsText.setFont(new Font(36.0));
        vsText.setWrappingWidth(96.80337524414062);

        ImageView team2Logo = new ImageView(new Image(event.getLogoTeam2()));
        team2Logo.setFitHeight(80.0);
        team2Logo.setFitWidth(85.0);
        team2Logo.setPreserveRatio(true);
        
        HBox team1LogoBox = new HBox();
        team1LogoBox.setAlignment(Pos.CENTER);
        team1LogoBox.getChildren().add(team1Logo);

        // Center the team2Logo
        HBox team2LogoBox = new HBox();
        team2LogoBox.setAlignment(Pos.CENTER);
        team2LogoBox.getChildren().add(team2Logo);

        
        
        imageBox.setMargin(matchSymbol, new Insets(0, 20, 0, 10));
        VBox.setMargin(vsText, new Insets(0, 10, 0, 10));
        VBox.setMargin(team2Logo, new Insets(0, 10, 0, 10));
        System.out.println("logo2 image");
        

        Region region3 = new Region();
        region3.setLayoutX(100.0);
        region3.setLayoutY(85.0);
        region3.setPrefHeight(25.0);
        region3.setPrefWidth(85.0);

        VBox priceRangeBox = new VBox();
        priceRangeBox.setAlignment(Pos.CENTER);
        priceRangeBox.setPrefHeight(85.0);
        priceRangeBox.setPrefWidth(193.0);
        VBox.setMargin(priceRangeBox,new Insets(0, 20, 0, 0));

        Text priceRangeText = new Text("Price Range");
        priceRangeText.setFill(Color.NAVY);
        priceRangeText.setStrokeType(OUTSIDE);
        priceRangeText.setStrokeWidth(0.0);
        priceRangeText.getStyleClass().add("smallTitle-style");
        priceRangeText.setTextAlignment(CENTER);
        priceRangeBox.setMargin(priceRangeText,new Insets(0, 20, 0, 0));
        
        priceRangeText.setWrappingWidth(158.13677978515625);

        double minPrice = event.getMinPrice();
        double maxPrice = event.getMaxPrice();

        String minPriceText = minPrice % 1 == 0 ? String.format("%.0f", minPrice) : String.format("%.2f", minPrice);
        String maxPriceText = maxPrice % 1 == 0 ? String.format("%.0f", maxPrice) : String.format("%.2f", maxPrice);

        Label priceBoxLabel = new Label(minPriceText + "$ - " + maxPriceText + "$");
        priceBoxLabel.setAlignment(Pos.CENTER);
        priceBoxLabel.setPrefHeight(45.0);
        priceBoxLabel.setPrefWidth(170.0);
        priceBoxLabel.setTextAlignment(CENTER);
        priceBoxLabel.getStyleClass().add("prices-style");
        priceBoxLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
        priceRangeBox.setMargin(priceBoxLabel,new Insets(0, 20, 0, 0));
        
        //priceBoxLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

        Rectangle rectangle = new Rectangle();
        rectangle.setArcHeight(5.0);
        rectangle.setArcWidth(5.0);
        rectangle.setFill(Color.WHITE);
        rectangle.setHeight(78.0);
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeType(INSIDE);
        rectangle.setWidth(5.0);

        VBox descriptionBox = new VBox();
        descriptionBox.setPrefHeight(45.0);
        descriptionBox.setPrefWidth(797.0);

        HBox dateBox = new HBox();
        dateBox.setPrefHeight(45.0);
        dateBox.setPrefWidth(193.0);

        Label dateLabelTitle = new Label("Date :");
        dateLabelTitle.setPrefHeight(30.0);
        dateLabelTitle.setPrefWidth(63.0);
        dateLabelTitle.getStyleClass().add("smallTitle2-style");
        dateLabelTitle.getStylesheets().add("/Styles/stadebuilder.css");

        Label dateLabel = new Label(event.getFormattedDate());
        dateLabel.getStyleClass().add("label-style");

        HBox timeBox = new HBox();
        timeBox.setPrefHeight(30.0);
        timeBox.setPrefWidth(200.0);

        Label timeLabelTitle = new Label("Time :");
        timeLabelTitle.setPrefHeight(30.0);
        timeLabelTitle.setPrefWidth(63.0);
        timeLabelTitle.getStyleClass().add("smallTitle2-style");
        timeLabelTitle.getStylesheets().add("/Styles/stadebuilder.css");

        Label timeLabel = new Label(event.getFormattedTime());
        timeLabel.getStyleClass().add("label-style");

        VBox descriptionContentBox = new VBox();
        descriptionContentBox.setPrefHeight(98.0);
        descriptionContentBox.setPrefWidth(673.0);

        Text descriptionText = new Text(event.getDescription());
        descriptionText.setId("MatchDescription");
        descriptionText.setFill(Color.NAVY);
        descriptionText.setStrokeType(OUTSIDE);
        descriptionText.setStrokeWidth(0.0);
        descriptionText.getStyleClass().add("Description-style");
        descriptionText.setWrappingWidth(670.4700317382812);
        
        HBox.setMargin(descriptionBox, new Insets(10, 0, 0, 10));

        //descriptionText.setPadding(new Insets(0, 10, 0, 10));
        descriptionContentBox.getChildren().add(descriptionText);
        
        
        
        Region region4 = new Region();
        region4.setPrefHeight(85.0);
        region4.setPrefWidth(80.0);

        VBox buttonsBox = new VBox();
        buttonsBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonsBox.setPrefHeight(92.0);
        buttonsBox.setPrefWidth(453.0);

        HBox buttonsInnerBox = new HBox();
        buttonsInnerBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonsInnerBox.setPrefHeight(42.0);
        buttonsInnerBox.setPrefWidth(300.0);
        buttonsInnerBox.setSpacing(10.0);

        Button checkMerchButton = new Button("Check Merch");
        checkMerchButton.getStyleClass().add("button-style");
        checkMerchButton.setPrefWidth(140);
        checkMerchButton.setPrefHeight(50);

        Button checkTicketButton = new Button("Check Ticket");
        checkTicketButton.getStyleClass().add("button-style");
        checkTicketButton.setPrefWidth(140);
        checkTicketButton.setPrefHeight(50);

        Button reserveNowButton = new Button("Reserve Now");
        reserveNowButton.getStyleClass().add("button-style");
        reserveNowButton.setPrefWidth(140);
        reserveNowButton.setPrefHeight(50);
        
        String buttonId = String.valueOf(event.getEventId());
        checkMerchButton.setId("checkMerch_" + buttonId);
        checkTicketButton.setId("checkTicket_" + buttonId);
        reserveNowButton.setId("reserveNow_" + buttonId);

        buttonsInnerBox.getChildren().addAll(checkMerchButton, checkTicketButton, reserveNowButton);
        HBox.setMargin(reserveNowButton, new Insets(0, 10, 0, 0)); // Adjust as needed

        buttonsBox.getChildren().add(buttonsInnerBox);
        VBox.setMargin(buttonsBox, new Insets(0, 10, 0, 10));

        

        System.out.println("step 6");
        
        priceRangeBox.getChildren().addAll(priceRangeText, priceBoxLabel);
        descriptionBox.getChildren().addAll(dateBox, descriptionContentBox, timeBox);

        System.out.println("step 7");
        dateBox.getChildren().addAll(
                createLabelBox("Date :", dateLabel, "smallTitle2-style", "/Styles/stadebuilder.css"),
                createLabelBox("Time :", timeLabel, "smallTitle2-style", "/Styles/stadebuilder.css")
        );
        
        VBox dateAndDescriptionBox = new VBox();
        dateAndDescriptionBox.getChildren().addAll(dateBox, descriptionBox);
        System.out.println("step 8");
        
        Region region31 = new Region();
        region31.setPrefWidth(20.0);
        duplicatedDisplay.getChildren().addAll(
                region, imageBox, region2, team1LogoBox, vsText, team2LogoBox, region3,
                priceRangeBox, rectangle, region31,dateAndDescriptionBox, buttonsBox
        );
        System.out.println("step 9");
        
        reserveNowButton.setOnAction(ev -> {
                try {
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/StadeViewer.fxml"));
                    Parent stadeViewerRoot = stadeViewerLoader.load();
                    Scene stadeViewerScene = new Scene(stadeViewerRoot);

                    StadeViewerController stadeViewerController = stadeViewerLoader.getController();

                    stadeViewerController.setUserData(CurrentUser,event);

                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(stadeViewerScene);
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            });
        
        checkMerchButton.setOnAction(ev -> {
                try {
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/ShopViewer.fxml"));
                    Parent shopViewerRoot = stadeViewerLoader.load();
                    Scene shopViewerScene = new Scene(shopViewerRoot);

                    ShopViewerController shopViewerController = stadeViewerLoader.getController();

                   
                    Evenement selectedEvenement;
                    shopViewerController.setUserData(CurrentUser,event);

                    // Set up the primary stage for the StadeViewer
                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(shopViewerScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        
        checkTicketButton.setOnAction(ev -> {
                try {
                    FXMLLoader stadeViewerLoader = new FXMLLoader(getClass().getResource("/Vues/TicketView.fxml"));
                    Parent ticketViewRoot = stadeViewerLoader.load();
                    Scene ticketViewScene = new Scene(ticketViewRoot);

                    TicketViewController ticketViewCon = stadeViewerLoader.getController();

                    ticketViewCon.setUserData(CurrentUser,event); 
                    Evenement selectedEvenement;

                    Stage primaryStage = (Stage) reserveNowButton.getScene().getWindow();
                    primaryStage.setScene(ticketViewScene);
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            });

        return duplicatedDisplay;
    }
    
    private HBox createLabelBox(String title, Label label, String styleClass, String stylesheet) {
        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER_LEFT);
        labelBox.setPrefHeight(43.0);
        labelBox.setPrefWidth(660.0);

        Label titleLabel = new Label(title);
        titleLabel.setPrefHeight(30.0);
        titleLabel.setPrefWidth(63.0);
        titleLabel.getStyleClass().add(styleClass);
        titleLabel.getStylesheets().add(stylesheet);

        labelBox.getChildren().addAll(titleLabel, label);

        return labelBox;
    }
    
    
}
