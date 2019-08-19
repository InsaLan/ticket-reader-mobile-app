package com.insalan.ticketreader.api;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.insalan.ticketreader.data.Storage;
import com.insalan.ticketreader.data.model.ApiErr;
import com.insalan.ticketreader.data.model.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Singleton class
 * Specifies every request that can be made to the InsaLan API
 * Handles specific data related with the requests and the server connection
 */
public class ApiRequests {

    private static volatile ApiRequests instance;

    private ApiRequests() {
        super();
    }

    public static synchronized ApiRequests getInstance() {
        if (instance == null) {
            instance = new ApiRequests();
        }
        return instance;
    }

    /*
     * API REQUESTS
     */

    public static void getTicket(final Context context, final String ticketToken, final Consumer<ApiResponse> callback, final Consumer<VolleyError> errorCallback) {
        // Creating the URL for the request
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConfigServer.SCHEME)
                .authority(ConfigServer.AUTHORITY)
                .appendPath(ConfigServer.BASE_API)
                .appendPath("get");
        String uri = builder.build().toString();

        // Creating the request
        RequestService.getInstance(context).postJsonParsed(uri, createTokenData(ticketToken), ApiResponse.class, createHeaders(context),
                // Calls the callback, that will handle the ticket, display it or do actions with it
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(final ApiResponse apiRes) {
                        callback.accept(apiRes);
                    }
                },
                // Calls the error callback, that will handle the error received
                errorCallback::accept);
    }


    public static void validateTicket(final Context context, final String ticketToken, final Consumer<ApiErr> callback, final Consumer<VolleyError> errorCallback) {
        // Creating the URL for the request
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConfigServer.SCHEME)
                .authority(ConfigServer.AUTHORITY)
                .appendPath(ConfigServer.BASE_API)
                .appendPath("validate");
        String uri = builder.build().toString();

        // Creating the request
        RequestService.getInstance(context).postJsonParsed(uri, createTokenData(ticketToken), ApiErr.class, createHeaders(context),
                // Calls the callback, that will handle the ticket, display it or do actions with it
                new Response.Listener<ApiErr>() {
                    @Override
                    public void onResponse(final ApiErr error) {
                        callback.accept(error);
                    }
                },
                // Calls the error callback, that will handle the error received
                errorCallback::accept);
    }


    /*
     * UTILS AND COMMON METHODS
     */

    private static JSONObject createTokenData(final String token) {
        JSONObject data = new JSONObject();
        try {
            data.put("token", token);
        } catch (JSONException e) {
            Log.e("Exception", "JSON");
        }
        return data;
    }

    private static Map<String, String> createHeaders(final Context context) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + Base64.encodeToString((Storage.getUsername(context) + ":" + Storage.getPassword(context)).getBytes(), Base64.DEFAULT));

        return headers;
    }


}

