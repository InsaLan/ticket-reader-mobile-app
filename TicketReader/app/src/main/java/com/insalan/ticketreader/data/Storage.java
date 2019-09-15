package com.insalan.ticketreader.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class storing informations, shared by every part of the app
 */
public final class Storage {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private static SharedPreferences infos;

    private static synchronized SharedPreferences getInfos(final Context context) {
        if (infos == null) {
            infos = context.getSharedPreferences("storage", Context.MODE_PRIVATE);
        }
        return infos;
    }


    /*
     * Public methods to modify stored infos
     */
    public static void storeUserCredentials(final Context context, final String username, final String password) {
        storeValue(context, USERNAME_KEY, username);
        storeValue(context, PASSWORD_KEY, password);
    }

    public static String getUsername(final Context context) {
        return getValue(context, USERNAME_KEY, null);
    }

    public static void deleteUsername(final Context context) {
        deleteValue(context, USERNAME_KEY);
    }

    public static String getPassword(final Context context) {
        return getValue(context, PASSWORD_KEY, null);
    }

    public static void deletePassword(final Context context) {
        deleteValue(context, PASSWORD_KEY);
    }

    public static void clear(final Context context) {
        deleteUsername(context);
        deletePassword(context);
    }

    /*
     * Private generic methods
     */
    private static void storeValue(final Context context, final String key, final String token) {
        getInfos(context).edit().putString(key, token).apply();
    }

    private static String getValue(final Context context, final String key, final String defaultValue) {
        return getInfos(context).getString(key, defaultValue);
    }

    private static void deleteValue(final Context context, final String key) {
        getInfos(context).edit().remove(key).apply();
    }
}