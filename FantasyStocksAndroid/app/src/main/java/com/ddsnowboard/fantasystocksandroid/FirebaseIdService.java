package com.ddsnowboard.fantasystocksandroid;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * Created by ddsnowboard on 5/1/17.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {
    public static final String TAG = "FirebaseIdService";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String newToken = FirebaseInstanceId.getInstance().getToken();
        try {
            FantasyStocksAPI.getInstance().registerFirebaseId(newToken);
        }
        catch (UnsupportedOperationException e){
            Log.e(TAG, "We need to log in before we can send the FirebaseId");
        }
    }
}
