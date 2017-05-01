package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

/**
 * Created by ddsnowboard on 5/1/17.
 */

public class GetAllFloorsTask extends GetterTask<Floor[]> {
    Predicate<Floor> filter;
    public GetAllFloorsTask(Context ctx, Consumer<Floor[]> consumer) {
        this(ctx, consumer, f -> true);
    }

    public GetAllFloorsTask(Context ctx, Consumer<Floor[]> consumer, Predicate<Floor> filter) {
        super(ctx, consumer);
        this.filter = filter;
    }

    @Override
    // These IntSuppliers don't do anything here
    protected Floor[] doInBackground(IntSupplier... uselessSuppliers) {
        return Arrays.stream(Floor.getFloors()).filter(filter).toArray(Floor[]::new);
    }
}
