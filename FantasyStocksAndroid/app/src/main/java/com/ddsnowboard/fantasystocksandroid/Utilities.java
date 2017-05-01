package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.content.SharedPreferences;

import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.User;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class Utilities {
    public static final int UNKNOWN_ID = -1;

    public static final String FLOOR_ID = "flooridnumber";
    public static final String FLOOR = "floor";
    public static final String USER_ID = "UserIdNumber";
    public static final String PLAYER_ID = "PlayerIDNumber";
    public static final String LOAD_NEW_FLOOR = "getafloor";
    public static final String PLAYERS = "players";
    public static final String STOCK_ID = "stock";
    public static final String STOCKS = "allStocks";

    public static final int GET_NEW_FLOOR = 1;
    public static final int GET_STOCK_FOR_TRADE = 2;
    public static final String FOUND_STARTING_FLOOR = "foundStartingFloor";

    public static final int MAKE_TRADE = 312;
    // https://imgflip.com/i/1o4x4j
    public static final String TRADE_STOCKS_TO_GIVE_USER = "lksajileajsfiojasofjjk";
    public static final String TRADE_STOCKS_FROM_USER = "asiofaosiufoiupoiupoiupoiueqwrpoiu";
    public static final int RESULT_NOT_READY = 5;



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

    public static class TradeContainer {
        private int recipientPlayerId;
        private int[] recipientStockIds;
        private int[] senderStockIds;

        public int getRecipientPlayerId() {
            return recipientPlayerId;
        }

        public void setRecipientPlayerId(int recipientPlayerId) {
            this.recipientPlayerId = recipientPlayerId;
        }

        public int[] getRecipientStockIds() {
            return recipientStockIds;
        }

        public void setRecipientStockIds(int[] recipientStockIds) {
            this.recipientStockIds = recipientStockIds;
        }

        public int[] getSenderStockIds() {
            return senderStockIds;
        }

        public void setSenderStockIds(int[] senderStockIds) {
            this.senderStockIds = senderStockIds;
        }
    }
}
