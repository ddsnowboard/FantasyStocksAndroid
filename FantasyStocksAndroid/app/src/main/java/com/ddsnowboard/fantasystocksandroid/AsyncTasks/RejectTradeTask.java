package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.function.IntSupplier;

/**
 * This tells the server that a certain trade, specified by the first `tradeIdSupplier`, has been
 * declined.
 */

public class RejectTradeTask extends AsyncTask<IntSupplier, Void, Void> {
    @Override
    protected Void doInBackground(IntSupplier... tradeIdSuppliers) {
        int id = tradeIdSuppliers[0].getAsInt();
        Trade.get(id).decline();
        return null;
    }
}
