/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules.Exceptions;

/**
 *
 * @author Ayoub Zerdoum
 */
public class InvalidPriceException extends RuntimeException{
    public InvalidPriceException(Double price) {
        super("Invalid price: " + price + ". Price must be a non-negative value.");
    }
}
