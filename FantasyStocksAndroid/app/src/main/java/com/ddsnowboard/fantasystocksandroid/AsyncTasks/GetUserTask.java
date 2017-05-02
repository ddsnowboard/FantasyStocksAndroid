package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;

import com.jameswk2.FantasyStocksAPI.User;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 5/1/17.
 */

public class GetUserTask extends GetterTask<User> {

    public GetUserTask(Context ctx, Consumer<User> consumer) {
        super(ctx, consumer);
    }

    @Override
    protected User doInBackground(IntSupplier... userIdSuppliers) {
        int id = userIdSuppliers[0].getAsInt();
        return User.get(id);
    }
}
