package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This, given a `Stock`'s id, gets it from the server.
 */

public class GetStockTask extends GetterTask<Stock> {

    public GetStockTask(Context ctx, Consumer<Stock> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Stock doInBackground(IntSupplier... stockIdSuppliers) {
        return Stock.get(stockIdSuppliers[0].getAsInt());
    }
}
