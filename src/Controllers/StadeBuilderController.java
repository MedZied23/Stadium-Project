/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import Modules.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

import java.io.*;
import javafx.fxml.FXML;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.Statement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.shape.StrokeType;

import java.util.Map;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
/**
 * FXML Controller class
 *
 * @author Ayoub Zerdoum
 */
public class StadeBuilderController implements Initializable {
    
    @FXML
    private TextField sectionXField;
    @FXML
    private TextField sectionYField;
    @FXML
    private TextField sectionRotationField;
    @FXML
    private TextField sectionShapeField;
    @FXML
    private TextField sectionScaleField;
    
    @FXML
    private TextField sectionIdField;
    
    @FXML
    private TextField sectionNameField;

    // Siege Form TextFields
    @FXML
    private TextField siegeIdField;
    @FXML
    private TextField siegeNumField;
    @FXML
    private TextField siegeSectionIdField;
    @FXML
    private TextField siegeXField;
    @FXML
    private TextField siegeYField;
    @FXML
    private TextField siegeRadiusField;
    @FXML
    private TextField siegeScaleField;
    @FXML
    private TextField SectionIdField;
    @FXML
    private TextField SectionNameField;
    
    @FXML
    private Button createNewSiegeButton;
    @FXML
    private Button saveSiegeButton;
    @FXML
    private Button deleteSiegeButton;
    @FXML
    private Button copySiegeButton;
    
    @FXML
    private Button createNewSectionButton;
    @FXML
    private Button saveSectionButton;
    @FXML
    private Button deleteSectionButton;
    @FXML
    private Button copySectionButton;
    
    @FXML
    private Button saveChangesButton;
    
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    
    @FXML
    private ImageView MenuStade;
    
    @FXML
    private ImageView MenuEvents;
    
    
    @FXML
    private ScrollPane StadePane;
    
    @FXML
    private Pane StadeLayout;
    
    @FXML
    private Pane TroublePane;
    
    private static final String SELECT_ALL_SECTIONS = "SELECT * FROM stadeprojet.sections";
    
    
    private List<Section> sections = new ArrayList<>();
    private List<Group> sectionGroups = new ArrayList<>();
    
    // Section colors and borders
    private final Color SEC_COLOR = Color.web("#e6e6fa"); // Lavender
    private final Color SEL_SEC_COLOR = Color.PURPLE;
    private final Color SEC_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SEC_BORDER_WIDTH = 0.5; // Replace 2.0 with your desired width
    private final StrokeType SEC_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed

    // Siege colors and borders
    private final Color SIG_COLOR = Color.WHITE;
    private final Color SEL_SIG_COLOR = Color.GREEN;
    private final Color SIG_BORDER_COLOR = Color.web("#a09db2"); // Slate Gray
    private final double SIG_BORDER_WIDTH = 0.2; // Replace 2.0 with your desired width
    private final StrokeType SIG_BORDER_TYPE = StrokeType.OUTSIDE; // or INSIDE if needed
    
    // JDBC URL, username, and password of MySQL server
    private static final String URL = "jdbc:mysql://localhost:3306/stadeprojet";
    private static final String USER = "root";
    private static final String PASSWORD = "***";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createNewSectionButton.setOnAction(event -> createNewSection());
        saveSectionButton.setOnAction(event -> saveCurrentSection());
        deleteSectionButton.setOnAction(event -> deleteSection());
        copySectionButton.setOnAction(event -> copySection());
        
        createNewSiegeButton.setOnAction(event -> createNewSiege());
        saveSiegeButton.setOnAction(event -> saveCurrentSiege());
        deleteSiegeButton.setOnAction(event -> deleteSiege());
        copySiegeButton.setOnAction(event -> copySiege());
        
        saveChangesButton.setOnAction(event -> saveChangesToDatabase());
        
        
        loadSectionsFromDatabase();

