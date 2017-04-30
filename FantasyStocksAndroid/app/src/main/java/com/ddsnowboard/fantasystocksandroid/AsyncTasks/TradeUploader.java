package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.Trade;

import java.util.Arrays;

/**
 * Created by ddsnowboard on 4/29/17.
 */

public class TradeUploader extends AsyncTask<Utilities.TradeContainer, Void, Void> {
    @Override
    protected Void doInBackground(Utilities.TradeContainer... tradeContainers) {
        Utilities.TradeContainer tc = tradeContainers[0];
        Player recipient = Player.get(tc.getRecipientPlayerId());
        Floor floor = recipient.getFloor();
        Player currentPlayer = Arrays.stream(FantasyStocksAPI.getInstance().getUser().getPlayers())
                .filter(p -> p.getFloor().equals(floor))
                .findFirst()
                .get();
        Stock[] recipientStocks = Arrays.stream(tc.getRecipientStockIds()).mapToObj(Stock::get).toArray(Stock[]::new);
        Stock[] senderStocks = Arrays.stream(tc.getSenderStockIds()).mapToObj(Stock::get).toArray(Stock[]::new);
        Trade out = Trade.create(currentPlayer, recipient, senderStocks, recipientStocks, floor);
        return null;
    }
}
