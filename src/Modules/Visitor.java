/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ayoub Zerdoum
 */
public class Visitor extends User{
    private String email;
    private String phoneNumber;
    private List<Integer> ticketIDs;

    public Visitor(int userID, String username, String password, boolean isAdmin, String email, String phoneNumber) {
        super(userID, username, password, isAdmin);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ticketIDs = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getTicketIDs() {
        return ticketIDs;
    }

    public void setTickets(int ticketID) {
        this.ticketIDs.add(ticketID);
    }
}
