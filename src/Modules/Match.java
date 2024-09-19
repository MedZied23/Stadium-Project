/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modules;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 *
 * @author Ayoub Zerdoum
 */
public class Match extends  Evenement{
    private String team1;
    private String team2;
    private String logoTeam1;
    private String logoTeam2;

    public Match(int eventId, String eventName, LocalDate eventDate,LocalTime eventTime, Map<Integer,Double> sectionPrices,String description, String team1, String team2, String logoTeam1, String logoTeam2) {
        super(eventId, eventName, eventDate,eventTime, sectionPrices, description);
        this.team1 = team1;
        this.team2 = team2;
        this.logoTeam1 = logoTeam1;
        this.logoTeam2 = logoTeam2;
    }

    public String getTeam1() {return team1;}
    public String getTeam2() {return team2;}
    public String getLogoTeam1() {return logoTeam1;}
    public String getLogoTeam2() {return logoTeam2;}
    
    
    public void setTeam1(String team1) {this.team1 = team1;}
    public void setTeam2(String team2) {this.team2 = team2;}
    public void setLogoTeam1(String logoTeam1) {this.logoTeam1 = logoTeam1;}
    public void setLogoTeam2(String logoTeam2) {this.logoTeam2 = logoTeam2;}
    
    @Override
    public String toString() {
        return "Match{" +
                "eventId=" + getEventId() +
                ", eventName='" + getEventName() + '\'' +
                ", eventDate=" + getEventDate() +
                ", eventTime=" + getEventTime() +
                ", sectionPrices=" + getSectionPrices() +
                ", description='" + getDescription() + '\'' +
                ", team1='" + getTeam1() + '\'' +
                ", team2='" + getTeam2() + '\'' +
                ", logoTeam1='" + getLogoTeam1() + '\'' +
                ", logoTeam2='" + getLogoTeam2() + '\'' +
                '}';
    }
}
