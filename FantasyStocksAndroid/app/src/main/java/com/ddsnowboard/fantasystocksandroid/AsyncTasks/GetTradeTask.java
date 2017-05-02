package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 5/2/17.
 */

public class GetTradeTask extends GetterTask<Trade> {

    public GetTradeTask(Context ctx, Consumer<Trade> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Trade doInBackground(IntSupplier... tradeIdSuppliers) {
        return Trade.get(tradeIdSuppliers[0].getAsInt());
    }
}
