package com.insalan.ticketreader.api.json;

import android.util.Log;

import com.insalan.ticketreader.data.model.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Class used to parse Json, from the response, and serialize it with given classes
 */
public final class GsonService {

    private static final String TAG = GsonService.class.getSimpleName();

    /**
     * Single instance.
     */
    private static final GsonService INSTANCE = new GsonService();

    private final Gson gson;

    /**
     * Hidden constructor.
     */
    private GsonService() {
        super();
        this.gson = new GsonBuilder().registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer()).create();
    }

    /**
     * Get service instance.
     *
     * @return Service instance.
     */
    public static GsonService getInstance() {
        return INSTANCE;
    }

    public <T> T fromJson(final String json, final Class<T> clazz) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "Parsing " + json);
        }
        return this.gson.fromJson(json, clazz);
    }

}
