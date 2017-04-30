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
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public class GetFloorsTask extends GetterTask<Floor[]> {
    public static final String TAG = "GetFloorsTask";

    public GetFloorsTask(Context ctx, Consumer<Floor[]> cb) {
        super(ctx, cb);
    }

    @Override
    protected Floor[] doInBackground(IntSupplier... voids) {
        // This one doesn't actually use the int suppliers. You can still use it though.
        // If I wanted to I could make them optional, using them if they're there and using the
        // context otherwise.
        User u = Utilities.login(ctx);
        return Arrays.stream(Player.getPlayers())
                .filter(p -> p.getUser().equals(u))
                .map(p -> p.getFloor())
                .toArray(Floor[]::new);
    }

}
