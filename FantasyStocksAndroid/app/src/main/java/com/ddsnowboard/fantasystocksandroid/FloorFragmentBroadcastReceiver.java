package com.ddsnowboard.fantasystocksandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ddsnowboard.fantasystocksandroid.AsyncTasks.GetterTask;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import static com.ddsnowboard.fantasystocksandroid.Utilities.FLOOR_ID;
import static com.ddsnowboard.fantasystocksandroid.Utilities.LOAD_NEW_FLOOR;
import static com.ddsnowboard.fantasystocksandroid.Utilities.UNKNOWN_ID;

/**
 * Created by ddsnowboard on 4/27/17.
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
        if (intent.getAction().equals(LOAD_NEW_FLOOR)) {
            int id = intent.getIntExtra(FLOOR_ID, UNKNOWN_ID);
            if (id == UNKNOWN_ID)
                throw new RuntimeException("Something bad happened");
            try {
                constructor.newInstance(context, callback).execute(() -> id);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Something bad happened with the template witchcraft");
            }
        }
    }
}
