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
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetStocksTask;
import com.google.gson.Gson;
import com.jameswk2.FantasyStocksAPI.AbbreviatedFloor;
import com.jameswk2.FantasyStocksAPI.AbbreviatedPlayer;
import com.jameswk2.FantasyStocksAPI.AbbreviatedStock;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.FullFloor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.Stock;
import com.jameswk2.FantasyStocksAPI.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
    public static final String FLOOR = "floor";
    public static final String FLOORS = "floors";
    public static final int GET_NEW_FLOOR = 1;

    ListView drawer;

    StockFragment stockFragment;
    FloatingActionButton fab;
    ProgressDialog progress;

    ArrayList<Stock> stockArray = new ArrayList<>();

    ArrayList<Player> playerArray = new ArrayList<>();

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
        }

        drawer = (ListView) findViewById(R.id.drawer);
        findViewById(R.id.joinFloor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This will need to be changed too
                final int NUM_DUMMY_FLOORS = 10;
                Intent intent = new Intent(FloorActivity.this, JoinFloor.class);
                Floor[] floors = new Floor[NUM_DUMMY_FLOORS];
                for(int i = 1; i <= NUM_DUMMY_FLOORS; i++) {
                    floors[i - 1] = new DummyFloor(String.format("Floor%d", i));
                }
                Gson gson = new Gson();
                String jsonString = gson.toJson(floors, DummyFloor[].class);
                intent.putExtra(FLOORS, jsonString);
                startActivityForResult(intent, GET_NEW_FLOOR);
            }
        });

        // This needs to be replaced with real floors, but I need the caching stuff for that
        ArrayList<Floor> dummyFloors = new ArrayList<>();
        Field name;
        try {
            name = AbbreviatedFloor.class.getDeclaredField("name");
            name.setAccessible(true);
        }
        catch (NoSuchFieldException e){
            throw new RuntimeException(e.toString());
        }

        for(int i = 1; i <= 3; i++) {
            AbbreviatedFloor currFloor = new AbbreviatedFloor();
            try {
                name.set(currFloor, String.format("Floor number %d", i));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.toString());
            }
            dummyFloors.add(currFloor);
        }
        Log.d(TAG, dummyFloors.toString());
        FloorListAdapter floorListAdapter = new FloorListAdapter(this, dummyFloors);
        drawer.setAdapter(floorListAdapter);

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
                StockFragment.stocks.add(stock);
            progress.hide();
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
    public void onListFragmentInteraction(Object o) {
        // Make a new trade with that stock
        if(o instanceof Stock) {
            Stock s = (Stock) o;
            Intent intent = new Intent(this, FirstLevelTrade.class);
            // This is also going to need the caching
            intent.putExtra(FirstLevelTrade.USER, "{\"username\": \"Aaron Burr\"}");
            intent.putExtra(FirstLevelTrade.STOCK, "{\"symbol\": \"AAPL\"}");
            startActivity(intent);
        }
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
                Stock[] dummyStocks = new Stock[]{new AbbreviatedStock(),
                        new AbbreviatedStock(),
                        new AbbreviatedStock()};
                try {
                    Field nameField = AbbreviatedStock.class.getDeclaredField("symbol");
                    nameField.setAccessible(true);
                    Field priceField = AbbreviatedStock.class.getDeclaredField("price");
                    priceField.setAccessible(true);



                    nameField.set(dummyStocks[0], "AAPL");
                    priceField.set(dummyStocks[0], 1.23);

                    nameField.set(dummyStocks[1], "VZW");
                    priceField.set(dummyStocks[1], 2.23);

                    nameField.set(dummyStocks[2], "GOOG");
                    priceField.set(dummyStocks[2], 3.23);
                }
                catch (NoSuchFieldException | IllegalAccessException e) { throw new RuntimeException(e.toString());}

                String jsonString = gson.toJson(dummyStocks, AbbreviatedStock[].class);
                // TODO: Make this actually work
                // String jsonString = gson.toJson(stocks.toArray(new Stock[0]), AbbreviatedStock[].class);
                Log.d(TAG, "JsonString is " + jsonString);
                bundle.putString(StockFragment.STOCKS, jsonString);
                stocksFragment.setArguments(bundle);
                return stocksFragment;
            } else if (position == PLAYERS_PAGE) {
                PlayerFragment fragment = new PlayerFragment();
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                Log.d(TAG, "players is " + playerArray.toString());
                Player[] dummyPlayers = new Player[]{new AbbreviatedPlayer(),
                        new AbbreviatedPlayer(),
                        new AbbreviatedPlayer()};
                try {
                    // Field usernameField = AbbreviatedPlayer.class.getDeclaredField("username");
                    // usernameField.setAccessible(true);
                    Field pointField = AbbreviatedPlayer.class.getDeclaredField("points");
                    pointField.setAccessible(true);



                    // usernameField.set(dummyPlayers[0], "Ned Schnebly");
                    pointField.setInt(dummyPlayers[0], 500);

                    // usernameField.set(dummyPlayers[1], "Dewey Finn");
                    pointField.setInt(dummyPlayers[1], 213);

                    // usernameField.set(dummyPlayers[2], "Summer Hathaway");
                    pointField.setInt(dummyPlayers[2], 123);
                }
                catch (NoSuchFieldException | IllegalAccessException e) { throw new RuntimeException(e.toString());}

                String jsonString = gson.toJson(dummyPlayers, AbbreviatedPlayer[].class);
                // TODO: Make this actually work
                Log.d(TAG, "JsonString is " + jsonString);
                bundle.putString(PlayerFragment.PLAYERS, jsonString);
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
        if(requestCode == GET_NEW_FLOOR) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Joined a new floor", Toast.LENGTH_LONG).show();
                // Actually join the floor...
            }
        }
    }

    class DummyFloor implements Floor {
        private String name;
        public DummyFloor(String name) {
            this.name = name;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Stock[] getStocks() {
            return new Stock[0];
        }

        @Override
        public FullFloor.Permissiveness getPermissiveness() {
            return null;
        }

        @Override
        public User getOwner() {
            return null;
        }

        @Override
        public Player getFloorPlayer() {
            return null;
        }

        @Override
        public boolean isPublic() {
            return false;
        }

        @Override
        public int getNumStocks() {
            return 0;
        }
    }
}
