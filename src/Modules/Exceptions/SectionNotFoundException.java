/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules.Exceptions;

/**
 *
 * @author Ayoub Zerdoum
 */
public class SectionNotFoundException extends Exception{
    public SectionNotFoundException(int sectionId) {
        super("Section with ID " + sectionId + " was not found.");
    }
}
