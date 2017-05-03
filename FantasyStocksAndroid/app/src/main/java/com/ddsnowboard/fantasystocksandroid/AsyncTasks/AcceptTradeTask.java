package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.function.IntSupplier;

/**
 * This accepts a Trade whose id is given by the first tradeIdSupplier
 */

public class AcceptTradeTask extends AsyncTask<IntSupplier, Void, Void> {
    @Override
    protected Void doInBackground(IntSupplier... tradeIdSuppliers) {
        int id = tradeIdSuppliers[0].getAsInt();
        Trade.get(id).accept();
        return null;
    }
}
