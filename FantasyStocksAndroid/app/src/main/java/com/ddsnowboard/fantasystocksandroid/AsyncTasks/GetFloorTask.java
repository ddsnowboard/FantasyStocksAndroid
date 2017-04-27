package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;

import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/27/17.
 */

public class GetFloorTask extends AsyncTask<Integer, Void, Floor> {
    private final Consumer<Floor> callback;

    public GetFloorTask(Consumer<Floor> cb)
    {
        this.callback = cb;
    }

    @Override
    protected Floor doInBackground(Integer... integers) {
        // Get the floor from the server
    }

    @Override
    protected void onPostExecute(Floor floor) {
        super.onPostExecute(floor);
        callback.accept(floor);
    }
}
