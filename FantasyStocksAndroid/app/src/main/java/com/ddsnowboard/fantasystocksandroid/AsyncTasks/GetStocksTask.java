package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.R;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class GetStocksTask extends AsyncTask<Void, Void, Stock[]> {
    Context ctx;

    Floor floor = null;

    int idx;

    Player player = null;

    Consumer<Stock[]> callback = null;

    public GetStocksTask(Context ctx) {
        this.ctx = ctx;
    }

    public void setFloor(Floor f) {
        floor = f;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public void setIndex(int idx) {
        this.idx = idx;
    }

    public GetStocksTask setCallback(Consumer<Stock[]> cb) {
        this.callback = cb;
        return this;
    }

    @Override
    protected Stock[] doInBackground(Void... voids) {
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        User u;
        try {
            u = api.getUser();
        } catch (RuntimeException e) {
            SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.preferences), 0);
            String username = prefs.getString(ctx.getString(R.string.username), null);
            String password = prefs.getString(ctx.getString(R.string.password), null);
            api.login(username, password);
            u = api.getUser();
        }

        if (floor == null) {
            if (player == null) {
                player = playerFromUser(u, idx);
            }
            floor = floorFromPlayer(player);
        }
        return floor.getStocks();
    }

    private Player playerFromUser(User u, int idx) {
        return u.getPlayers()[idx];
    }

    private Floor floorFromPlayer(Player p) {
        return p.getFloor();
    }

    @Override
    protected void onPostExecute(Stock[] stocks) {
        if(callback != null)
        callback.accept(stocks);
    }
}
