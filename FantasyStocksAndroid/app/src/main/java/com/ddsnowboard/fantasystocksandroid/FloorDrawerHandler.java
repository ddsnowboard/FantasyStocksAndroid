package com.ddsnowboard.fantasystocksandroid;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetFloorsTask;
import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class FloorDrawerHandler {
    public static final String TAG = "FloorDrawerHandler";

    private final ListView drawer;
    private final DrawerLayout masterView;
    private ArrayAdapter<Floor> adapter;
    private ArrayList<Floor> floors = new ArrayList<>();

    public FloorDrawerHandler(ListView drawer) {
        this.drawer = drawer;
        // Don't tell anyone I did this...
        ViewParent possibleDrawer = drawer.getParent();
        while (!(possibleDrawer instanceof DrawerLayout))
            possibleDrawer = possibleDrawer.getParent();
        masterView = (DrawerLayout) possibleDrawer;

        adapter = new FloorListAdapter(drawer.getContext(), floors);
        GetFloorsTask task = new GetFloorsTask(drawer.getContext(), floors -> {
            Arrays.stream(floors).forEach(f -> this.floors.add(f));
            adapter.notifyDataSetChanged();
        });
        task.execute();
        drawer.setAdapter(adapter);
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                masterView.closeDrawers();
                Intent intent = new Intent(Utilities.LOAD_NEW_FLOOR);
                intent.putExtra(Utilities.FLOOR_ID, floors.get(idx).getId());
                drawer.getContext().sendBroadcast(intent);
            }
        });
    }
}
