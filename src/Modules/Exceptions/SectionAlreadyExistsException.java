/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules.Exceptions;

/**
 *
 * @author Ayoub Zerdoum
 */
public class SectionAlreadyExistsException extends Exception {
    public SectionAlreadyExistsException(int sectionId) {
        super("Section with ID " + sectionId + " already exists.");
    }
}
