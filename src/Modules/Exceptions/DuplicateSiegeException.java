/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules.Exceptions;

/**
 *
 * @author Ayoub Zerdoum
 */
public class DuplicateSiegeException extends Exception {
    public DuplicateSiegeException(int siegeId) {
        super("A duplicate siege with ID " + siegeId + " was found.");
    }
}
