package com.ddsnowboard.fantasystocksandroid;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jameswk2.FantasyStocksAPI.FantasyStocksAPI;

/**
 * Created by ddsnowboard on 5/1/17.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String newToken = FirebaseInstanceId.getInstance().getToken();
        FantasyStocksAPI.getInstance().registerFirebaseId(newToken);
    }
}
