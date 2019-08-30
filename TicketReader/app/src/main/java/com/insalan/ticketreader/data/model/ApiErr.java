package com.insalan.ticketreader.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class representing an Error message received from the API
 */
public class ApiErr extends ApiResponse implements Serializable {


    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("no")
    private int errorNumber;

    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("msg")
    private String errorMessage;

    /*
     * GETTER & SETTER
     */

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }
}
