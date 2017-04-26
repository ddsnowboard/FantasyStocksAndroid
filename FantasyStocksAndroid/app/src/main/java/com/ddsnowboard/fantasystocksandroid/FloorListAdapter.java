package com.ddsnowboard.fantasystocksandroid;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.ArrayList;

/**
 * Created by ddsnowboard on 4/25/17.
 */

public class FloorListAdapter extends ArrayAdapter<Floor> {
    ArrayList<Floor> floors;
    @LayoutRes static final int RESOURCE = R.layout.floor_list_container;

    public FloorListAdapter(@NonNull Context context, ArrayList<Floor> floors) {
        super(context, RESOURCE);
        this.floors = floors;
        addAll(floors);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Floor floor = floors.get(position);
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.floor_list_container,
                    parent, false);
        ((TextView) view).setText(floor.getName());
        return view;
    }
}
