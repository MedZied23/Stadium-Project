/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 *
 * @author Ayoub Zerdoum
 */
public class Ticket {
    private int ticketID;
    private int evenID;
    private double price;
    private int seatNumber;
    private LocalDate dateConcert;
    private LocalTime timeConcert;

    public Ticket(int ticketID, int evenID, double price, int seatNumber, LocalDate dateConcert, LocalTime timeConcert) {
        this.ticketID = ticketID;
        this.evenID = evenID;
        this.price = price;
        this.seatNumber = seatNumber;
        this.dateConcert = dateConcert;
        this.timeConcert = timeConcert;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getEventId() {
        return evenID;
    }

    public void setEvenName(int evenID) {
        this.evenID = evenID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDate getDate() {
        return dateConcert;
    }

    public void setDate(LocalDate dateConcert) {
        this.dateConcert = dateConcert;
    }

    public LocalTime getTime() {
        return timeConcert;
    }

    public void setTime(LocalTime timeConcert) {
        this.timeConcert = timeConcert;
    }

    @Override
    public String toString() {
        return "Ticket{" + "ticketID=" + ticketID + ", evenID=" + evenID + ", price=" + price + ", seatNumber=" + seatNumber + ", dateConcert=" + dateConcert + ", timeConcert=" + timeConcert + '}';
    }
}
