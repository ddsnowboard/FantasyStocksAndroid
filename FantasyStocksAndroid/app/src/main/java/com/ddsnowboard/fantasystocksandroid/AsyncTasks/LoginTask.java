package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.os.AsyncTask;

import com.ddsnowboard.fantasystocksandroid.FirebaseIdService;
import com.ddsnowboard.fantasystocksandroid.LoginActivity;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * This logs in given a username and a password. It also notifies the server of the current Firebase
 * Id, since the API documentation clearly says that sending the same Id twice is not an error.
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
            new FirebaseIdService().onTokenRefresh();
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
