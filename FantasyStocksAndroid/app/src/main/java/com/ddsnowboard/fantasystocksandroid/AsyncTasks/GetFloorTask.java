package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This gets the floor specified by the id returned by the first floorIdGenerator 
 */

public class GetFloorTask extends GetterTask<Floor> {

    public GetFloorTask(Context ctx, Consumer<Floor> cb) {
        super(ctx, cb);
    }

    @Override
    protected Floor doInBackground(IntSupplier... floorIdGenerators) {
        // You'll notice, if you're particularly astute, that I'm getting the id of a floor object
        // only to turn it back into a floor. This is because I want a full Floor object and not an abbreviated one.
        int floorId = floorIdGenerators[0].getAsInt();
        if (floorId == -1) {
            // We should get the first floor that this guy owns
            User u = Utilities.login(ctx);
            Player p = u.getPlayers()[0];
            floorId = p.getFloor().getId();
        }
        return Floor.get(floorId);
    }
}
