package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.UpdateTokenTask;

public class FloorActivity extends FragmentActivity implements StockFragment.OnListFragmentInteractionListener {
    ListView drawer;

    FloatingActionButton fab;

    FloorDrawerHandler drawerHandler;
    FloorActivity.PagerAdapter pagerAdapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preferences), 0);

        // TODO: Should this finish() or not? How is this going to get the username back?
        // startActivityForResult()?
        if (!prefs.contains(getString(R.string.username))) {
            if (true)
                throw new RuntimeException("I need to implement this properly");
            startActivity(new Intent(this, LoginActivity.class));
        }


        // Initialize UI components...
        drawer = (ListView) findViewById(R.id.drawer);
        drawerHandler = new FloorDrawerHandler(drawer);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), Utilities.UNKNOWN_ID);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        IntentFilter filter = new IntentFilter(Utilities.FOUND_STARTING_FLOOR);
        filter.addAction(Utilities.LOAD_NEW_FLOOR);
        BroadcastReceiver receiver = new FloorFoundListener();
        registerReceiver(receiver, filter);
        new UpdateTokenTask().execute();
    }

    @Override
    public void onListFragmentInteraction(Object o) {
        // Does nothing
    }

    class PagerAdapter extends FragmentPagerAdapter {
        final int STOCKS_PAGE = 0;
        final int PLAYERS_PAGE = 1;
        final int TRADES_PAGE = 2;

        int currentPlayerId;

        public PagerAdapter(FragmentManager fm, int startingPlayerId) {
            super(fm);
            currentPlayerId = startingPlayerId;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            super.getPageTitle(position);
            if (position == STOCKS_PAGE)
                return "Stocks";
            else if (position == PLAYERS_PAGE)
                return "Players";
            else if(position == TRADES_PAGE)
                return "Trades";
            else
                throw new RuntimeException("This has too many tabs");
        }

        @Override
        public Fragment getItem(int position) {
            if (position == STOCKS_PAGE) {
                StockFragment stocksFragment = new StockFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Utilities.PLAYER_ID, currentPlayerId);
                stocksFragment.setArguments(bundle);
                return stocksFragment;
            } else if (position == PLAYERS_PAGE) {
                PlayerFragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Utilities.PLAYER_ID, currentPlayerId);
                fragment.setArguments(bundle);
                return fragment;
            }
            else if(position == TRADES_PAGE) {
                TradesFragment fragment = new TradesFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Utilities.PLAYER_ID, currentPlayerId);
                fragment.setArguments(bundle);
                return fragment;
            } else {
                throw new RuntimeException("Something strange happened");
            }
        }

        @Override
        public int getCount() {
            final int NUMBER_OF_CARDS = 3;
            return NUMBER_OF_CARDS;
        }
    }

    class TradeSequenceCreator implements View.OnClickListener {
        private int floorId;

        public TradeSequenceCreator(int floorId) {
            this.floorId = floorId;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(FloorActivity.this, PreTradePickPlayer.class);
            intent.putExtra(Utilities.FLOOR_ID, floorId);
            startActivity(intent);
        }
    }

    class FloorFoundListener extends BroadcastReceiver {
        public static final String TAG = "FloorFoundListener";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Utilities.FOUND_STARTING_FLOOR) || intent.getAction().equals(Utilities.LOAD_NEW_FLOOR)) {
                int floorId = intent.getIntExtra(Utilities.FLOOR_ID, Utilities.UNKNOWN_ID);
                if (floorId == Utilities.UNKNOWN_ID)
                    throw new RuntimeException("Something bad happened");
                fab.setOnClickListener(new TradeSequenceCreator(floorId));
            }
        }
    }
}