        StadeLayout.setPrefWidth(1336);
        StadeLayout.setPrefHeight(738);
        
    }
    
    
    
    public void saveChangesToDatabase() {
        deleteSectionsNotInList(sections);
        for (Section section : sections) {
            saveSectionToDatabase(section);
        }
        System.out.println("Changes saved to the database.");
    }
    
    public void deleteSectionsNotInList(List<Section> sections) {
        // Extract section IDs from the provided list
        List<Integer> sectionIds = sections.stream()
                                           .map(Section::getSectionId)
                                           .collect(Collectors.toList());

        // Create a comma-separated string of section IDs to use in the SQL query
        String sectionIdsString = sectionIds.stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(","));

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Delete sieges associated with the sections not in the provided list
            String deleteSiegesQuery = "DELETE FROM Sieges WHERE sectionId NOT IN (" + sectionIdsString + ")";
            statement.executeUpdate(deleteSiegesQuery);

            // Delete sections from the database that are not in the provided list
            String deleteSectionsQuery = "DELETE FROM Sections WHERE sectionId NOT IN (" + sectionIdsString + ")";
            statement.executeUpdate(deleteSectionsQuery);

            System.out.println("Sections and associated sieges deleted from the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void saveSiegeToDatabase(Siege siege) {
    String sql = "INSERT INTO Sieges (siegeId, numSiege, sectionId, radius, scale, x, y) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE\n" +
            "    numSiege=VALUES(numSiege),\n" +
            "    sectionId=VALUES(sectionId),\n" +
            "    radius=VALUES(radius),\n" +
            "    scale=VALUES(scale),\n" +
            "    x=VALUES(x),\n" +
            "    y=VALUES(y)";

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        // Set values for the parameters in the SQL query
        preparedStatement.setInt(1, siege.getSiegeId());
        preparedStatement.setInt(2, siege.getNumSiege());
        preparedStatement.setInt(3, siege.getSectionId());
        preparedStatement.setDouble(4, siege.getRadius());
        preparedStatement.setDouble(5, siege.getScale());
        preparedStatement.setDouble(6, siege.getX());
        preparedStatement.setDouble(7, siege.getY());

        // Execute the query
        preparedStatement.executeUpdate();

        System.out.println("Siege added to the database: " + siege.getSiegeId());

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void saveSectionToDatabase(Section section) {
        String sql = "INSERT INTO Sections (sectionId, sectionName, shapeIndex, x, y, rotation, scale) VALUES (?, ?, ?, ?, ?, ?, ?)"
                + "ON DUPLICATE KEY UPDATE\n" +
                "    sectionName=VALUES(sectionName),\n" +
                "    shapeIndex=VALUES(shapeIndex),\n" +
                "    x=VALUES(x),\n" +
                "    y=VALUES(y),\n" +
                "    rotation=VALUES(rotation),\n" +
                "    scale=VALUES(scale)";


        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set values for the parameters in the SQL query
            preparedStatement.setInt(1, section.getSectionId());
            preparedStatement.setString(2, section.getSectionName());
            preparedStatement.setInt(3, section.getShapeIndex());
            preparedStatement.setDouble(4, section.getX());
            preparedStatement.setDouble(5, section.getY());
            preparedStatement.setDouble(6, section.getRotation());
            preparedStatement.setDouble(7, section.getScale());

            // Execute the query
            preparedStatement.executeUpdate();
            
            // Get a list of siegeIds associated with the current section
            List<Integer> siegeIds = section.getSieges().stream()
                    .map(Siege::getSiegeId)
                    .collect(Collectors.toList());
            
            
            // Create a comma-separated string of section IDs to use in the SQL query
            String siegesIdsString = siegeIds.stream()
                                                .map(Object::toString)
                                                .collect(Collectors.joining(","));
            
            String deleteSiegesSQL;
            if (section.hasSieges()) {
                deleteSiegesSQL = "DELETE FROM Sieges WHERE sectionId = ? AND siegeId NOT IN (" + siegesIdsString + ")";
            } else {
                deleteSiegesSQL = "DELETE FROM Sieges WHERE sectionId = ?";
            }

            
            //String deleteSiegesSQL = "DELETE FROM Sieges WHERE sectionId = ? AND siegeId NOT IN ("+ siegesIdsString +")";
            PreparedStatement deleteSiegesStatement = connection.prepareStatement(deleteSiegesSQL);
            deleteSiegesStatement.setInt(1, section.getSectionId());
            deleteSiegesStatement.executeUpdate();

            System.out.println("Section added to the database: " + section.getSectionName());
            
            for (Siege siege : section.getSieges()) {
                saveSiegeToDatabase(siege);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                section.getSectionShape().setOnMouseClicked(event -> selectSection(section));
                applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
                //sectionGroup.getChildren().add(section.getSectionShape());
                sections.add(section);
                sectionGroups.add(sectionGroup);

                // Update the transformations
                // Scale the section shape
                section.getSectionShape().setScaleX(section.getScale());
                section.getSectionShape().setScaleY(section.getScale());

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
            }
        } catch (SQLException e) {
            e.printStackTrace(); //
        }
    }
    
    private void refreshStadeLayout(List<Section> sections) {
        // Clear existing sections in StadeLayout
        //StadeLayout.getChildren().clear();
        StadeLayout.getChildren().removeIf(node -> node instanceof Group);

        // Add sections to StadeLayout
        for (Section section : sections) {
            Group sectionGroup = new Group();
            sectionGroup.setOnMouseClicked(event -> selectSection(section));
            applyColorAndBorder(section.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
            sectionGroup.getChildren().add(section.getSectionShape());
            updateTransformations(sectionGroup, section);
            sectionGroups.add(sectionGroup);
            
            StadeLayout.getChildren().add(sectionGroup);
        }
        System.out.println("layout refreshed selected");
    }
    

    private Section currentSection;
    
    public void createNewSection() {
        System.out.println("New Section created");
        int shapeIndex = Integer.parseInt(sectionShapeField.getText());
        if (shapeIndex < 0 || shapeIndex >= Section.sectionShapes.length) {
            return;
        }
        
        //int sectionId = Integer.parseInt(sectionIdField.getText());
        double scale = Double.parseDouble(sectionScaleField.getText());

        Section newSection = new Section(sections.size()+10, "New S", shapeIndex, 0, 0, 0, scale);
        sections.add(newSection);
        Group sectionGroup = new Group();
        newSection.getSectionShape().setOnMouseClicked(event -> selectSection(newSection));
        sectionGroups.add(sectionGroup);
        sectionGroup.getChildren().add(newSection.getSectionShape());
        selectSection(newSection);
        updateTransformations(sectionGroup,newSection);
        
        StadeLayout.getChildren().add(sectionGroup);

    }
    
    public void saveCurrentSection() {
        if (currentSection != null) {
            // Update the section attributes with the values from the text fields
            currentSection.setSectionId(Integer.parseInt(sectionIdField.getText()));
            currentSection.setSectionName(sectionNameField.getText());
            currentSection.setX(Double.parseDouble(sectionXField.getText()));
            currentSection.setY(Double.parseDouble(sectionYField.getText()));
            currentSection.setRotation(Double.parseDouble(sectionRotationField.getText()));

            currentSection.setScale(Double.parseDouble(sectionScaleField.getText()));

            // Set the shape index
            currentSection.setShapeByIndex(Integer.parseInt(sectionShapeField.getText()));
            int newSectionIdPart = currentSection.getSectionId() * 1000;
            int originalId;
            for (Siege siege : currentSection.getSieges()) {
                originalId = siege.getSiegeId() % 1000; // Extract the original section ID part
                System.out.println("original id : "+ originalId);
                System.out.println("original id modified: "+ originalId% 1000);
                siege.setSiegeId(newSectionIdPart + originalId );
            }
            // Update the transformations for the section
            int index = sections.indexOf(currentSection);
            System.out.println("Index: " + index);
            if (index >= 0 && index < sectionGroups.size()) {
                Group currentSectionGroup = sectionGroups.get(index);
                updateTransformations(currentSectionGroup,currentSection);
                System.out.println("Change in progress ");
            }
            System.out.println("Changes saved ");
        }
    }
    
    public void deleteSection() {
        if (currentSection != null) {
            int index = sections.indexOf(currentSection);
            if (index >= 0 && index < sectionGroups.size()) {
                Group sectionGroup = sectionGroups.get(index);
                StadeLayout.getChildren().remove(sectionGroup);
                sectionGroups.remove(sectionGroup);
                sections.remove(currentSection);
                currentSection = null;
                
            }
        }
    }
    
    public void copySection() {
        if (currentSection != null) {
            Section copiedSection = new Section(
                sections.size()+10, 
                currentSection.getSectionName() + " C",
                currentSection.getShapeIndex(),
                currentSection.getX() + 20, 
                currentSection.getY() + 20,
                currentSection.getRotation(),
                currentSection.getScale()
            );

            sections.add(copiedSection);
            
            Group copiedSectionGroup = new Group();
            copiedSectionGroup.getChildren().add(copiedSection.getSectionShape());
            
            copiedSection.getSectionShape().setOnMouseClicked(event -> {
                selectSection(copiedSection);
            });
            
            updateTransformations(copiedSectionGroup,copiedSection);

            sectionGroups.add(copiedSectionGroup);
            StadeLayout.getChildren().add(copiedSectionGroup);
            
            // Copy sieges within the section using existing method
            for (Siege originalSiege : currentSection.getSieges()) {
                copySiege2(originalSiege,copiedSection);
            }

            
            selectSection(copiedSection);
        }
    }
    
    
    
    
    // Méthode pour mettre à jour les transformations de rotation et de translation
    private void updateTransformations(Group sectionGroup,Section section) {
        // Efface toutes les transformations et éléments existants du groupe
        sectionGroup.getChildren().clear();
        sectionGroup.getTransforms().clear();
        
        // Scale the section shape
        section.getSectionShape().setScaleX(section.getScale());
        section.getSectionShape().setScaleY(section.getScale());

        // Ajoute la forme de section
        sectionGroup.getChildren().add(section.getSectionShape());

        // Ajoute les sièges
        for (Siege siege : section.getSieges()) {
            System.out.println("Siege X: " + siege.getCenterX() + ", Siege Y: " + siege.getCenterY());
            sectionGroup.getChildren().add(siege);
        }

        // Applique les transformations de rotation et de translation
        sectionGroup.getTransforms().add(new Rotate(section.getRotation(), section.getX(), section.getY()));
        //sectionGroup.getTransforms().add(new Rotate(section.getRotation()));
        sectionGroup.getTransforms().add(new Translate(section.getX(), section.getY()));
        
        updatePaneSize(section);
        
    }
    
    private void updatePaneSize(Section section) {
        double x = section.getX();
        double y = section.getY();
        //double shapeWidth = section.getSectionShape().getBoundsInParent().getWidth();
        //double shapeHeight = section.getSectionShape().getBoundsInParent().getHeight();

        // Calculate the maximum X and Y values
        double maxX = x + 500;
        double maxY = y + 500;

        // Get the current width and height of the StadeLayout
        double currentWidth = StadeLayout.getWidth();
        double currentHeight = StadeLayout.getHeight();

        // Check if maxX or maxY exceed the current width or height
        if (maxX > currentWidth) {
            StadeLayout.setPrefWidth(maxX);
        }

        if (maxY > currentHeight) {
            StadeLayout.setPrefHeight(maxY);
        }
    }
    
    //*********************** Siege manipulation Methodes **************************//
    private Siege currentSiege;
    
    // Create a new siege
    public void createNewSiege() {
        
        siegeIdField.setText(String.valueOf(currentSection.getSectionId() * 1000 + (currentSection.getSieges().size() + 1)));
        siegeNumField.setText(String.valueOf(currentSection.sieges.size()+1));
        siegeSectionIdField.setText(String.valueOf(currentSection.getSectionId()));
        siegeXField.setText(String.valueOf(0));
        siegeYField.setText(String.valueOf(0));
        siegeRadiusField.setText(String.valueOf(Siege.getStaticRadius()));
        siegeScaleField.setText(String.valueOf(Siege.getScale()));
        
        int siegeId = Integer.parseInt(siegeIdField.getText());
        int sectionId = Integer.parseInt(siegeSectionIdField.getText());
        int NumSiege = Integer.parseInt(siegeNumField.getText());
        

        
        Siege newSiege = new Siege(siegeId, NumSiege, sectionId, "available", 0, 0);

        currentSection.sieges.add(newSiege);
        currentSiege = newSiege;
        applyColorAndBorder(currentSiege, SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
        newSiege.setOnMouseClicked(event -> selectSiege(newSiege));
        int index = sections.indexOf(currentSection);
            if (index >= 0 && index < sectionGroups.size()) {
                Group sectionGroup = sectionGroups.get(index);
                sectionGroup.getChildren().add(newSiege);          
            }
    }
    
    // Save the current siege
    public void saveCurrentSiege() {
        if (currentSiege != null) {
            // Update the siege attributes with the values from the text fields
            int siegeId = Integer.parseInt(siegeIdField.getText());
            int siegeNumber = Integer.parseInt(siegeNumField.getText());
            int sectionId = Integer.parseInt(siegeSectionIdField.getText());
            String status = "available";
            double x = Double.parseDouble(siegeXField.getText());
            double y = Double.parseDouble(siegeYField.getText());
            double scale = Double.parseDouble(siegeScaleField.getText()); 
            double radius = Double.parseDouble(siegeRadiusField.getText()); // Get the radius input

            currentSiege.setSiegeId(siegeId);
            currentSiege.setSiegeNumber(siegeNumber);
            currentSiege.setSectionId(sectionId);
            currentSiege.setStatus(status);
            currentSiege.setX(x);
            currentSiege.setY(y);
            currentSiege.setScaleAll(scale,sections); // Set the scale
            Siege.setStaticRadius(radius,sections);
            currentSiege.setRadius(radius);// Set the radius
        }
    }
    
    // Delete the current siege
    public void deleteSiege() {
        if (currentSiege != null) {
            int index = sections.indexOf(currentSection);
            if (index >= 0 && index < sectionGroups.size()) {
                Group sectionGroup = sectionGroups.get(index);
                sectionGroup.getChildren().remove(currentSiege);
                
            }
            currentSection.sieges.remove(currentSiege);
        }
    }

    // Copy the current siege
    public void copySiege() {
        if (currentSiege != null && currentSection != null) {
            copySiege(currentSiege, currentSection);
        }
    }
    
    // Copy a specific siege
    public void copySiege(Siege originalSiege, Section copiedSection) {
        double x = originalSiege.getCenterX();
        double y = originalSiege.getCenterY();
        if(copiedSection.getSectionId() == currentSection.getSectionId()){
            x = x + 10;
            y = y + 10;
        }
       
        Siege copiedSiege = new Siege(
            currentSection.getSectionId() * 1000 + (copiedSection.getSieges().size() + 1),
            currentSection.getSieges().size() + 1,
            copiedSection.getSectionId(),
            originalSiege.getStatus(),
            x,
            y  
        );

        applyColorAndBorder(copiedSiege, SEL_SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
        selectSiege(copiedSiege);

        copiedSiege.setOnMouseClicked(event -> selectSiege(copiedSiege));
         
        copiedSection.addSiege(copiedSiege);
        int index = sections.indexOf(copiedSection);
        if (index >= 0 && index < sectionGroups.size()) {
            Group sectionGroup = sectionGroups.get(index);
            sectionGroup.getChildren().add(copiedSiege);
        }
    }
    
    // Copy a specific siege
    public void copySiege2(Siege originalSiege, Section copiedSection) {
        double x = originalSiege.getCenterX();
        double y = originalSiege.getCenterY();
       
        Siege copiedSiege = new Siege(
            copiedSection.getSectionId() * 1000 + (copiedSection.getSieges().size() + 1),
            copiedSection.getSieges().size() + 1,
            copiedSection.getSectionId(),
            originalSiege.getStatus(),
            x,
            y 
        );

        //copiedSiege.setFill(Color.LIGHTBLUE);
        applyColorAndBorder(copiedSiege, SEL_SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
        //selectSiege(copiedSiege);

        copiedSiege.setOnMouseClicked(event -> selectSiege(copiedSiege));
         
        copiedSection.addSiege(copiedSiege);
        int index = sections.indexOf(copiedSection);
        if (index >= 0 && index < sectionGroups.size()) {
            Group sectionGroup = sectionGroups.get(index);
            sectionGroup.getChildren().add(copiedSiege);
        }
    }

    private void selectSiege(Siege siege, boolean triggerSectionSelection) {
        for (Section section : sections) {
            for (Siege otherSiege : section.getSieges()) {
                if (otherSiege != siege) {
                    applyColorAndBorder(otherSiege, SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
                }
            }
        }
        
        if (currentSiege != null) {
            applyColorAndBorder(currentSiege, SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
        }
        currentSiege = siege;
        applyColorAndBorder(currentSiege, SEL_SIG_COLOR, SIG_BORDER_COLOR, SIG_BORDER_WIDTH, SIG_BORDER_TYPE);
        siegeIdField.setText(Integer.toString(currentSiege.getSiegeId()));
        siegeNumField.setText(Integer.toString(currentSiege.getNumSiege()));
        siegeSectionIdField.setText(Integer.toString(currentSiege.getSectionId()));
        siegeXField.setText(Double.toString(currentSiege.getCenterX()));
        siegeYField.setText(Double.toString(currentSiege.getCenterY()));

        // Highlight the corresponding section if triggerSectionSelection is true
        if (triggerSectionSelection) {
            int sectionId = currentSiege.getSectionId();
            for (Section section : sections) {
                if (section.getSectionId() == sectionId) {
                    selectSection(section, false); 
                    break; 
                }
            }
        }

        System.out.println("Siege " + currentSiege.getSiegeId() + " is selected!");
    }
    
    private void selectSection(Section section, boolean triggerSiegeSelection) {
        if (currentSection != null) {
            applyColorAndBorder(currentSection.getSectionShape(), SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);
        }
        currentSection = section;
        applyColorAndBorder(currentSection.getSectionShape(), SEL_SEC_COLOR, SEC_BORDER_COLOR, SEC_BORDER_WIDTH, SEC_BORDER_TYPE);


        sectionIdField.setText(Integer.toString(currentSection.getSectionId()));
        sectionNameField.setText(currentSection.getSectionName());
        sectionXField.setText(Double.toString(currentSection.getX()));
        sectionYField.setText(Double.toString(currentSection.getY()));
        sectionRotationField.setText(Double.toString(currentSection.getRotation()));
        sectionShapeField.setText(Integer.toString(currentSection.getShapeIndex())); 
        sectionScaleField.setText(Double.toString(currentSection.getScale())); 
        if (currentSection.hasSieges() && triggerSiegeSelection) {
            Siege firstSiege = currentSection.getSiegeByNumber(0);
            selectSiege(firstSiege, false); 
        }
        System.out.println("Section " + currentSection.getSectionName() + " selected");
    }
    
    private void selectSiege(Siege siege) {
        selectSiege(siege, true);
    }
    
    private void selectSection(Section section) {
        selectSection(section, true);
    }
    
    
    @FXML
    private void handleEvents(MouseEvent event) {
        
            try {
                // Load the next FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vues/EventManager.fxml"));
                Parent root = loader.load();
                EventManagerController nextController = loader.getController();

                // Show the new scene
                Stage stage = (Stage) MenuEvents.getScene().getWindow();
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
    
    private double zoomLevel = 1.0;
    public void zoomInButtonClicked() {
        zoomLevel *= 1.1; 
        applyZoom();
        System.out.println("zoom in");
        
        StadeLayout.setPrefWidth(StadeLayout.getWidth()+200);
        StadeLayout.setPrefHeight(StadeLayout.getHeight()+200);
    }

    public void zoomOutButtonClicked() {
        zoomLevel /= 1.1; 

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
}
