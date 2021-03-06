package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayerTask;
import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the last screen in the trading sequence
 */
public class SecondLevelTrade extends TradeActivity {
    public static final String TAG = "SecondLevelTrade";
    ArrayList<Stock> stocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int playerId = getIntent().getIntExtra(Utilities.PLAYER_ID, Utilities.UNKNOWN_ID);
        if (playerId == Utilities.UNKNOWN_ID)
            throw new RuntimeException("You didn't give a playerId");
        GetterTask<Player> getUserPlayerTask = new GetPlayerTask(this, this::setPlayer);

        getUserPlayerTask.execute(() -> {
            Floor floor = Player.get(playerId).getFloor();
            User user = FantasyStocksAPI.getInstance().getUser();
            int retval = Arrays.stream(user.getPlayers())
                .filter(p -> p.getFloor().equals(floor))
                .findAny()
                .get()
                .getId();
            return retval;
        });

        setText(R.string.loading);
        // Set the text at the top of the screen
        GetterTask<Player> task = new GetPlayerTask(this,
                p -> setText(getString(R.string.your_stocks_prompt, p.getUser().getUsername())));

        task.execute(() -> playerId);

        fab.setOnClickListener(view -> {
            stocks.clear();
            stocks.addAll(getStocks());
            Intent out = new Intent();
            out.putExtra(Utilities.TRADE_STOCKS_TO_GIVE_USER, stocks.stream().mapToInt(Stock::getId).toArray());
            setResult(RESULT_OK, out);
            finish();
        });
    }
}
