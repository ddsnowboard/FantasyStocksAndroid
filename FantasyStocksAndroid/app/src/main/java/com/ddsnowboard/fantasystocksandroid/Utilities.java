package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.content.SharedPreferences;

import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.User;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class Utilities {
    public static <T> boolean contains(T[] haystack, T needle) {
        for (T s : haystack)
            if (s.equals(needle))
                return true;
        return false;
    }

    public static User login(Context ctx) {
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.preferences), 0);
        String username = prefs.getString(ctx.getString(R.string.username), null);
        String password = prefs.getString(ctx.getString(R.string.password), null);
        api.login(username, password);
        return api.getUser();
    }
}
