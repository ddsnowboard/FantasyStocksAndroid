package com.ddsnowboard.fantasystocksandroid;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.AbbreviatedUser;
import com.jameswk2.FantasyStocksAPI.User;

public class SecondLevelTrade extends TradeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        User user = gson.fromJson(getIntent().getStringExtra(USER), AbbreviatedUser.class);
        setText("What stocks do you want from " + user.getUsername());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stocksToReceive = adapter.getObjects();
                sendTrade(stocksToSend, stocksToReceive, currentRecipient);
                currentFirstLevel.finish();
                finish();
            }
        });
    }
}
