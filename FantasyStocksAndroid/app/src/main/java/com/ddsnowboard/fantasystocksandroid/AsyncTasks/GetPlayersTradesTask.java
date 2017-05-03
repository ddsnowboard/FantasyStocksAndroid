package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This gets the Trades for the logged-in user on the floor given by the first floorIdSupplier
 */

public class GetPlayersTradesTask extends GetterTask<Trade[]> {

    public GetPlayersTradesTask(Context ctx, Consumer<Trade[]> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Trade[] doInBackground(IntSupplier... floorIdSuppliers) {
        int id = floorIdSuppliers[0].getAsInt();
        if (id == Utilities.UNKNOWN_ID)
            id = FantasyStocksAPI.getInstance().getUser().getPlayers()[0].getFloor().getId();
        Floor floor = Floor.get(id);
        Trade[] trades = Arrays.stream(FantasyStocksAPI.getInstance().getUser().getPlayers())
                .filter(p -> p.getFloor().equals(floor))
                .findFirst().get().getReceivedTrades();
        return Arrays.stream(trades).mapToInt(Trade::getId)
                .mapToObj(Trade::get)
                .toArray(Trade[]::new);
    }
}
