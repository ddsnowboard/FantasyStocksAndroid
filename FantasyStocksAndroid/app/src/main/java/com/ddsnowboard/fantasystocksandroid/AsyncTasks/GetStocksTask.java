package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.R;
import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class GetStocksTask extends AsyncTask<IntSupplier, Void, Stock[]> {
    Context ctx;
    Consumer<Stock[]> callback;

    public GetStocksTask(Context ctx) {
        this.ctx = ctx;
    }

    public void setCallback(Consumer<Stock[]> cb) {
        this.callback = cb;
    }

    @Override
    protected Stock[] doInBackground(IntSupplier... generators) {
        int floorId = generators[0].getAsInt();
        if (floorId == -1) {
            // We should get the first floor that this guy owns
            User u = Utilities.login(ctx);
            Player p = u.getPlayers()[0];
            floorId = p.getFloor().getId();
        }
        Stock[] stocks = Floor.get(floorId).getStocks();
        Stream<Stock> st = Arrays.stream(stocks);
        st = st.map(s -> Stock.get(s.getId()));
        Stock[] retval = st.toArray(i -> new Stock[i]);
        return retval;
    }

    @Override
    protected void onPostExecute(Stock[] stocks) {
        if(callback != null)
        callback.accept(stocks);
    }
}
