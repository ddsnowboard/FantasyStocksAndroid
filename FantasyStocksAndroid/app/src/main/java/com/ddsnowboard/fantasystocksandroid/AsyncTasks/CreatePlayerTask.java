package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.function.Consumer;
import java.util.function.IntSupplier;


/**
 * Created by ddsnowboard on 5/1/17.
 */

public class CreatePlayerTask extends AsyncTask<IntSupplier, Void, Player> {
    public static final String TAG = "CreatePlayerTask";
    Consumer<Player> callback;

    public CreatePlayerTask(Consumer<Player> callback) {
        super();
        this.callback = callback;
    }

    @Override
    protected Player doInBackground(IntSupplier... floorIdSuppliers) {
        int floorId = floorIdSuppliers[0].getAsInt();
        User user = FantasyStocksAPI.getInstance().getUser();
        Floor floor = Floor.get(floorId);
        Log.e(TAG, String.format("About to create a new player on floor %s", floor.getName()));
        return Player.create(user, floor);
    }

    @Override
    protected void onPostExecute(Player aPlayer) {
        super.onPostExecute(aPlayer);
        this.callback.accept(aPlayer);
    }
}
