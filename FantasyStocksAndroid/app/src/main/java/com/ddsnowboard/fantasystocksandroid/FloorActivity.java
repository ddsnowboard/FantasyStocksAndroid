package com.ddsnowboard.fantasystocksandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetStocksTask;
import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.AbbreviatedStock;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

import java.util.ArrayList;
import java.util.Arrays;

/*
 This is really confusing me. I have no idea what is going on or how all these pieces fit together.
 I think I might have to either make everything block until I get the information I need from
 the server, or I might have to do something fancy to dynamically load the stocks. Or maybe I should
 load them where they are actually needed... Hmm. Either way, I should probably load some static
 dummy stocks so I can at least find out what would happen if everything were working. Then I could
 worry about the rest of this later and have something to show tomorrow.
 */

/* This is a complete cluster. I have no idea what's going on or how any of these things fit together.
Something is creating StockFragments, but it's clearly not the things I thought it was because
it's not getting any arguments, unless I did those wrong, which is possible. I might have just fixed it
on 167. Either way, something's fucky I think.
 */

public class FloorActivity extends FragmentActivity implements StockFragment.OnListFragmentInteractionListener {
    public static final String TAG = "FloorActivity";

    StockFragment stockFragment;
    FloatingActionButton fab;
    ProgressDialog progress;

    ArrayList<Stock> stockArray = new ArrayList<>();

    Player[] playerArray;

    FloorActivity.PagerAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences), 0);
        if (!prefs.contains(getString(R.string.username))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        progress = new ProgressDialog(this);
        progress.setTitle(R.string.loadingText);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), stockArray);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        /*
        recyclerView = (RecyclerView) findViewById(R.id.floorList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        */

        bindToFloor(0);


        fab = (FloatingActionButton) findViewById(R.id.fab);
    }


    private void bindToFloor(Floor f) {
        GetStocksTask task = new GetStocksTask(this);
        task.setCallback(s -> {
            stockArray.clear();
            for(Stock stock : s)
                stockArray.add(stock);
            progress.hide();
            pagerAdapter.notifyDataSetChanged();
        });
        task.setFloor(f);
        progress.show();
        task.execute();
    }

    private void bindToFloor(int idx) {
        GetStocksTask task = new GetStocksTask(this);
        task.setCallback(s -> {
            stockArray.clear();
            for(Stock stock : s)
                stockArray.add(stock);
            progress.hide();
            pagerAdapter.notifyDataSetChanged();
        });
        task.setIndex(idx);
        progress.show();
        task.execute();
    }

    private boolean contains(Stock[] haystack, Stock needle) {
        for (Stock s : haystack)
            if (s.equals(needle))
                return true;
        return false;
    }

    @Override
    public void onListFragmentInteraction(Stock stock) {
        // Make a new trade with that stock
    }

    class PagerAdapter extends FragmentPagerAdapter {

        final int STOCKS_PAGE = 0;
        final int PLAYERS_PAGE = 1;

        ArrayList<Stock> stocks;

        public PagerAdapter(FragmentManager fm, ArrayList<Stock> stocks) {
            super(fm);
            this.stocks = stocks;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == STOCKS_PAGE) {
                StockFragment stocksFragment = new StockFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                Log.d(TAG, "stocks is " + stocks.toString());
                **********
                // It might have something to do with the fact that I'm serializing this so it's not
                // actually the same arraylist...
                String jsonString = gson.toJson(stocks.toArray(new Stock[0]), AbbreviatedStock[].class);
                Log.d(TAG, "JsonString is " + jsonString);
                bundle.putString(StockFragment.STOCKS, jsonString);
                stocksFragment.setArguments(bundle);
                FloorActivity.this.stockFragment = stocksFragment;
                return stocksFragment;
            } else if (position == PLAYERS_PAGE) {
                PlayerFragment fragment = new PlayerFragment();
                return fragment;
            } else {
                throw new RuntimeException("Something strange happened");
            }
        }

        public void setStocks(ArrayList<Stock> stocks) {
            this.stocks.clear();
            this.stocks.addAll(stocks);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
