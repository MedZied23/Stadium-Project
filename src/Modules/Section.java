/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import javafx.scene.Group;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import java.util.List;
import java.util.ArrayList;

import Modules.Exceptions.DuplicateSiegeException;
import Modules.Exceptions.SiegeNotFoundException;
/**
 *
 * @author Ayoub Zerdoum
 */
public class Section {
    private int sectionId;
    private String sectionName;
    private SVGPath sectionShape = new SVGPath();;
    public List<Siege> sieges;
    private double scale;
    private double x;
    private double y;
    private double rotation;
    
    public static String[] sectionShapes = {
        "M0,0 L100,0 L100,60 L0,60 Z",
        "M0,0 L100,0 L100,50 L0,50 Z",
        "M0,0 L100,0 L100,77 L0,77 Z",
        "M10 0 L90 0 L70 100 L30 100 Z",
        "M50 0 L100 40 L75 100 L25 100 L0 40 Z",
        "M15 0 L85 0 L70 30 L30 30 Z",
        "M10 0 L100 0 L100 50 L0 50 Z",
        "M0 0 L90 0 L100 50 L0 50 Z",
        "M0 0 L92 0 L100 40 L0 40 Z",
        "M8 0 L100 0 L100 40 L0 40 Z"
    };
    //"M50 0 L100 40 L80 100 L20 100 L0 40 Z"
    
    public Section(int sectionId, String sectionName, int shapeIndex, double x, double y, double rotation,double scale) {
        this(sectionId, sectionName, shapeIndex, new ArrayList<>(), x, y, rotation,scale);
    }

    public Section(int sectionId, String sectionName, int shapeIndex, List<Siege> sieges, double x, double y, double rotation,double scale) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        if (shapeIndex >= 0 && shapeIndex < sectionShapes.length) {
            this.sectionShape.setContent(sectionShapes[shapeIndex]);
        }
        this.sieges = sieges;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.scale = scale;
    }
    
    public int getSectionId() {return sectionId;}
    public String getSectionName() {return sectionName;}
    public SVGPath getSectionShape() {return sectionShape;}
    public List<Siege> getSieges() {return sieges;}
    public double getX() {return x;}
    public double getY() {return y;}
    public double getRotation() {return rotation;}
    public double getScale() {return scale;}

    public void setSectionId(int id) {this.sectionId=id;}
    public void setScale(double scale) {this.scale = scale;}
    public void setX(double x) {this.x=x;}
    public void setY(double y) {this.y=y;}
    public void setRotation(double r) {this.rotation=r;}
    public void setSectionName(String name) {this.sectionName = name;}
    
    public void setShapeByIndex(int shapeIndex) {
        if (shapeIndex >= 0 && shapeIndex < sectionShapes.length) {
            this.sectionShape.setContent(sectionShapes[shapeIndex]);
        }
    }
    
    public int getShapeIndex() {
    // Logic to extract the shape index from the sectionShape content
        for (int i = 0; i < sectionShapes.length; i++) {
            if (sectionShape.getContent().equals(sectionShapes[i])) {
                return i;
            }
        }
        return -1; // Shape index not found
    }
    
    public boolean hasSieges() {return !sieges.isEmpty();}
    
    public Siege getSiegeByNumber(int index) {
        if (index >= 0 && index < sieges.size()) {
            return sieges.get(index);
        } else {
            return null; // Return null if the index is out of bounds
        }
    }
    
    // Méthode pour effectuer une rotation
    public void rotate(double angle) {
        // Mettez en œuvre la logique de rotation ici
        rotation += angle;
    }

    // Méthode pour effectuer une translation
    public void translate(double deltaX, double deltaY) {
        // Mettez en œuvre la logique de translation ici
        x += deltaX;
        y += deltaY;
    }
    
    
    public void addSiege(Siege siege) {
        try {
            if (sieges.contains(siege)) {
                throw new DuplicateSiegeException(siege.getSiegeId());
            } else {
                sieges.add(siege);
            }
        } catch (DuplicateSiegeException e) {
            // Print the exception with its message
            System.out.println(e);
        }
    }

    // Method to remove a seat from the section
    public void removeSiege(Siege siege) {
        try {
            if (!sieges.contains(siege)) {
                throw new SiegeNotFoundException(siege.getSiegeId());
            } else {
                sieges.remove(siege);
            }
        } catch (SiegeNotFoundException e) {
            System.out.println(e);
        }
    }

    // Method to get information about a specific seat by seat ID
    public Siege getSiegeById(int siegeId) {
        for (Siege siege : sieges) {
            if (siege.getSiegeId() == siegeId) {
                return siege;
            }
        }
        return null; // Seat not found
    }

    // Method to check seat availability by seat ID
    public boolean isSiegeAvailable(int siegeId) {
        Siege siege = getSiegeById(siegeId);
        return siege != null && siege.isAvailable();
    }

    // Method to check if the section has available seats
    public boolean hasAvailableSieges() {
        for (Siege siege : sieges) {
            if (siege.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    // Method to get a list of sieges with the status "Resale"
    public List<Siege> getSiegesWithStatusResale() {
        List<Siege> resaleSieges = new ArrayList<>();
        for (Siege siege : sieges) {
            if (siege.isForSale()) {
                resaleSieges.add(siege);
            }
        }
        return resaleSieges;
    }

    // Method to get a list of sieges with the status "Switch"
    public List<Siege> getSiegesWithStatusSwitch() {
        List<Siege> switchSieges = new ArrayList<>();
        for (Siege siege : sieges) {
            if (siege.isForSwitch()) {
                switchSieges.add(siege);
            }
        }
        return switchSieges;
    }
}
