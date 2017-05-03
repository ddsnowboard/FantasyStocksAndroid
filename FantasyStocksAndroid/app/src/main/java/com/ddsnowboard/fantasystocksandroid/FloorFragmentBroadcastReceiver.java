package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * This is a BroadcastReceiver for when you want to go get some data and do something with it 
 * on every broadcast. You just instantiate it with a callback and one of the GetterTasks, and it 
 * takes care of the rest. 
 */

public class FloorFragmentBroadcastReceiver<T extends GetterTask<J>, J> extends BroadcastReceiver {
    private final Consumer<J> callback;
    private final Constructor<T> constructor;

    public FloorFragmentBroadcastReceiver(Consumer<J> cb, Class<T> klass) {
        this.callback = cb;
        constructor = (Constructor<T>) klass.getConstructors()[0];
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Utilities.LOAD_NEW_FLOOR)) {
            int id = intent.getIntExtra(Utilities.FLOOR_ID, Utilities.UNKNOWN_ID);
            if (id == Utilities.UNKNOWN_ID)
                throw new RuntimeException("Something bad happened");
            try {
                constructor.newInstance(context, callback).execute(() -> id);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Something bad happened with the template witchcraft");
            }
        }
    }
}
