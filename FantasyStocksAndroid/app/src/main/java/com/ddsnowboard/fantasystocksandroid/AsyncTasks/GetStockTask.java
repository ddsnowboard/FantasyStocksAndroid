package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 4/29/17.
 */

public class GetStockTask extends GetterTask<Stock> {

    public GetStockTask(Context ctx, Consumer<Stock> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Stock doInBackground(IntSupplier... intSuppliers) {
        return Stock.get(intSuppliers[0].getAsInt());
    }
}
