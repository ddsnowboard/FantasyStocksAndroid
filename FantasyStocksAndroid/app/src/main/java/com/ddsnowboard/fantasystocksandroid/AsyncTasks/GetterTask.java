package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * This is the abstract base class for all the `AsyncTask`s that just get something
 * and expose it to a Consumer. Since they all do almost the same thing, this saves code
 * by factoring out the common functionality. In other news, I learned about generics.
 * Also, note that the IntSupplier arguments will be executed on the worker thread, but
 * the callback will be on the UI thread
 */

public abstract class GetterTask<T> extends AsyncTask<IntSupplier, Void, T> {
    Context ctx;
    Consumer<T> consumer;

    /**
     * @param ctx the context
     * @param consumer the consumer, which is used to replace the need to override onPostExecute
     */
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
