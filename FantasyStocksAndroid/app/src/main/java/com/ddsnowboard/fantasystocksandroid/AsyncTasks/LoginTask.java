package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.LoginActivity;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * Created by ddsnowboard on 4/24/17.
 */

public class LoginTask extends AsyncTask<String, Void, Boolean> {
    private LoginActivity caller;

    public LoginTask(LoginActivity activity) {
        super();
        caller = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        FantasyStocksAPI api = FantasyStocksAPI.getInstance();
        String username = strings[0];
        String password = strings[1];
        try {
            api.login(username, password);
        } catch (IllegalArgumentException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        caller.finishLogin(result);
    }
}
