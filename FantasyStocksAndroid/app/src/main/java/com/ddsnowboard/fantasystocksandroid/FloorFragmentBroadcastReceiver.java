package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * Created by ddsnowboard on 4/27/17.
 */

public class FloorFragmentBroadcastReceiver<T extends GetterTask<J>, J> extends BroadcastReceiver {
    public static final String LOAD_NEW_FLOOR = "getafloor";
    public static final String FLOOR_ID = "floorIdNumber";
    private final Consumer<J> callback;
    private final Constructor<T> constructor;

    public FloorFragmentBroadcastReceiver(Consumer<J> cb, Class<T> klass) {
        this.callback = cb;
        constructor = (Constructor<T>) klass.getConstructors()[0];
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LOAD_NEW_FLOOR)) {
            final int BAD_VALUE = -1;
            int id = intent.getIntExtra(FLOOR_ID, BAD_VALUE);
            if (id == BAD_VALUE)
                throw new RuntimeException("Something bad happened");
            try {
                constructor.newInstance(context, callback).execute(() -> id);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Something bad happened with the template witchcraft");
            }
        }
    }
}
