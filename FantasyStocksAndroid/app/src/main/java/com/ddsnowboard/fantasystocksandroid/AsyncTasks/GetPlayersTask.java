package com.ddsnowboard.fantasystocksandroid.AsyncTasks;

import android.content.Context;
import android.util.Log;

import com.ddsnowboard.fantasystocksandroid.Utilities;
import com.jameswk2.FantasyStocksAPI.Floor;
import com.jameswk2.FantasyStocksAPI.Player;
import com.jameswk2.FantasyStocksAPI.User;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

/**
 * This gets all the player on a Floor specified by the first floorIdSupplier, or the first floor
 * that the logged-in User belongs to.
 */

public class GetPlayersTask extends GetterTask<Player[]> {
    private static final String TAG = "GetPlayersTask";
    public GetPlayersTask(Context ctx, Consumer<Player[]> cb) {
        super(ctx, cb);
    }

    @Override
    protected Player[] doInBackground(IntSupplier... floorIdSuppliers) {
        int floorId = floorIdSuppliers[0].getAsInt();
        if (floorId == -1) {
            // We should get the first floor that this guy owns
            User u = Utilities.login(ctx);
            Player p = u.getPlayers()[0];
            floorId = p.getFloor().getId();
        }
        Floor floor = Floor.get(floorId);
        Stream<Player> players = Arrays.stream(Player.getPlayers());
        players = players.filter(p -> p.getFloor().equals(floor));
        players = players.map(p -> Player.get(p.getId()));
        return players.toArray(Player[]::new);
    }

}
