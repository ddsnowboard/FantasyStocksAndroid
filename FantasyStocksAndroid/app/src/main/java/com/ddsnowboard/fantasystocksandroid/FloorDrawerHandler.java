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
 * Created by ddsnowboard on 4/28/17.
 */

public class FloorDrawerHandler {
    public static final String TAG = "FloorDrawerHandler";

    private final ListView drawer;
    private final DrawerLayout masterView;
    private ArrayAdapter<Floor> adapter;
    private ArrayList<Floor> floors = new ArrayList<>();

    public FloorDrawerHandler(ListView drawer) {
        TextView joinFloorButton = new TextView(drawer.getContext());
        joinFloorButton.setText(R.string.joinFloor);
        ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT);
        joinFloorButton.setLayoutParams(lp);
        joinFloorButton.setGravity(Gravity.LEFT);
        joinFloorButton.setTextColor(Color.BLACK);
        joinFloorButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, 11);

        joinFloorButton.setOnClickListener((view) -> {
            // TODO: This needs to be changed to reflect the actual floors
            // available. I could probably just put it in the actual class
            // though and not do any work here. Appealing...
            Intent intent = new Intent(drawer.getContext(), JoinFloor.class);
            drawer.getContext().startActivity(intent);
            // Do I have to finish the activity here or something? I'll worry about it later.
        });

        this.drawer = drawer;
        // Don't tell anyone I did this...
        ViewParent possibleDrawer = drawer.getParent();
        while (!(possibleDrawer instanceof DrawerLayout))
            possibleDrawer = possibleDrawer.getParent();
        masterView = (DrawerLayout) possibleDrawer;

        adapter = new FloorListAdapter(drawer.getContext(), floors);
        drawer.setAdapter(adapter);

        GetFloorsTask task = new GetFloorsTask(drawer.getContext(), floors -> {
            Arrays.stream(floors).forEach(f -> this.floors.add(f));
            adapter.notifyDataSetChanged();
            // Have selected the first element in the list
            getViewByPosition(0, drawer).setBackgroundColor(0xFFDDDDDD);
            drawer.addFooterView(joinFloorButton);
        });
        task.execute();
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    getViewByPosition(i, drawer).setBackgroundColor(Color.WHITE);
                }
                getViewByPosition(idx, drawer).setBackgroundColor(0xFFDDDDDD);
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

    /**
     * Stolen from StackOverflow's VVB
     *
     * @param pos      the position of the view
     * @param listView the ListView to look in
     * @return the view at position `pos`
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        Log.d(TAG, String.format("first position is %d, last position is %d", firstListItemPosition, lastListItemPosition));

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
