package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Player;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This, given a `Player`'s id, returns that Player
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
