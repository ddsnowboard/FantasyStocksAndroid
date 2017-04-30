package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by ddsnowboard on 4/28/17.
 */

public abstract class GetterTask<T> extends AsyncTask<IntSupplier, Void, T> {
    Context ctx;
    Consumer<T> consumer;

    public GetterTask(Context ctx, Consumer<T> consumer) {
        this.consumer = consumer;
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        consumer.accept(t);
    }
}
