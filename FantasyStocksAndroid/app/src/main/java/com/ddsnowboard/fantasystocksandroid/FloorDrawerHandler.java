package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetFloorsTask;
import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This takes care of all the complexity in the drawer that lets the user select what Floor they want to look at
 */

public class FloorDrawerHandler {
    public static final String TAG = "FloorDrawerHandler";

    private final ListView drawer;
    private final DrawerLayout masterView;
    private ArrayAdapter<Floor> adapter;
    private ArrayList<Floor> floors = new ArrayList<>();

    public FloorDrawerHandler(ListView drawer) {
        this.drawer = drawer;

        /* The "Join Floor" button is dynamically added here
        I couldn't do it in the xml because I had to use the special addFooterView
        method, and that couldn't be accessed from xml. Note that this is actually
        added to the ListView below, in the lambda of the GetFloorsTask.
         */
        TextView joinFloorButton = new TextView(drawer.getContext());
        joinFloorButton.setText(R.string.joinFloor);
        ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
        joinFloorButton.setLayoutParams(lp);
        joinFloorButton.setGravity(Gravity.LEFT);
        joinFloorButton.setTextColor(Color.BLACK);
        joinFloorButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 11);

        joinFloorButton.setOnClickListener((view) -> {
            Intent intent = new Intent(drawer.getContext(), JoinFloor.class);
            drawer.getContext().startActivity(intent);
        });

        // This gets the actual DrawerLayout in a semi-extensible way
        ViewParent possibleDrawer = drawer.getParent();
        while (!(possibleDrawer instanceof DrawerLayout))
            possibleDrawer = possibleDrawer.getParent();
        masterView = (DrawerLayout) possibleDrawer;

        adapter = new FloorListAdapter(drawer.getContext(), floors);
        drawer.setAdapter(adapter);

        GetFloorsTask task = new GetFloorsTask(drawer.getContext(), floors -> {
            Arrays.stream(floors).forEach(this.floors::add);
            adapter.notifyDataSetChanged();

            drawer.addFooterView(joinFloorButton);
        });
        task.execute();

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                masterView.closeDrawers();

                Intent intent = new Intent(Utilities.LOAD_NEW_FLOOR);
                intent.putExtra(Utilities.FLOOR_ID, floors.get(idx).getId());
                drawer.getContext().sendBroadcast(intent);
            }
        });


        // Whenever we load a new floor, we should reload the list of floors,
        // in case we added the new floor because we joined it.
        IntentFilter filter = new IntentFilter(Utilities.LOAD_NEW_FLOOR);
        drawer.getContext().registerReceiver(new FloorFragmentBroadcastReceiver<>(floors -> {
            FloorDrawerHandler.this.floors.clear();
            Arrays.stream(floors).forEach(FloorDrawerHandler.this.floors::add);
            adapter.notifyDataSetChanged();
        }, GetFloorsTask.class), filter);
    }
}
