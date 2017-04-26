package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.AbbreviatedStock;
import com.jameswk2.FantasyStocksAPI.AbbreviatedUser;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.Trade;
import com.jameswk2.FantasyStocksAPI.User;

public class FirstLevelTrade extends TradeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stocksToReceive.clear();
        stocksToSend.clear();
        currentRecipient = new DummyPlayer();
        Intent bundle = getIntent();
        Gson gson = new Gson();
        User user = gson.fromJson(bundle.getStringExtra(USER), AbbreviatedUser.class);
        Stock stock = gson.fromJson(bundle.getStringExtra(STOCK), AbbreviatedStock.class);
        setText("What stocks do you want to give to " + user.getUsername());
        addStock(stock);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stocksToSend = adapter.getObjects();
                currentFirstLevel = FirstLevelTrade.this;
                Intent newIntent = new Intent(FirstLevelTrade.this, SecondLevelTrade.class);
                newIntent.putExtra(USER, bundle.getStringExtra(USER));
                startActivity(newIntent);
            }
        });
    }

    class DummyPlayer implements Player {
        @Override
        public int getId() {
            return 0;
        }

        @Override
        public User getUser() {
            return new User() {
                @Override
                public int getId() {
                    return 0;
                }

                @Override
                public String getUsername() {
                    return "A User";
                }

                @Override
                public Player[] getPlayers() {
                    return new Player[]{DummyPlayer.this};
                }
            };
        }

        @Override
        public Floor getFloor() {
            return null;
        }

        @Override
        public Stock[] getStocks() {
            return new Stock[0];
        }

        @Override
        public int getPoints() {
            return 0;
        }

        @Override
        public boolean isFloor() {
            return false;
        }

        @Override
        public Trade[] getSentTrades() {
            return new Trade[0];
        }

        @Override
        public Trade[] getReceivedTrades() {
            return new Trade[0];
        }

        @Override
        public boolean isFloorOwner() {
            return false;
        }
    }
}
