package com.insalan.ticketreader.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class representing a ticket received from the API
 */
public class Ticket extends ApiResponse implements Serializable {
    // {"name":"Jean Durant","phone":"0677778844","gameName":"Herpandine","tournament":"CS: GO","ticketScanned":false}

    private String name;

    private String phone;

    private String gameName;

    private String tournament;

    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("ticketScanned")
    private boolean isTicketScanned;

    @SerializedName("ticketCancelled")
    private boolean isTicketCancelled;

    // Added later
    private String token;


    /*
     * GETTERS
     */
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGameName() {
        return gameName;
    }

    public String getTournament() {
        return tournament;
    }

    public boolean isTicketScanned() {
        return isTicketScanned;
    }

    public String getToken() {
        return token;
    }

    public boolean isTicketCancelled() {
        return isTicketCancelled;
    }


    /*
     * SETTERS
     */

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public void setTicketScanned(boolean ticketScanned) {
        isTicketScanned = ticketScanned;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTicketCancelled(boolean ticketCancelled) {
        isTicketCancelled = ticketCancelled;
    }
}
