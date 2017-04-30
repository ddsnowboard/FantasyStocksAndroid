package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 4/29/17.
 */

public class GetPlayersStocks extends GetterTask<Stock[]>{
    public GetPlayersStocks(Context ctx, Consumer<Stock[]> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Stock[] doInBackground(IntSupplier... intSuppliers) {
        Player player = Player.get(intSuppliers[0].getAsInt());
        return Arrays.stream(player.getStocks()).mapToInt(Stock::getId).mapToObj(Stock::get).toArray(Stock[]::new);
    }
}
