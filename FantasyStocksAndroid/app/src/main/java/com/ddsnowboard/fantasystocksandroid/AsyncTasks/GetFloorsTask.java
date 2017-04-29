package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class GetFloorsTask extends AsyncTask<Void, Void, Floor[]> {
    public static final String TAG = "GetFloorsTask";
    Context ctx;
    Consumer<Floor[]> callback;

    public GetFloorsTask(Context ctx, Consumer<Floor[]> cb) {
        this.ctx = ctx;
        this.callback = cb;
    }

    @Override
    protected Floor[] doInBackground(Void... voids) {
        User u = Utilities.login(ctx);
        return Arrays.stream(Player.getPlayers())
                .filter(p -> p.getUser().equals(u))
                .map(p -> p.getFloor())
                .toArray(Floor[]::new);
    }

    @Override
    protected void onPostExecute(Floor[] floors) {
        if (callback != null) {
            callback.accept(floors);
        }
    }
}
