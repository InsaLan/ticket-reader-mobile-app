package com.insalan.ticketreader.api.json;

import com.insalan.ticketreader.data.model.ApiErr;
import com.insalan.ticketreader.data.model.ApiResponse;
import com.insalan.ticketreader.data.model.Ticket;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Class used to adapt the response parsing in case there is an error
 */
public class ApiResponseDeserializer implements JsonDeserializer<ApiResponse> {

    @Override
    public ApiResponse deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        // If there is the field "err", we deserialize as an ApiErr, else as a Ticket
        boolean isErr = jsonObject.has("err");

        ApiResponse typeModel = null;     

        if(isErr) {
            typeModel = context.deserialize(json, ApiErr.class);
        } else {
            typeModel = context.deserialize(json, Ticket.class);
        }

        return typeModel;
    }
}