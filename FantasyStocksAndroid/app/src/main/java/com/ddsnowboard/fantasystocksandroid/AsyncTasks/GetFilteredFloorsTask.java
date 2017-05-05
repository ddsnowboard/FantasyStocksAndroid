package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.Floor;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

/**
 * This gets all the floors in the database and filters them according to filter. Note
 * that the IntSuppliers don't function here. 
 */

public class GetFilteredFloorsTask extends GetterTask<Floor[]> {
    Predicate<Floor> filter;

    /**
     * @param ctx The context; see the super method
     * @param consumer The consumer; see the super method
     * @param filter the filter that is used to select the floors
     */
    public GetFilteredFloorsTask(Context ctx, Consumer<Floor[]> consumer, Predicate<Floor> filter) {
        super(ctx, consumer);
        this.filter = filter;
    }

    @Override
    // These IntSuppliers don't do anything here
    protected Floor[] doInBackground(IntSupplier... uselessSuppliers) {
        return Arrays.stream(Floor.getFloors()).filter(filter).toArray(Floor[]::new);
    }
}
