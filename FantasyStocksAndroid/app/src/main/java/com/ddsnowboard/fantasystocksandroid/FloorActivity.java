package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.Toast;

import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;

public class FloorActivity extends FragmentActivity implements StockFragment.OnListFragmentInteractionListener {
    public static final String TAG = "FloorActivity";
    public static final String FLOOR = "floor";
    public static final String FLOORS = "floors";
    public static final int GET_NEW_FLOOR = 1;
    public static final String USER_ID = "UserIdNumber";
    public static final String PLAYER_ID = "PlayerIDNumber";

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
            Object o = null;
            System.out.println(o.toString());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // Initialize UI components...
        drawer = (ListView) findViewById(R.id.drawer);
        drawerHandler = new FloorDrawerHandler(drawer);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), PagerAdapter.UNKNOWN_PLAYER_ID);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        findViewById(R.id.joinFloor).setOnClickListener((view) -> {
            // TODO: This needs to be changed to reflect the actual floors
            // available. I could probably just put it in the actual class
            // though and not do any work here. Appealing...
            Intent intent = new Intent(FloorActivity.this, JoinFloor.class);
            intent.putExtra(USER_ID, 42);
            startActivityForResult(intent, GET_NEW_FLOOR);
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void onListFragmentInteraction(Object o) {
        // Make a new trade with that stock
        if (o instanceof Stock) {
            Stock s = (Stock) o;
            Intent intent = new Intent(this, FirstLevelTrade.class);
            // This is also going to need the caching
            intent.putExtra(FirstLevelTrade.PLAYER, "This is some text");
            intent.putExtra(FirstLevelTrade.STOCK, "{\"symbol\": \"AAPL\"}");
            startActivity(intent);
        }
        else if(o instanceof Player) {
            // Make a trade with this player
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {
        public static final int UNKNOWN_PLAYER_ID = -1;

        final int STOCKS_PAGE = 0;
        final int PLAYERS_PAGE = 1;

        int currentPlayerId;

        public PagerAdapter(FragmentManager fm, int startingPlayerId) {
            super(fm);
            currentPlayerId = startingPlayerId;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == STOCKS_PAGE) {
                StockFragment stocksFragment = new StockFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PLAYER_ID, currentPlayerId);
                stocksFragment.setArguments(bundle);
                return stocksFragment;
            } else if (position == PLAYERS_PAGE) {
                // TODO: Some stuff has to happen here, but that's for later.
                PlayerFragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PLAYER_ID, currentPlayerId);
                fragment.setArguments(bundle);
                return fragment;
            } else {
                throw new RuntimeException("Something strange happened");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_NEW_FLOOR) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Joined a new floor", Toast.LENGTH_LONG).show();
                // TODO: Actually join the floor...
            }
        }
    }
}
