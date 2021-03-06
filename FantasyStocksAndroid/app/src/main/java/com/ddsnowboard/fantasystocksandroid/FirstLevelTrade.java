package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetPlayerTask;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;

/**
 * This activity is the first step of a making a Trade, after picking another User to send the Trade to.
 */
public class FirstLevelTrade extends TradeActivity {
    public static final String TAG = "FirstLevelTrade";

    private ArrayList<Stock> stocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent bundle = getIntent();
        int playerId = bundle.getIntExtra(Utilities.PLAYER_ID, Utilities.UNKNOWN_ID);

        setPlayer(playerId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(FirstLevelTrade.this, SecondLevelTrade.class);
                stocks.clear();
                stocks.addAll(getStocks());
                newIntent.putExtra(Utilities.PLAYER_ID, playerId);
                startActivityForResult(newIntent, Utilities.MAKE_TRADE);
            }
        });

        setText(R.string.loading);

        // Get the player that we received in the Intent and put their name in the header field
        GetPlayerTask getPlayerTask = new GetPlayerTask(this,
                player -> setText(getString(R.string.other_stocks_prompt, player.getUser().getUsername())));
        getPlayerTask.execute(() -> playerId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utilities.MAKE_TRADE) {
            if(resultCode == RESULT_OK) {
                Intent out = new Intent();
                out.putExtra(Utilities.TRADE_STOCKS_FROM_USER, stocks.stream().mapToInt(Stock::getId).toArray());
                // This is what we got from the second level trade activity
                out.putExtra(Utilities.TRADE_STOCKS_TO_GIVE_USER, data.getIntArrayExtra(Utilities.TRADE_STOCKS_TO_GIVE_USER));
                setResult(RESULT_OK, out);
                finish();
            }
        }
    }
}
