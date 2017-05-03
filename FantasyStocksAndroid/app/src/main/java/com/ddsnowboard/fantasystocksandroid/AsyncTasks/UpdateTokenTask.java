package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * This updates the Firebase token on the server
 */

public class UpdateTokenTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        String newToken = FirebaseInstanceId.getInstance().getToken();
        FantasyStocksAPI.getInstance().registerFirebaseId(newToken);
        return null;
    }
}
