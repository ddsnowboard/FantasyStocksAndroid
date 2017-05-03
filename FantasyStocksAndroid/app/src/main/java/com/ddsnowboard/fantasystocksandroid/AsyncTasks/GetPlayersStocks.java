package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This returns all the stocks owned by a certain Player, specified by the first playerIdSupplier
 */

public class GetPlayersStocks extends GetterTask<Stock[]>{
    public GetPlayersStocks(Context ctx, Consumer<Stock[]> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Stock[] doInBackground(IntSupplier... playerIdSuppliers) {
        Player player = Player.get(playerIdSuppliers[0].getAsInt());
        return Arrays.stream(player.getStocks()).mapToInt(Stock::getId).mapToObj(Stock::get).toArray(Stock[]::new);
    }
}
