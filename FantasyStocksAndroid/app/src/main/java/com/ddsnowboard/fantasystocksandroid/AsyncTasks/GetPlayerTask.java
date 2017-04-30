package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Player;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 4/29/17.
 */

public class GetPlayerTask extends GetterTask<Player> {

    public GetPlayerTask(Context ctx, Consumer<Player> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected Player doInBackground(IntSupplier... playerIdSupplier) {
        return Player.get(playerIdSupplier[0].getAsInt());
    }
}
