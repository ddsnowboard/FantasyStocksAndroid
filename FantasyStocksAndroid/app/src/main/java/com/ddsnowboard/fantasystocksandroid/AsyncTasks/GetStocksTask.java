package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ddsnowboard.fantasystocksandroid.Utilities;
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

public class GetStocksTask extends GetterTask<Stock[]> {
    public static final String TAG = "GetStocksTask";

    public GetStocksTask(Context ctx, Consumer<Stock[]> cb) {
        super(ctx, cb);
    }

    @Override
    protected Stock[] doInBackground(IntSupplier... generators) {
        int floorId = generators[0].getAsInt();
        if (floorId == -1) {
            // We should get the first floor that this guy owns
            User u = Utilities.login(ctx);
            Player p = u.getPlayers()[0];
            floorId = p.getFloor().getId();

            // The first (and probably only) time we have to do this,
            // we have to tell the floating action button the floor we found
            Intent intent = new Intent(Utilities.FOUND_STARTING_FLOOR);
            intent.putExtra(Utilities.FLOOR_ID, floorId);
            ctx.sendBroadcast(intent);
        }
        Stock[] stocks = Floor.get(floorId).getStocks();
        Stream<Stock> st = Arrays.stream(stocks);
        st = st.map(s -> Stock.get(s.getId()));
        Stock[] retval = st.toArray(i -> new Stock[i]);
        return retval;
    }

}
