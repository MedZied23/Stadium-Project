/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;


import Modules.Concert;
import Modules.Evenement;
import Modules.Match;
import Modules.Section;
import Modules.Siege;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import static javafx.scene.shape.StrokeType.INSIDE;
import static javafx.scene.shape.StrokeType.OUTSIDE;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import static javafx.scene.text.TextAlignment.CENTER;


public class EventManagerController implements Initializable {

    @FXML
    private TextField team1NameField;

    @FXML
    private TextField team2NameField;
    
    @FXML
    private TextField artistNameField;
    
    @FXML
    private TextField EventNameField;
    
    @FXML
    private ImageView MenuStade;
    
    @FXML
    private ImageView MenuEvents;
    
    

    @FXML
    private Button team1LogoButton;

    @FXML
    private Button team2LogoButton;

    @FXML
    private DatePicker datePickerMatch;
    
    @FXML
    private DatePicker datePickerConcert;
    
    @FXML
    private Spinner<Integer> MatchHourSpinner;

    @FXML
    private Spinner<Integer> MatchMinuteSpinner;

    @FXML
    private Spinner<Integer> ConcertHourSpinner;

    @FXML
    private Spinner<Integer> ConcertMinuteSpinner;

    @FXML
    private TextArea descriptionTextAreaMatch;
    
    @FXML
    private TextArea descriptionTextAreaConcert;

    @FXML
    private GridPane sectionDetailsGridMatch;
    
    @FXML
    private GridPane sectionDetailsGridConcert;
    
    @FXML
    private Button createMatchButton;

    @FXML
    private Button createConcertButton;
    
    @FXML
    private Button saveMatchButton;
    
    @FXML
    private Button resetMatchButton;
    
    @FXML
    private Button saveConcertButton;
    
    @FXML
    private Button resetConcertButton;
    
    
    
    @FXML
    private HBox MatchForm;
    
    @FXML
    private HBox ConcertForm;
    
    @FXML
    private VBox artistsTextFields;
    
    
    
    @FXML
    private HBox NewBox;
    
    @FXML
    private HBox MatchDisplay;
    
    @FXML
    private VBox mainLayout;
    
    @FXML
    private ImageView team1ChosenImg;
    
    @FXML
    private ImageView team2ChosenImg;
    
    @FXML
    private ImageView saveMatchButtonImg;
    
    @FXML
    private ImageView posterImg;
    
    @FXML
    private Pane MatchSectionPane;
    
    @FXML
    private Pane ConcertSectionPane;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadSectionsFromDatabase();
        for (Section section : sections) {
            System.out.println("Section ID: " + section.getSectionId());
        }
        mainLayout.getChildren().remove(MatchForm);
        mainLayout.getChildren().remove(ConcertForm);
        //displaySections();
        
        
        
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        MatchHourSpinner.setValueFactory(hourFactory);
        MatchHourSpinner.getStyleClass().clear();

        // Initialize the minute spinner
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        MatchMinuteSpinner.setValueFactory(minuteFactory);
        MatchMinuteSpinner.getStyleClass().clear();
        
        ConcertHourSpinner.setValueFactory(hourFactory);
        ConcertHourSpinner.getStyleClass().clear();
        ConcertMinuteSpinner.setValueFactory(minuteFactory);
        ConcertMinuteSpinner.getStyleClass().clear();
        
