/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules.Exceptions;

/**
 *
 * @author Ayoub Zerdoum
 */
public class SiegeNotFoundException extends Exception{
    public SiegeNotFoundException(int siegeId) {
        super("Siege with ID " + siegeId + " not found in the section.");
    }
}
