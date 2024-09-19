/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.util.Map;
import java.util.HashMap;
import Modules.Exceptions.InvalidPriceException;
import Modules.Exceptions.SectionAlreadyExistsException;
import Modules.Exceptions.SectionNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


/**
 *
 * @author Ayoub Zerdoum
 */
public abstract class Evenement {
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Map<Integer, Double> sectionPrices = new HashMap<>();
    private String description;

    public Evenement(int eventId, String eventName, LocalDate eventDate,LocalTime eventTime,String description) {
        this(eventId, eventName, eventDate,eventTime, new HashMap<>(),description);
    }

    public Evenement(int eventId, String eventName, LocalDate eventDate,LocalTime eventTime, Map<Integer, Double> sectionPrices, String description) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.sectionPrices = sectionPrices;
        this.description = description;
    
    }

    public int getEventId() {return eventId;}
    public String getEventName() {return eventName;}
    public LocalDate getEventDate() {return eventDate;}
    public LocalTime getEventTime() {return eventTime;}
    public Map<Integer, Double> getSectionPrices() {return sectionPrices;}
    public String getDescription() {return description;}
    
    
    public void setEventId(int eventId) {this.eventId = eventId;}
    public void setEventName(String eventName) {this.eventName = eventName;}
    public void setEventDate(LocalDate eventDate) {this.eventDate = eventDate;}
    public void setEventTime(LocalTime eventTime) {this.eventTime = eventTime;}
    public void setDescription(String description) {this.description = description;}
    
    public boolean isConcert() {
        return this instanceof Concert;
    }
    
    public boolean isMatch() {
        return this instanceof Match;
    }
    
    public void removeSection(int sectionId) {
        sectionPrices.remove(sectionId);
    }

    public void addSectionPrice(int sectionId, double price) throws SectionAlreadyExistsException, InvalidPriceException {
        try {
            if (sectionPrices.containsKey(sectionId)) {
                throw new SectionAlreadyExistsException(sectionId);
            }
            if (price < 0) {
                throw new InvalidPriceException(price);
            }

            sectionPrices.put(sectionId, price);
        } catch (SectionAlreadyExistsException e) {
            System.out.println(e);
        } catch (InvalidPriceException e) {
            System.out.println(e);
        }
    }

    public void updateSectionPrice(int sectionId, double price) throws SectionNotFoundException, InvalidPriceException {
        try {
            if (!sectionPrices.containsKey(sectionId)) {
                throw new SectionNotFoundException(sectionId);
            }
            if (price < 0) {
                throw new InvalidPriceException(price);
            }

            sectionPrices.put(sectionId, price);
        } catch (SectionNotFoundException e) {
            System.out.println(e);
        } catch (InvalidPriceException e) {
            System.out.println(e);
        }
    }
    
    // Get the price for a section
    public double getSectionPrice(int sectionId) {
        try {
            if (sectionPrices.containsKey(sectionId)) {
                return sectionPrices.get(sectionId);
            } else {
                throw new SectionNotFoundException(sectionId);
            }
        } catch (SectionNotFoundException e) {
            System.out.println(e);
            return 0.0;
        }
    }
    
     public double getMinPrice() {
        return Collections.min(sectionPrices.values());
    }

    public double getMaxPrice() {
        return Collections.max(sectionPrices.values());
    }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return eventDate.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return eventTime.format(formatter);
    }
}