        loadEventsFromDatabase();
        
    }    
    
    private List<Evenement> events = new ArrayList<>();
    
    private List<Section> sections = new ArrayList<>();
    private List<Section> selectedSections = new ArrayList<>();
    private Map<String, HBox> matchDisplayMap = new HashMap<>();
    private Map<String, HBox> concertDisplayMap = new HashMap<>();
    private final int maxColumns = 7; // Adjust as needed
    private Evenement selectedEvent = null;
    private int displaySwitch;
    //private Concert selectedConcert= null;
    
    // Section colors and borders
    private final Color SEC_COLOR = Color.web("#e6e6fa"); // Lavender
    private final Color SEL_SEC_COLOR = Color.BLUE;
    private final Color SEC_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SEC_BORDER_WIDTH = 0.5; // Replace 2.0 with your desired width
    private final StrokeType SEC_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";
    private static final String SELECT_ALL_SECTIONS = "SELECT * FROM stadeprojet.sections";

    public static void insertMatchToDatabase(Match match) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement
            String insertSql = "INSERT INTO matches (matchId,eventName, eventDate, eventTime, description, team1, team2, team1Logo, team2Logo) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setInt(1, match.getEventId());
            preparedStatement.setString(2, match.getEventName());
            preparedStatement.setDate(3, Date.valueOf(match.getEventDate()));
            preparedStatement.setString(4, match.getEventTime().toString());
            preparedStatement.setString(5, match.getDescription());
            preparedStatement.setString(6, match.getTeam1());
            preparedStatement.setString(7, match.getTeam2());
            preparedStatement.setString(8, match.getLogoTeam1());
            preparedStatement.setString(9, match.getLogoTeam2());

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            // Close the prepared statement and connection
            preparedStatement.close();
            
            // Prepare the SQL statement for inserting into the section_prices table
            String insertSectionPricesSql = "INSERT INTO match_section_prices (matchId, sectionId, price) VALUES (?, ?, ?)";
            PreparedStatement insertSectionPricesStatement = connection.prepareStatement(insertSectionPricesSql);

            // Loop through sectionPrices map and add entries to section_prices table
            Map<Integer, Double> sectionPrices = match.getSectionPrices();
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();
                double price = entry.getValue();

                insertSectionPricesStatement.setInt(1, match.getEventId());
                insertSectionPricesStatement.setInt(2, sectionId);
                insertSectionPricesStatement.setDouble(3, price);

                // Execute the SQL statement for inserting into the section_prices table
                insertSectionPricesStatement.executeUpdate();
            }
            // Close the connection

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public static void updateMatchToDatabase(Match match) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Update the match details in the matches table
            String updateMatchSql = "UPDATE matches SET eventName=?, eventDate=?, eventTime=?, description=?, team1=?, team2=?, team1Logo=?, team2Logo=? WHERE matchId=?";
            PreparedStatement updateMatchStatement = connection.prepareStatement(updateMatchSql);
            updateMatchStatement.setString(1, match.getEventName());
            updateMatchStatement.setDate(2, Date.valueOf(match.getEventDate()));
            updateMatchStatement.setString(3, match.getEventTime().toString());
            updateMatchStatement.setString(4, match.getDescription());
            updateMatchStatement.setString(5, match.getTeam1());
            updateMatchStatement.setString(6, match.getTeam2());
            updateMatchStatement.setString(7, match.getLogoTeam1());
            updateMatchStatement.setString(8, match.getLogoTeam2());
            updateMatchStatement.setInt(9, match.getEventId());

            // Execute the update statement for the matches table
            updateMatchStatement.executeUpdate();

            // Close the prepared statement
            updateMatchStatement.close();

            // Prepare the SQL statement for deleting entries from section_prices table
            String deleteSectionPricesSql = "DELETE FROM match_section_prices WHERE matchId=? AND sectionId=?";
            PreparedStatement deleteSectionPricesStatement = connection.prepareStatement(deleteSectionPricesSql);

            // Loop through existing entries in the section_prices table and delete the ones that don't exist in the map
            Map<Integer, Double> sectionPrices = match.getSectionPrices();
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();

                deleteSectionPricesStatement.setInt(1, match.getEventId());
                deleteSectionPricesStatement.setInt(2, sectionId);

                // Execute the delete statement for the section_prices table
                deleteSectionPricesStatement.executeUpdate();
            }

            // Close the prepared statement
            deleteSectionPricesStatement.close();

            // Prepare the SQL statement for inserting or updating entries in section_prices table
            String insertOrUpdateSectionPricesSql = "INSERT INTO match_section_prices (matchId, sectionId, price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE price=VALUES(price)";
            PreparedStatement insertOrUpdateSectionPricesStatement = connection.prepareStatement(insertOrUpdateSectionPricesSql);

            // Loop through sectionPrices map and add entries to section_prices table
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();
                double price = entry.getValue();

                insertOrUpdateSectionPricesStatement.setInt(1, match.getEventId());
                insertOrUpdateSectionPricesStatement.setInt(2, sectionId);
                insertOrUpdateSectionPricesStatement.setDouble(3, price);

                // Execute the insert or update statement for the section_prices table
                insertOrUpdateSectionPricesStatement.executeUpdate();
            }

            // Close the prepared statement
            insertOrUpdateSectionPricesStatement.close();

            // Close the connection
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    @FXML
    public void handleCreateMatchButton() {
        if (mainLayout.getChildren().contains(ConcertForm)) {
            mainLayout.getChildren().remove(ConcertForm);
        }
        
        if (mainLayout.getChildren().contains(MatchForm)) {
            mainLayout.getChildren().remove(MatchForm);
        } else {
            mainLayout.getChildren().add(1, MatchForm);
            initializeMatchForm();
            selectedEvent = null;
        }
        selectAllSections();
        
    }
    
    @FXML
    public void handleResetMatchForm() {
        // Reset the form fields to their default values
        initializeMatchForm();
        mainLayout.getChildren().remove(MatchForm);
    }
    
    private void initializeMatchForm() {
        // Set default values for date, time, description, team names, and logos
        datePickerMatch.setValue(LocalDate.now()); // You can set a default date
        MatchHourSpinner.getValueFactory().setValue(12); // Set default hour
        MatchMinuteSpinner.getValueFactory().setValue(0); // Set default minute
        descriptionTextAreaMatch.clear(); // Clear the description text area
        team1NameField.clear(); // Clear the team 1 name field
        team2NameField.clear(); // Clear the team 2 name field

        // Set default logos (replace "default_logo.png" with your actual default logo image)
        team1ChosenImg.setImage(new Image("/img/picture-2-128.png"));
        team2ChosenImg.setImage(new Image("/img/picture-2-128.png"));
        
        sectionDetailsGridMatch.getChildren().clear();
        displaySections(0);
        selectAllSections();
    }

    private void selectAllSections() {
        selectedSections.clear();
        selectedSections.addAll(sections);
        for (Section section : sections) {
            applyColorAndBorder(section.getSectionShape(), SEL_SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        }
        updateSectionGrid(sectionDetailsGridMatch);
        updateSectionGrid(sectionDetailsGridConcert);
        /*
        if (selectedEvent instanceof Match) {
            updateSectionGrid(sectionDetailsGridMatch);
        } else if (selectedEvent instanceof Concert) {
            updateSectionGrid(sectionDetailsGridConcert);
        }
        */
    }
    
    
    
    @FXML
    public void handleSaveMatchButton() {
        // Get data from the UI elements
        String team1Name = team1NameField.getText();
        String team2Name = team2NameField.getText();
        
        int selectedHour = MatchHourSpinner.getValue();
        int selectedMinute = MatchMinuteSpinner.getValue();
        LocalTime selectedTime = LocalTime.of(selectedHour, selectedMinute);
        // Other data...

        // Create a new match event
        int eventId = generateUniqueId();
        String eventName = "Match " + team1Name + " vs " + team2Name;
        LocalDate eventDate = datePickerMatch.getValue();
        Map<Integer, Double> sectionPrices = getSectionPricesFromUI(sectionDetailsGridMatch); // You might want to get this data from UI
        String description = descriptionTextAreaMatch.getText();
        String logoTeam1 = team1ChosenImg.getImage().getUrl();
        String logoTeam2 = team2ChosenImg.getImage().getUrl();
        
        Match match = new Match(eventId, eventName, eventDate,selectedTime, sectionPrices, description, team1Name, team2Name, logoTeam1, logoTeam2);

        if (selectedEvent == null) {
            // New match: Insert the match details
            insertMatchToDatabase(match);
        } else {
            match.setEventId(selectedEvent.getEventId());
            updateMatchToDatabase(match);
            events.remove(selectedEvent);
        }
        // Add the match to the events list
        events.add(match);
        Collections.sort(events, Collections.reverseOrder(Comparator.comparing(Evenement::getEventDate)));

        updateEventDisplays();

        // Print the match details (for testing purposes)
        System.out.println("New Match Event: " + match);
    }
    
    
    @FXML
    public void handleTeam1LogoButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Team 1 Logo");
        //Stage stage = (Stage) mainPane.getScene().getWindow(); // You can replace 'mainLayout' with the appropriate parent container
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Handle the selected file (e.g., set it as the logo for Team 1)
            System.out.println("Selected Team 1 Logo: " + selectedFile.getAbsolutePath());
            team1ChosenImg.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    public void handleTeam2LogoButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Team 2 Logo");
        //Stage stage = (Stage) mainPane.getScene().getWindow(); // You can replace 'mainLayout' with the appropriate parent container
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Handle the selected file (e.g., set it as the logo for Team 1)
            System.out.println("Selected Team 2 Logo: " + selectedFile.getAbsolutePath());
            team2ChosenImg.setImage(new Image(selectedFile.toURI().toString()));
        }
    }
    
    
    private void loadSectionsFromDatabase() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SECTIONS)) {
            

            while (resultSet.next()) {
                int id = resultSet.getInt("sectionId");
                String sectionName = resultSet.getString("sectionName");
                int shapeIndex = resultSet.getInt("shapeIndex");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double rotation = resultSet.getDouble("rotation");
                double scale = resultSet.getDouble("scale");

                Section section = new Section(id, sectionName, shapeIndex, x, y, rotation, scale);
                
                applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
                sections.add(section);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // 
        }
    }
    
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
            e.printStackTrace(); // Handle the exception appropriately
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

    private void saveMatchToDatabase(Match match) {
        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Insert match data
            String insertMatchQuery = "INSERT INTO matches (eventName, eventDate, eventTime, description, team1Name, team2Name, logoTeam1, logoTeam2) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertMatchQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, match.getEventName());
                preparedStatement.setDate(2, java.sql.Date.valueOf(match.getEventDate()));
                preparedStatement.setTime(3, java.sql.Time.valueOf(match.getEventTime()));
                preparedStatement.setString(4, match.getDescription());
                preparedStatement.setString(5, match.getTeam1());
                preparedStatement.setString(6, match.getTeam2());
                preparedStatement.setString(7, match.getLogoTeam1());
                preparedStatement.setString(8, match.getLogoTeam2());

                preparedStatement.executeUpdate();

                // Retrieve the generated matchId
                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int matchId = resultSet.getInt(1);

                    // Insert section prices
                    String insertSectionPricesQuery = "INSERT INTO match_section_prices (matchId, sectionId, price) VALUES (?, ?, ?)";
                    try (PreparedStatement sectionPricesStatement = connection.prepareStatement(insertSectionPricesQuery)) {
                        for (Map.Entry<Integer, Double> entry : match.getSectionPrices().entrySet()) {
                            sectionPricesStatement.setInt(1, matchId);
                            sectionPricesStatement.setInt(2, entry.getKey());
                            sectionPricesStatement.setDouble(3, entry.getValue());

                            sectionPricesStatement.executeUpdate();
                        }
                    }
                }
            }

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void displaySections(int paneType){
        Pane targetPane = (paneType == 0) ? MatchSectionPane : ConcertSectionPane;
        targetPane.getChildren().clear();
        for(Section section: sections){
            // Applique les transformations de rotation et de translation
            section.getSectionShape().getTransforms().clear();
            section.getSectionShape().getTransforms().add(new Rotate(section.getRotation(), section.getX() -680, section.getY()-350));
            section.getSectionShape().getTransforms().add(new Translate(section.getX()-680, section.getY()-350));
            
            section.getSectionShape().setOnMouseClicked(event -> {
                toggleSectionSelection(section);
            });

            
            targetPane.getChildren().add(section.getSectionShape());
            
            targetPane.setScaleX(0.3);
            targetPane.setScaleY(0.3);
            
            //section.getSectionShape().setScaleX(section.getScale());
            //section.getSectionShape().setScaleY(section.getScale());
            
            
        }
    }
    
    private void toggleSectionSelection(Section section) {
        if (selectedSections.contains(section)) {
            selectedSections.remove(section);
            applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        } else {
            selectedSections.add(section);
            applyColorAndBorder(section.getSectionShape(), SEL_SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        }
        updateSectionGrid(sectionDetailsGridMatch);
        updateSectionGrid(sectionDetailsGridConcert);
    }

    private void updateSectionGrid(GridPane sectionDetailsGrid) {
        sectionDetailsGrid.getChildren().clear();

        int columnIndex = 0;
        int rowIndex = 0;

        for (Section section : selectedSections) {
            HBox hbox = new HBox();
            hbox.getStyleClass().add("section-box"); // Add a style class for styling in CSS

            Label sectionNameLabel = new Label("S" + section.getSectionId());
            sectionNameLabel.getStyleClass().add("sectionName-style"); // Style class for section name label

            TextField sectionPriceTextField;
            if (selectedEvent != null) {
                // If there is a selected event, set the price from the sectionPrices map
                Double price = selectedEvent.getSectionPrices().getOrDefault(section.getSectionId(), 20.0);
                sectionPriceTextField = new TextField(String.valueOf(price));
            } else {
                // If there is no selected event, set the default price to "20$"
                sectionPriceTextField = new TextField("20$");
            }
            sectionPriceTextField.getStyleClass().add("sectionPrice-style");// Style class for section price TextField

            setLabelProperties(sectionNameLabel, 61); // Set your desired width
            setTextFieldProperties(sectionPriceTextField, 61,30); // Set your desired width

            hbox.getChildren().addAll(sectionNameLabel, sectionPriceTextField);
            sectionDetailsGrid.add(hbox, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex == maxColumns) {
                columnIndex = 0;
                rowIndex++;
            }
        }
    }
    
    private Map<Integer, Double> getSectionPricesFromUI(GridPane sectionDetailsGrid) {
        Map<Integer, Double> sectionPrices = new HashMap<>();

        for (Node node : sectionDetailsGrid.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;

                // Assuming the first child is the section name label and the second child is the price text field
                if (hbox.getChildren().size() >= 2) {
                    Node sectionNameNode = hbox.getChildren().get(0);
                    Node sectionPriceNode = hbox.getChildren().get(1);

                    if (sectionNameNode instanceof Label && sectionPriceNode instanceof TextField) {
                        Label sectionNameLabel = (Label) sectionNameNode;
                        TextField sectionPriceTextField = (TextField) sectionPriceNode;

                        // Extract section name (remove the 'S' prefix)
                        int sectionId = Integer.parseInt(sectionNameLabel.getText().substring(1));

                        // Extract section price (remove the '$' and convert to double)
                        double sectionPrice = Double.parseDouble(sectionPriceTextField.getText().replace("$", ""));

                        sectionPrices.put(sectionId, sectionPrice);
                    }
                }
            }
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
                //mainLayout.getChildren().add(eventDisplay);
            } else if (event instanceof Concert) {
                Concert concert = (Concert) event;
                eventDisplay = createDuplicateConcertDisplay(concert);
            }
            // Create a new display for the event
            

            System.out.println("Displaying an event");
            // Add the customized display to mainLayout
            mainLayout.getChildren().add(eventDisplay);
        }
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

        
        
        imageBox.setMargin(matchSymbol, new Insets(0, 0, 0, 10));
        VBox.setMargin(vsText, new Insets(0, 10, 0, 10));
        VBox.setMargin(team2Logo, new Insets(0, 10, 0, 10));
        System.out.println("logo2 image");
        

        Region region3 = new Region();
        region3.setLayoutX(100.0);
        region3.setLayoutY(25.0);
        region3.setPrefHeight(85.0);
        region3.setPrefWidth(93.0);

        VBox priceRangeBox = new VBox();
        priceRangeBox.setAlignment(Pos.CENTER);
        priceRangeBox.setPrefHeight(85.0);
        priceRangeBox.setPrefWidth(193.0);

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
        
        priceBoxLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));

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
        descriptionContentBox.setPrefWidth(963.0);

        Text descriptionText = new Text(event.getDescription());
        descriptionText.setId("MatchDescription");
        descriptionText.setFill(Color.NAVY);
        descriptionText.setStrokeType(OUTSIDE);
        descriptionText.setStrokeWidth(0.0);
        descriptionText.getStyleClass().add("Description-style");
        descriptionText.setWrappingWidth(799.4700317382812);
        
        HBox.setMargin(descriptionBox, new Insets(10, 0, 0, 10));

        //descriptionText.setPadding(new Insets(0, 10, 0, 10));
        descriptionContentBox.getChildren().add(descriptionText);
        
        
        
        Region region4 = new Region();
        region4.setPrefHeight(85.0);
        region4.setPrefWidth(80.0);

        VBox buttonsBox = new VBox();
        buttonsBox.setAlignment(Pos.TOP_RIGHT);
        buttonsBox.setPrefHeight(85.0);
        buttonsBox.setPrefWidth(310.0);

        HBox buttonsInnerBox = new HBox();
        buttonsInnerBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsInnerBox.setPrefHeight(135.0);
        buttonsInnerBox.setPrefWidth(310.0);

        Button editButton = new Button();
        editButton.setMnemonicParsing(false);
        editButton.setPrefHeight(70.0);
        editButton.setPrefWidth(70.0);
        editButton.getStyleClass().add("button-style");
       

        System.out.println("before save");
        ImageView editButtonIcon = new ImageView(new Image("/img/edit-128.png"));
        System.out.println("after save");
        editButtonIcon.setFitHeight(50.0);
        editButtonIcon.setFitWidth(50.0);
        editButtonIcon.setPickOnBounds(true);
        editButtonIcon.setPreserveRatio(true);

        editButton.setGraphic(editButtonIcon);

        Region region5 = new Region();
        region5.setPrefHeight(86.0);
        region5.setPrefWidth(22.0);

        Button deleteButton = new Button();
        /****************************************************************************************/
        String buttonId = String.valueOf(event.getEventId());
        editButton.setId("edit_" + buttonId);
        deleteButton.setId("delete_" + buttonId);

        // Add the MatchDisplay and its corresponding ID to the map
        matchDisplayMap.put(buttonId, duplicatedDisplay);
        /******************************************************************************************/
       
        
        deleteButton.setMnemonicParsing(false);
        deleteButton.setPrefHeight(70.0);
        deleteButton.setPrefWidth(70.0);
        deleteButton.getStyleClass().add("button-style");

        System.out.println("before delete");
        ImageView deleteButtonIcon = new ImageView(new Image("/img/delete-128.png"));
        System.out.println("after delete");
        deleteButtonIcon.setFitHeight(50.0);
        deleteButtonIcon.setFitWidth(50.0);
        deleteButtonIcon.setPickOnBounds(true);
        deleteButtonIcon.setPreserveRatio(true);
        System.out.println("before setting graphic");
        deleteButton.setGraphic(deleteButtonIcon);
        System.out.println("after setting graphic");
        
        VBox.setMargin(buttonsInnerBox, new Insets(0, 10, 0, 10));
        System.out.println("step 1");
        HBox.setMargin(editButton, new Insets(0, 0, 0, 10));
        System.out.println("step 2");
        HBox.setMargin(deleteButton, new Insets(0, 10, 0, 0));
        System.out.println("step 3");

        //deleteButton.setGraphic(deleteButtonIcon);
        

        buttonsInnerBox.getChildren().addAll(editButton, region5, deleteButton);
        System.out.println("step 4");
        buttonsBox.getChildren().add(buttonsInnerBox);
        System.out.println("step 5");

        

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
                priceRangeBox, rectangle, region31,dateAndDescriptionBox, region4, buttonsBox
        );
        System.out.println("step 9");

        deleteButton.setOnAction(this::handleDeleteMatchButtonClick);
        editButton.setOnAction(this::handleEditMatchButtonClick);

        //deleteButton.setOnAction(e -> handleDeleteButtonClick(e));

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
    
    @FXML
    private void handleEditMatchButtonClick(ActionEvent event) {
        System.out.println("Edit button clicked");

        // Get the button that triggered the event
        Button editButton = (Button) event.getSource();

        // Retrieve the unique ID set for the edit button
        String buttonId = editButton.getId();
        String eventId = buttonId.substring("edit_".length());

        // Retrieve the Match object associated with the ID from the map
        Match matchToEdit = (Match) events.stream()
                .filter(match -> String.valueOf(match.getEventId()).equals(eventId))
                .findFirst()
                .orElse(null);

        // Perform null check
        if (matchToEdit != null) {
            // Check if the form already exists in mainLayout
            selectedEvent = matchToEdit;
            System.out.println("editing match");
            if (mainLayout.getChildren().contains(MatchForm)) {
                // Bring the form to the front
                mainLayout.getChildren().remove(MatchForm);
                mainLayout.getChildren().add(1, MatchForm);
            } else {
                // Create and add the form to mainLayout
                mainLayout.getChildren().add(1, MatchForm);
            }
            mainLayout.getChildren().remove(ConcertForm);

            // Initialize the form with the details of the selected event
            sectionDetailsGridMatch.getChildren().clear();
            initializeMatchForm(matchToEdit);
        }
        System.out.println("starting the edit");
        
    }

    @FXML
    private void handleDeleteMatchButtonClick(ActionEvent event) {
        System.out.println("Delete button clicked");

        // Get the button that triggered the event
        Button deleteButton = (Button) event.getSource();

        // Retrieve the unique ID set for the delete button
        String buttonId = deleteButton.getId();
        String eventId = buttonId.substring("delete_".length());

        // Retrieve the MatchDisplay associated with the ID from the map
        HBox matchDisplay = matchDisplayMap.get(eventId);

        // Retrieve the Match object associated with the ID from the map
        Match matchToDelete = (Match) events.stream()
                .filter(match -> String.valueOf(match.getEventId()).equals(eventId))
                .findFirst()
                .orElse(null);

        // Perform null check and remove the Match object from the list of events
        if (matchToDelete != null) {
            events.remove(matchToDelete);
            deleteMatchFromDatabase(matchToDelete);


            // Remove the MatchDisplay from its parent (mainLayout in this case)
            mainLayout.getChildren().remove(matchDisplay);

            // Remove the MatchDisplay and its corresponding ID from the map
            matchDisplayMap.remove(buttonId);
        }
    }
    
    public static void deleteMatchFromDatabase(Match match) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for deleting from section_prices table
            String deleteSectionPricesSql = "DELETE FROM match_section_prices WHERE matchId = ?";
            PreparedStatement deleteSectionPricesStatement = connection.prepareStatement(deleteSectionPricesSql);
            deleteSectionPricesStatement.setInt(1, match.getEventId());

            // Execute the SQL statement for deleting from section_prices table
            deleteSectionPricesStatement.executeUpdate();

            // Close the prepared statement and connection
            deleteSectionPricesStatement.close();
            
            // Prepare the SQL statement for deleting from matches table
            String deleteMatchSql = "DELETE FROM matches WHERE matchId = ?";
            PreparedStatement deleteMatchStatement = connection.prepareStatement(deleteMatchSql);
            deleteMatchStatement.setInt(1, match.getEventId());

            // Execute the SQL statement for deleting from matches table
            deleteMatchStatement.executeUpdate();

            // Close the prepared statement
            deleteMatchStatement.close();

            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    public static void deleteConcertFromDatabase(Concert concert) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement for deleting from concert_section_prices table
            String deleteSectionPricesSql = "DELETE FROM concert_section_prices WHERE concertId = ?";
            PreparedStatement deleteSectionPricesStatement = connection.prepareStatement(deleteSectionPricesSql);
            deleteSectionPricesStatement.setInt(1, concert.getEventId());

            // Execute the SQL statement for deleting from concert_section_prices table
            deleteSectionPricesStatement.executeUpdate();

            // Close the prepared statement
            deleteSectionPricesStatement.close();

            // Prepare the SQL statement for deleting from concerts table
            String deleteConcertSql = "DELETE FROM concerts WHERE concertId = ?";
            PreparedStatement deleteConcertStatement = connection.prepareStatement(deleteConcertSql);
            deleteConcertStatement.setInt(1, concert.getEventId());

            // Execute the SQL statement for deleting from concerts table
            deleteConcertStatement.executeUpdate();

            // Close the prepared statement and connection
            deleteConcertStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    private void initializeMatchForm(Match match) {
        // Set values in the form based on the selected event
        datePickerMatch.setValue(match.getEventDate());
        MatchHourSpinner.getValueFactory().setValue(match.getEventTime().getHour());
        MatchMinuteSpinner.getValueFactory().setValue(match.getEventTime().getMinute());
        descriptionTextAreaMatch.setText(match.getDescription());
        team1NameField.setText(match.getTeam1());
        team2NameField.setText(match.getTeam2());
        team1ChosenImg.setImage(new Image(match.getLogoTeam1()));
        team2ChosenImg.setImage(new Image(match.getLogoTeam2()));

        // Clear the existing grid
        sectionDetailsGridMatch.getChildren().clear();
        selectedSections.clear();

        // Select sections and set prices based on the data in your map
        for (Map.Entry<Integer, Double> entry : match.getSectionPrices().entrySet()) {
            // Assuming you have a method to retrieve a Section based on its ID
            Section section = sections.stream()
                                      .filter(s -> s.getSectionId() == entry.getKey())
                                      .findFirst()
                                      .orElse(null);
            if (section != null) {
                selectedSections.add(section);
            }
        }

        for(Section section : selectedSections){
            applyColorAndBorder(section.getSectionShape(), SEL_SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        }        
        displaySections(0);
        updateSectionGrid(sectionDetailsGridMatch);
    }
    
    public static void insertConcertToDatabase(Concert concert) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Prepare the SQL statement
            String insertSql = "INSERT INTO concerts (concertId, concertName, concertDate, concertTime, concertDescription, posterImg, artists) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setInt(1, concert.getEventId());
            preparedStatement.setString(2, concert.getEventName());
            preparedStatement.setDate(3, Date.valueOf(concert.getEventDate()));
            preparedStatement.setString(4, concert.getEventTime().toString());
            preparedStatement.setString(5, concert.getDescription());
            preparedStatement.setString(6, concert.getPosterImagePath());
            preparedStatement.setString(7, String.join(",", concert.getArtists()));

            // Execute the SQL statement
            preparedStatement.executeUpdate();

            // Close the prepared statement and connection
            preparedStatement.close();

            // Prepare the SQL statement for inserting into the concert_section_prices table
            String insertSectionPricesSql = "INSERT INTO concert_section_prices (concertId, sectionId, price) VALUES (?, ?, ?)";
            PreparedStatement insertSectionPricesStatement = connection.prepareStatement(insertSectionPricesSql);

            // Loop through sectionPrices map and add entries to concert_section_prices table
            Map<Integer, Double> sectionPrices = concert.getSectionPrices();
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();
                double price = entry.getValue();

                insertSectionPricesStatement.setInt(1, concert.getEventId());
                insertSectionPricesStatement.setInt(2, sectionId);
                insertSectionPricesStatement.setDouble(3, price);

                // Execute the SQL statement for inserting into the concert_section_prices table
                insertSectionPricesStatement.executeUpdate();
            }

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    // *************** Concert Functions ********************** //
    @FXML
    public void handleCreateConcertButton() {
        // Remove the MatchForm if it exists
        if (mainLayout.getChildren().contains(MatchForm)) {
            mainLayout.getChildren().remove(MatchForm);
        }

        if (mainLayout.getChildren().contains(ConcertForm)) {
            mainLayout.getChildren().remove(ConcertForm);
        } else {
            mainLayout.getChildren().add(1, ConcertForm);
            initializeConcertForm();
            selectedEvent = null;
        }
        selectAllSections();
    }
    
    @FXML
    public void handleSaveConcertButton() {
        // Get data from the UI elements
        String eventName = EventNameField.getText();
        LocalDate eventDate = datePickerConcert.getValue();
        LocalTime eventTime = LocalTime.of(ConcertHourSpinner.getValue(), ConcertMinuteSpinner.getValue());
        Map<Integer, Double> sectionPrices = getSectionPricesFromUI(sectionDetailsGridConcert);
        String description = descriptionTextAreaConcert.getText();
        String posterImagePath = posterImg.getImage().getUrl();

        // Create a new concert event
        int eventId = generateUniqueId();
        Set<String> artists = getArtistsFromForm();

        Concert concert = new Concert(eventId, eventName, eventDate, eventTime, sectionPrices, description, artists, posterImagePath);

        if (selectedEvent == null) {
            insertConcertToDatabase(concert);
            System.out.println("starting to insert");
        } else {
            concert.setEventId(selectedEvent.getEventId());
            System.out.println("starting to update");
            updateConcertToDatabase(concert);
            events.remove(selectedEvent);
        }

        // Add the concert to the events list
        events.add(concert);
        Collections.sort(events, Collections.reverseOrder(Comparator.comparing(Evenement::getEventDate)));

        // Update the event displays in the UI
        updateEventDisplays();

        // Print the concert details (for testing purposes)
        System.out.println("New Concert Event: " + concert);
    }
    
    @FXML
    public void handleResetConcertForm() {
        // Reset the form fields to their default values
        initializeConcertForm();
        mainLayout.getChildren().remove(ConcertForm);
    }

    private void initializeConcertForm() {
        // Set default values for date, time, description, artists, and poster image
        datePickerConcert.setValue(LocalDate.now()); // You can set a default date
        ConcertHourSpinner.getValueFactory().setValue(12); // Set default hour
        ConcertMinuteSpinner.getValueFactory().setValue(0); // Set default minute
        descriptionTextAreaConcert.clear(); // Clear the description text area
        artistNameField.clear(); // Clear the artist text field
        EventNameField.clear();
        artistsTextFields.getChildren().clear(); // Clear the artists text fields
        artistsTextFields.getChildren().add(artistNameField);

        // Set default poster image (replace "default_poster.png" with your actual default poster image)
        posterImg.setImage(new Image("/img/picture-2-128.png"));

        sectionDetailsGridConcert.getChildren().clear();
        displaySections(1);
        selectAllSections(); // You might want to implement this method based on your application logic
    }
    
    @FXML
    public void handlePosterImageButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Concert Poster Image");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Handle the selected file (e.g., set it as the poster image for the concert)
            System.out.println("Selected Concert Poster Image: " + selectedFile.getAbsolutePath());
            posterImg.setImage(new Image(selectedFile.toURI().toString()));
        }
    }
    
    private Set<String> getArtistsFromForm() {
        Set<String> artists = new HashSet<>();

        for (Node node : artistsTextFields.getChildren()) {
            if (node instanceof TextField) {
                TextField artistTextField = (TextField) node;
                String artistName = artistTextField.getText().trim();

                if (!artistName.isEmpty()) {
                    artists.add(artistName);
                }
            }
        }

        return artists;
    }
    
    public static void updateConcertToDatabase(Concert concert) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Update the concert details in the concerts table
            String updateConcertSql = "UPDATE concerts SET concertName=?, concertDate=?, concertTime=?, concertDescription=?, posterImg=?, artists=? WHERE concertId=?";
            PreparedStatement updateConcertStatement = connection.prepareStatement(updateConcertSql);
            updateConcertStatement.setString(1, concert.getEventName());
            updateConcertStatement.setDate(2, Date.valueOf(concert.getEventDate()));
            updateConcertStatement.setString(3, concert.getEventTime().toString());
            updateConcertStatement.setString(4, concert.getDescription());
            updateConcertStatement.setString(5, concert.getPosterImagePath());
            updateConcertStatement.setString(6, String.join(",", concert.getArtists()));
            updateConcertStatement.setInt(7, concert.getEventId());

            // Execute the update statement for the concerts table
            updateConcertStatement.executeUpdate();

            // Close the prepared statement
            updateConcertStatement.close();

            // Prepare the SQL statement for deleting entries from concert_section_prices table
            String deleteSectionPricesSql = "DELETE FROM concert_section_prices WHERE concertId=? AND sectionId=?";
            PreparedStatement deleteSectionPricesStatement = connection.prepareStatement(deleteSectionPricesSql);

            // Loop through existing entries in the concert_section_prices table and delete the ones that don't exist in the map
            Map<Integer, Double> sectionPrices = concert.getSectionPrices();
            System.out.println("deleting started !");
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();

                deleteSectionPricesStatement.setInt(1, concert.getEventId());
                deleteSectionPricesStatement.setInt(2, sectionId);

                System.out.println("deleting section : " + sectionId + " du concert : " + concert.getEventId());
                // Execute the delete statement for the concert_section_prices table
                deleteSectionPricesStatement.executeUpdate();
            }

            // Close the prepared statement
            deleteSectionPricesStatement.close();

            // Prepare the SQL statement for inserting or updating entries in concert_section_prices table
            String insertOrUpdateSectionPricesSql = "INSERT INTO concert_section_prices (concertId, sectionId, price) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE price=VALUES(price)";
            PreparedStatement insertOrUpdateSectionPricesStatement = connection.prepareStatement(insertOrUpdateSectionPricesSql);

            // Loop through sectionPrices map and add entries to concert_section_prices table
            for (Map.Entry<Integer, Double> entry : sectionPrices.entrySet()) {
                int sectionId = entry.getKey();
                double price = entry.getValue();

                insertOrUpdateSectionPricesStatement.setInt(1, concert.getEventId());
                insertOrUpdateSectionPricesStatement.setInt(2, sectionId);
                insertOrUpdateSectionPricesStatement.setDouble(3, price);

                // Execute the insert or update statement for the concert_section_prices table
                insertOrUpdateSectionPricesStatement.executeUpdate();
            }

            // Close the prepared statement
            insertOrUpdateSectionPricesStatement.close();

            // Close the connection
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    
    private HBox createDuplicateConcertDisplay(Concert event) {
        System.out.println("Creating an event");
        HBox duplicatedDisplay = new HBox();
        
        VBox.setMargin(duplicatedDisplay, new Insets(20, 50, 0, 50));
        duplicatedDisplay.setPadding(new Insets(15, 0, 20, 0));
        
        duplicatedDisplay.getStyleClass().add("Event-display");
        //duplicatedDisplay.setAlignment(Pos.CENTER_LEFT);

        // Create and configure the elements based on the values of the Match event
        Region region = new Region();
        region.setPrefHeight(85.0);
        region.setPrefWidth(30.0);

        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.TOP_LEFT);
        imageBox.setPrefHeight(48.0);
        imageBox.setPrefWidth(60.0);
        

        System.out.println("football image before");
        ImageView concertSymbol = new ImageView(new Image("/img/guitar-128 (1).png"));
        System.out.println("football image after");
        concertSymbol.setFitHeight(60.0);
        concertSymbol.setFitWidth(60.0);
        concertSymbol.setPickOnBounds(true);
        concertSymbol.setPreserveRatio(true);
        imageBox.setMargin(concertSymbol, new Insets(0, 0, 0, 10));

        
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
        innerVBox.setPrefWidth(361.0);

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
        
        priceBoxLabel.setPadding(new Insets(5.0, 10.0, 5.0, 10.0));
        
        //priceRangeBox.getChildren().addAll(priceRangeTitle, priceRangeLabel);
        

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
        descriptionContentBox.setPrefWidth(963.0);

        Text descriptionText = new Text(event.getDescription());
        descriptionText.setId("MatchDescription");
        descriptionText.setFill(Color.NAVY);
        descriptionText.setStrokeType(OUTSIDE);
        descriptionText.setStrokeWidth(0.0);
        descriptionText.getStyleClass().add("Description-style");
        descriptionText.setWrappingWidth(799.4700317382812);
        
        HBox.setMargin(descriptionBox, new Insets(10, 0, 0, 10));

        //descriptionText.setPadding(new Insets(0, 10, 0, 10));
        descriptionContentBox.getChildren().add(descriptionText);
        
        
        
        Region region4 = new Region();
        region4.setPrefHeight(85.0);
        region4.setPrefWidth(80.0);

        VBox buttonsBox = new VBox();
        buttonsBox.setAlignment(Pos.TOP_RIGHT);
        buttonsBox.setPrefHeight(85.0);
        buttonsBox.setPrefWidth(310.0);

        HBox buttonsInnerBox = new HBox();
        buttonsInnerBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsInnerBox.setPrefHeight(135.0);
        buttonsInnerBox.setPrefWidth(310.0);

        Button editButton = new Button();
        editButton.setMnemonicParsing(false);
        editButton.setPrefHeight(70.0);
        editButton.setPrefWidth(70.0);
        editButton.getStyleClass().add("button-style");
       

        System.out.println("before save");
        ImageView editButtonIcon = new ImageView(new Image("/img/edit-128.png"));
        System.out.println("after save");
        editButtonIcon.setFitHeight(50.0);
        editButtonIcon.setFitWidth(50.0);
        editButtonIcon.setPickOnBounds(true);
        editButtonIcon.setPreserveRatio(true);

        editButton.setGraphic(editButtonIcon);

        Region region5 = new Region();
        region5.setPrefHeight(86.0);
        region5.setPrefWidth(22.0);

        Button deleteButton = new Button();
        /****************************************************************************************/
        String buttonId = String.valueOf(event.getEventId());
        editButton.setId("edit_" + buttonId);
        deleteButton.setId("delete_" + buttonId);

        // Add the MatchDisplay and its corresponding ID to the map
        concertDisplayMap.put(buttonId, duplicatedDisplay);
        /******************************************************************************************/
       
        
        deleteButton.setMnemonicParsing(false);
        deleteButton.setPrefHeight(70.0);
        deleteButton.setPrefWidth(70.0);
        deleteButton.getStyleClass().add("button-style");

        System.out.println("before delete");
        ImageView deleteButtonIcon = new ImageView(new Image("/img/delete-128.png"));
        System.out.println("after delete");
        deleteButtonIcon.setFitHeight(50.0);
        deleteButtonIcon.setFitWidth(50.0);
        deleteButtonIcon.setPickOnBounds(true);
        deleteButtonIcon.setPreserveRatio(true);
        System.out.println("before setting graphic");
        deleteButton.setGraphic(deleteButtonIcon);
        System.out.println("after setting graphic");
        
        VBox.setMargin(buttonsInnerBox, new Insets(0, 10, 0, 10));
        System.out.println("step 1");
        HBox.setMargin(editButton, new Insets(0, 0, 0, 10));
        System.out.println("step 2");
        HBox.setMargin(deleteButton, new Insets(0, 10, 0, 0));
        System.out.println("step 3");

        //deleteButton.setGraphic(deleteButtonIcon);
        

        buttonsInnerBox.getChildren().addAll(editButton, region5, deleteButton);
        System.out.println("step 4");
        buttonsBox.getChildren().add(buttonsInnerBox);
        System.out.println("step 5");

        

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
                innerVBox, rectangle, region31,dateAndDescriptionBox, region4, buttonsBox
        );
        System.out.println("step 9");

        deleteButton.setOnAction(this::handleDeleteConcertButtonClick);
        editButton.setOnAction(this::handleEditConcertButtonClick);

        //deleteButton.setOnAction(e -> handleDeleteButtonClick(e));

        return duplicatedDisplay;
    }
    
    private void initializeConcertForm(Concert concert) {
        // Set values in the form based on the selected concert
        datePickerConcert.setValue(concert.getEventDate());
        ConcertHourSpinner.getValueFactory().setValue(concert.getEventTime().getHour());
        ConcertMinuteSpinner.getValueFactory().setValue(concert.getEventTime().getMinute());
        descriptionTextAreaConcert.setText(concert.getDescription());
        EventNameField.setText(concert.getEventName());

        // Set artists in the artists text fields
        Set<String> artists = concert.getArtists();
        for (int i = 0; i < artistsTextFields.getChildren().size(); i++) {
            TextField artistTextField = (TextField) artistsTextFields.getChildren().get(i);
            if (i < artists.size()) {
                artistTextField.setText(artists.toArray(new String[0])[i]);
            } else {
                artistTextField.clear();
            }
        }

        // Set poster image
        posterImg.setImage(new Image(concert.getPosterImagePath()));

        // Clear the existing grid
        sectionDetailsGridConcert.getChildren().clear();
        selectedSections.clear();

        // Select sections and set prices based on the data in your map
        for (Map.Entry<Integer, Double> entry : concert.getSectionPrices().entrySet()) {
            // Assuming you have a method to retrieve a Section based on its ID
            Section section = sections.stream()
                                      .filter(s -> s.getSectionId() == entry.getKey())
                                      .findFirst()
                                      .orElse(null);
            if (section != null) {
                selectedSections.add(section);
            }
        }
        for(Section section : selectedSections){
            applyColorAndBorder(section.getSectionShape(), SEL_SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        }
        
        displaySections(1);
        updateSectionGrid(sectionDetailsGridConcert);
    }
    
    @FXML
    private void handleDeleteConcertButtonClick(ActionEvent event) {
        System.out.println("Delete button clicked");

        // Get the button that triggered the event
        Button deleteButton = (Button) event.getSource();

        // Retrieve the unique ID set for the delete button
        String buttonId = deleteButton.getId();
        String eventId = buttonId.substring("delete_".length());

        // Retrieve the ConcertDisplay associated with the ID from the map
        HBox concertDisplay = concertDisplayMap.get(eventId);

        // Retrieve the Concert object associated with the ID from the map
        Concert concertToDelete = (Concert) events.stream()
                .filter(concert -> String.valueOf(concert.getEventId()).equals(eventId))
                .findFirst()
                .orElse(null);

        // Perform null check and remove the Concert object from the list of events
        if (concertToDelete != null) {
            events.remove(concertToDelete);
            deleteConcertFromDatabase(concertToDelete);

            // Remove the ConcertDisplay from its parent (mainLayout in this case)
            mainLayout.getChildren().remove(concertDisplay);

            // Remove the ConcertDisplay and its corresponding ID from the map
            concertDisplayMap.remove(buttonId);
        }
    }
    
    @FXML
    private void handleEditConcertButtonClick(ActionEvent event) {
        System.out.println("Edit button clicked");

        // Get the button that triggered the event
        Button editButton = (Button) event.getSource();

        // Retrieve the unique ID set for the edit button
        String buttonId = editButton.getId();
        String eventId = buttonId.substring("edit_".length());
        System.out.println("editing concert 0" + eventId);

        // Retrieve the Concert object associated with the ID from the map
        Concert concertToEdit = (Concert) events.stream()
                .filter(concert -> String.valueOf(concert.getEventId()).equals(eventId))
                .findFirst()
                .orElse(null);

        System.out.println("editing concert 0");
        // Perform null check
        if (concertToEdit != null) {
            // Check if the form already exists in mainLayout
            System.out.println("editing concert 1");
            selectedEvent = concertToEdit;
            System.out.println("editing concert 2");
            if (mainLayout.getChildren().contains(ConcertForm)) {
                // Bring the form to the front
                mainLayout.getChildren().remove(ConcertForm);
                mainLayout.getChildren().add(1, ConcertForm);
            } else {
                // Create and add the form to mainLayout
                mainLayout.getChildren().add(1, ConcertForm);
            }
            mainLayout.getChildren().remove(MatchForm);

            // Initialize the form with the details of the selected event
            sectionDetailsGridConcert.getChildren().clear();
            initializeConcertForm(concertToEdit);
        }
        System.out.println("starting the edit");
    }
    
    @FXML
    private void handleAddArtistButtonClick() {
        // Create a new instance of the artistTextField and clone properties
        TextField newArtistTextField = new TextField(artistNameField.getText());

        // Copy properties or styles if needed
        newArtistTextField.setPromptText(artistNameField.getPromptText());
        newArtistTextField.setStyle(artistNameField.getStyle());
        // ... other properties or styles you want to copy

        // Add the new artistTextField to the artistsTextFields VBox
        artistsTextFields.getChildren().add(newArtistTextField);
    }
    
    
    @FXML
    private void handleStade(MouseEvent event) {
        
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/StadeBuilder.fxml"));
                Parent root = loader.load();
                StadeBuilderController nextController = loader.getController();

                // Show the new scene
                Stage stage = (Stage) MenuStade.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        
    }
    //extrat --------------------------
    public void applyColorAndBorder(Shape shape, Color fillColor, Color borderColor, double borderWidth, StrokeType strokeType) {
        shape.setFill(fillColor);
        shape.setStroke(borderColor);
        shape.setStrokeWidth(borderWidth);
        shape.setStrokeType(strokeType);
    }
    
    private void setLabelProperties(Label label, double width) {
        label.setMinWidth(width);
        label.setPrefWidth(width);
        label.setMaxWidth(width);
        label.setAlignment(Pos.CENTER);
    }

    private void setTextFieldProperties(TextField textField, double width,double height) {
        textField.setMinWidth(width);
        textField.setPrefWidth(width);
        textField.setMaxWidth(width);
        textField.setMinHeight(height);
        textField.setPrefHeight(height);
        textField.setMinHeight(height);
        textField.setAlignment(Pos.CENTER); // Center alignment
    }
    
    private int generateUniqueId() {
        int newId = 1;
        List<Integer> existingIds = events.stream().map(Evenement::getEventId).toList();
        while (existingIds.contains(newId)) {
            newId++;
        }
        return newId;
    }
}
