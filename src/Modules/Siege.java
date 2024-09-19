/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.util.Objects;
import java.util.List;
import javafx.scene.shape.Circle;

/**
 *
 * @author Ayoub Zerdoum
 */
public class Siege extends Circle{
    private int siegeId;
    private int numSiege;
    private int sectionId;
    private String status;
    private static double radius = 2.0;
    private static double scale = 1.0;
    private double x;
    private double y;

    static {
        radius = 5;
        scale = 1.0;
    }
    
    public static final String[] STATUS_VALUES = {
        "RESERVED",
        "RESALE",
        "SWITCH",
        "AVAILABLE",
        "YOURS"
    };

    public Siege(int siegeId, int numSiege, int sectionId, String status, double x, double y) {
        this.siegeId = siegeId;
        this.numSiege = numSiege;
        this.sectionId = sectionId;
        this.status = status;
        this.x = x;
        this.y = y;
        
        // preciser la taille du siege
        setScaleX(scale);
        setScaleY(scale);
        // preciser le diametre du siege
        setRadius(radius);
        // Positionnez le siège 
        setCenterX(x);
        setCenterY(y);

    }

    // Getteurs et Setteurs pour les attributs statics
    public static double getStaticRadius() {return radius;}

    public static void setStaticRadius(double radius,List<Section> sections) {
        Siege.radius = radius;
        for (Section section : sections) {
            for (Siege siege : section.sieges) {
                siege.setRadius(radius);
            }
        }
    }
    
    public static void setStaticRadius(double radius) {Siege.radius = radius;}
    public static void setScaleAll(double scale){Siege.scale = scale;}

    public static double getScale() {return scale;}
    
    public static void setScaleAll(double scale,List<Section> sections){
        Siege.scale = scale;
        for (Section section : sections) {
            for (Siege siege : section.sieges) {
                siege.setScaleX(scale);
                siege.setScaleY(scale);
            }
        }
    }

    
    // Setteurs
    public void setSiegeId(int id) {siegeId = id;}
    public void setSiegeNumber(int number) {numSiege = number;}
    public void setSectionId(int sectionId) {this.sectionId = sectionId;}
    public void setStatus(String status) {this.status = status;}

    public void setX(double x) {
        this.x = x;
        setCenterX(x);
    }

    public void setY(double y) {
        this.y = y;
        setCenterY(y);
    }

    // Getteurs
    public int getSiegeId() {return siegeId;}
    public int getNumSiege() {return numSiege;}
    public int getSectionId() {return sectionId;}
    public String getStatus() {return status;}
    public double getX() {return x;}
    public double getY() {return y;}
    
    // Check if a seat is reserved
    public boolean isReserved() {
        return status.equals(STATUS_VALUES[0]);
    }

    // Check if a seat is for sale
    public boolean isForSale() {
        return status.equals(STATUS_VALUES[1]);
    }

    // Check if a seat is available for purchase
    public boolean isAvailable() {
        return status.equals(STATUS_VALUES[3]);
    }
    
    // Check if a seat is for switching
    public boolean isForSwitch() {
        return status.equals(STATUS_VALUES[2]);
    }
    
   @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Siege siege = (Siege) obj;
        return siegeId == siege.siegeId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(siegeId);
    }
}
