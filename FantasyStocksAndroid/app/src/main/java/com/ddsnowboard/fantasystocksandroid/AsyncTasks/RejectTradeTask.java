package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 5/2/17.
 */

public class RejectTradeTask extends AsyncTask<IntSupplier, Void, Void> {
    @Override
    protected Void doInBackground(IntSupplier... tradeIdSuppliers) {
        int id = tradeIdSuppliers[0].getAsInt();
        Trade.get(id).decline();
        return null;
    }
}
