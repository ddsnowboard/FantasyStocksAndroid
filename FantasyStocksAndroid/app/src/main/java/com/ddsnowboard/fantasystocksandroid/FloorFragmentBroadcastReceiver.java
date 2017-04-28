package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetFloorTask;
import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/27/17.
 */

public class FloorFragmentBroadcastReceiver extends BroadcastReceiver {
    public static final String GET_FLOOR = "getafloor";
    public static final String FLOOR_ID = "floorIdNumber";
    private final Consumer<Floor> callback;

    public FloorFragmentBroadcastReceiver(Consumer<Floor> cb) {
        this.callback = cb;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(GET_FLOOR)) {
            int id = intent.getIntExtra(GET_FLOOR, -1);
            if(id == -1)
                throw new RuntimeException("Something bad happened");
            new GetFloorTask(context, f -> callback.accept(f)).execute(() -> id);
        }
    }
}
